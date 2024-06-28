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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
        if (user.isEnabled()) {
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

//    public boolean login(UserLoginDTO userDTO) {
//        User user = userRepository.findByUsername(userDTO.getUsername());
//
//        if (user != null && user.isEnabled() &&
//                passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
//
//            user.setAccesso(true);
//            userRepository.save(user);
//            return true;
//        }
//
//        return false;
//    }

//    @PreAuthorize("permitAll()")
//        if (userRepository.existsByUsername(userDTO.getUsername())) {
//            log.error("Username already exists: " + userDTO.getUsername());
//            // Gestione dell'errore o avviso, ad esempio lanciare un'eccezione o ritornare un messaggio di errore
//            throw new IllegalArgumentException("Username already exists");
//        }
//public void createUser(UserDTO userDTO) {
//    if (userRepository.existsByUsername(userDTO.getUsername())) {
//        throw new IllegalArgumentException("Username already exists: " + userDTO.getUsername());
//    }
//    if (userRepository.existsByEmail(userDTO.getEmail())) {
//        throw new IllegalArgumentException("Email already exists: " + userDTO.getEmail());
//    }
//    User user = userMapper.userDTOToUser(userDTO);
//    user.setPassword(passwordEncoder.encode(user.getPassword()));
//    user.setRegistrato(true);
//    userRepository.save(user);
//}

    public boolean createUser(UserDTO userDTO) throws MessagingException {
        if (userRepository.existsByUsernameOrEmail(userDTO.getUsername(), userDTO.getEmail())) {
//            throw new IllegalArgumentException("Username or Email already exists");
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
        // Pubblica l'evento OnRegistrationCompleteEvent
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(user, confirmationUrl));
//        emailService.sendEmailConfirm(user.getEmail(), confirmationUrl);
        return true;
    }

    @Transactional
    public void deleteVotazioniByUserAndUuidVotazione  (UUID uuidUser, UUID uuidVotazione) {
        User user = userRepository.findByUuidUser(uuidUser)
                .orElseThrow(() -> new EntityNotFoundException("User with UUID " + uuidUser + " not found"));

        votazioneRepository.deleteByUserAndUuidVotazione(user, uuidVotazione);
    }






    //    @PreAuthorize("isAuthenticated()")
    @Transactional
    public void deleteAllVotazioniByUser(UUID uuidUser) {
        User user = userRepository.findByUuidUser(uuidUser)
                .orElseThrow(() -> new EntityNotFoundException("User with UUID " + uuidUser + " not found"));
        List<Votazione> votazioni = user.getVotazione();

        votazioni.forEach(votazioneRepository::delete);
        votazioni.clear();
        userRepository.save(user);
    }

//    @PreAuthorize("isAuthenticated()")
    public List<VotazioneDTO> findAllByUuidUser(UUID uuidUser) {
        User user = userRepository.findByUuidUser(uuidUser)
                .orElseThrow(() -> new EntityNotFoundException("User with UUID " + uuidUser + " not found"));
        return user.getVotazione().stream()
                .map(votazioneMapper::votazioneToVotazioneDto)
                .collect(Collectors.toList());
    }

//    @PreAuthorize("isAuthenticated()")
    public VotazioneDTO findVotazioneByUuidUserUuidVotazione(UUID uuidUser, UUID uuidVotazione) {
        User user = userRepository.findByUuidUser(uuidUser)
                .orElseThrow(() -> new EntityNotFoundException("User with UUID " + uuidUser + " not found"));
        Votazione votazione = user.getVotazione().stream()
                .filter(v -> v.getUuidVotazione().equals(uuidVotazione))
                .findAny()
                .orElseThrow(() -> new EntityNotFoundException("Votazione with UUID " + uuidVotazione + " not found"));
        return votazioneMapper.votazioneToVotazioneDto(votazione);
    }

    public Votazione findVotazioneEntityByUuidUserUuidVotazione(UUID uuidUser, UUID uuidVotazione) {
        User user = userRepository.findByUuidUser(uuidUser)
                .orElseThrow(() -> new EntityNotFoundException("User with UUID " + uuidUser + " not found"));
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

    //nel momento in cui l utente clicca il link arrivato per email parte questa chiamata
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
        user.setVerificationToken(null); // Eliminazione token dopo la verifica
        user.setTokenCreationDate(null); // Rimozione data di creazione del token
        userRepository.save(user);
        log.info("Token validated and user enabled: {}", user.getUsername());
        return "valid";
    }


}
