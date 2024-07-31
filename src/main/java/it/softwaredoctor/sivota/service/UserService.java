package it.softwaredoctor.sivota.service;

import it.softwaredoctor.sivota.dto.UserDTO;
import it.softwaredoctor.sivota.dto.UserLoginDTO;
import it.softwaredoctor.sivota.dto.VotazioneDTO;
import it.softwaredoctor.sivota.mapper.UserMapper;
import it.softwaredoctor.sivota.mapper.VotazioneMapper;
import it.softwaredoctor.sivota.model.OnRegistrationCompleteEvent;
import it.softwaredoctor.sivota.model.User;
import it.softwaredoctor.sivota.model.Votazione;
import it.softwaredoctor.sivota.repository.UserRepository;
import it.softwaredoctor.sivota.repository.VotazioneRepository;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final VotazioneRepository votazioneRepository;
    private final VotazioneMapper votazioneMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CustomUserDetailsService customUserDetailsService;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;


    public boolean login(UserLoginDTO userDTO) {
        User user = userRepository.findByUsername(userDTO.getUsername());
        if (user == null) {
            log.warn("Login failed: user not found for username {}", userDTO.getUsername());
            return false;
        }
        if (!user.isEnabled()) {
            log.warn("Login failed: user {} is not enabled", userDTO.getUsername());
            return false;
        }
        if (!passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            log.warn("Login failed: incorrect password for user {}", userDTO.getUsername());
            return false;
        }
        user.setAccesso(true);
        userRepository.save(user);
        log.info("User {} logged in successfully", userDTO.getUsername());
        return true;
    }

    @Transactional
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            User user = getUserFromUserDetails2();
            if (user != null && user.isAccesso() && user.isEnabled()) {
                user.setAccesso(false);
                user.setEnabled(false);
                userRepository.save(user);
                log.info("User {} logged out successfully", user.getUsername());
            } else {
                log.warn("Logout failed: User {} is already logged out or disabled", user.getUsername());
            }
        } else {
            log.warn("Logout failed: No authenticated user found");
        }
    }


    public boolean createUser(UserDTO userDTO) throws MessagingException {
        if (userRepository.existsByUsernameOrEmail(userDTO.getUsername(), userDTO.getEmail())) {
            return false;
        }
        User user = userMapper.userDTOToUser(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRegistrato(true);
        user.setEnabled(false);
        user.setTokenCreationDate(LocalDate.now());
        String token = UUID.randomUUID().toString();
        user.setVerificationToken(token);
        userRepository.save(user);

        String confirmationUrl = "http://localhost:8080/api/v1/user/verify-email?token=" + token + "&email=" + user.getEmail();
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, confirmationUrl));
        return true;
    }

    @Transactional
    public void deleteVotazioniByUserAndUuidVotazione  (User user,UUID uuidVotazione) {
        UserDetails userDetails = getCurrentUser();
        user = getUserFromUserDetails(userDetails);
        votazioneRepository.deleteByUserAndUuidVotazione(user, uuidVotazione);
    }

    @Transactional
    public void deleteAllVotazioniByUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails userDetails = getCurrentUser();
        User user = getUserFromUserDetails(userDetails);
        List<Votazione> votazioni = user.getVotazione();
        votazioni.forEach(votazioneRepository::delete);
        votazioni.clear();
        userRepository.save(user);
    }

    public List<VotazioneDTO> findAllByUuidUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails userDetails = getCurrentUser();
        User user = getUserFromUserDetails(userDetails);
        return user.getVotazione().stream()
                .map(votazioneMapper::votazioneToVotazioneDto)
                .collect(Collectors.toList());
    }

    public VotazioneDTO findVotazioneByUuidUserUuidVotazione(UUID uuidUser, UUID uuidVotazione) {
        User user = getUserFromUserDetails2();
        Votazione votazione = user.getVotazione().stream()
                .filter(v -> v.getUuidVotazione().equals(uuidVotazione))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Votazione with UUID " + uuidVotazione + " not found"));
        return votazioneMapper.votazioneToVotazioneDto(votazione);
    }

    public Votazione findVotazioneEntityByUuidUserUuidVotazione(UUID uuidUser, UUID uuidVotazione) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserDetails userDetails = getCurrentUser();
        User user = getUserFromUserDetails(userDetails);
        return user.getVotazione().stream()
                .filter(v -> v.getUuidVotazione().equals(uuidVotazione))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Votazione with UUID " + uuidVotazione + " not found"));
    }

    public void resetPassword(String email, String newPassword) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setPassword(newPassword);
            userRepository.save(user);
        } else {
            throw new UsernameNotFoundException("User not found with username: " + email);
        }
    }

    public String validateVerificationToken(String token) {
        log.info("Validating token: {}", token);
        User user = userRepository.findByVerificationToken(token).orElse(null);
        if (user == null) {
            log.warn("Invalid verification token: {}", token);
            return "invalid";
        }
        LocalDateTime tokenCreationDate = user.getTokenCreationDate().atStartOfDay();
        if (tokenCreationDate == null || Duration.between(tokenCreationDate, LocalDateTime.now()).toHours() > 24) {
            log.warn("Expired verification token: {}", token);
            return "expired";
        }
        user.setEnabled(true);
        user.setVerificationToken(null);
        user.setTokenCreationDate(null);
        userRepository.save(user);
        log.info("Token validated and user enabled: {}", user.getUsername());
        return "valid";
    }

    public User findByUsername(String username) {
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByUsername(username));
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
    }

    public UUID getCurrentUserUuid() {
        UserDetails userDetails = getCurrentUser();
        User user = getUserFromUserDetails(userDetails);
        return user.getUuidUser();
    }

    public UserDetails getCurrentUser() {
        return customUserDetailsService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }
    public User getUserFromUserDetails(UserDetails userDetails) {
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User non trovato con username: " + username);
        }
        return user;
    }

    public User getUserFromUserDetails2() {
        UserDetails userDetails = getCurrentUser();
        String username = userDetails.getUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RuntimeException("User non trovato con username: " + username);
        }
        return user;
    }
}
