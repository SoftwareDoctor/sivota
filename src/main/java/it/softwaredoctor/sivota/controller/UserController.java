package it.softwaredoctor.sivota.controller;

import it.softwaredoctor.sivota.dto.UserDTO;
import it.softwaredoctor.sivota.dto.UserLoginDTO;
import it.softwaredoctor.sivota.dto.VotazioneDTO;
import it.softwaredoctor.sivota.model.User;
import it.softwaredoctor.sivota.service.CustomUserDetailsService;
import it.softwaredoctor.sivota.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserLoginDTO loginUser) {
        boolean loggedIn = userService.login(loginUser);
        if (loggedIn) {
            log.info("User {} logged in successfully.", loginUser.getUsername());
            return ResponseEntity.ok().build();
        } else {
            log.warn("Failed login attempt for user {} due to incorrect credentials or disabled account.", loginUser.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            userService.logout();
            SecurityContextHolder.clearContext();
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> confirmRegistration(@RequestParam("token") String token) {
        String result = userService.validateVerificationToken(token);

        switch (result) {
            case "valid":
                return ResponseEntity.ok("Email verified successfully");
            case "invalid":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid verification token");
            case "expired":
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Verification token has expired");
            default:
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred");
        }
    }

    @PostMapping("/")
    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) throws MessagingException {
        if (!userService.createUser(userDTO)) {
            log.error("Username or Email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("{id}")
                .buildAndExpand(userDTO.getUuidUser())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @DeleteMapping("/all")
    public ResponseEntity<Void> deleteAllVotazioniByUser() {
        userService.deleteAllVotazioniByUser();
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{uuidVotazione}")
    public ResponseEntity<Void> deleteVotazioniByUserAndUuidVotazione(@PathVariable UUID uuidVotazione) {
        UserDetails userDetails = getCurrentUser();
        User user = userService.getUserFromUserDetails(userDetails);
        userService.deleteVotazioniByUserAndUuidVotazione(user, uuidVotazione);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all/")
    public ResponseEntity<List<VotazioneDTO>> findAllByUuidUser() {
        UserDetails userDetails = getCurrentUser();
        User user = userService.getUserFromUserDetails(userDetails);
        UUID uuidUser = user.getUuidUser();
        List<VotazioneDTO> votazioniDTO = userService.findAllByUuidUser();
        return ResponseEntity.ok().body(votazioniDTO);
    }

    @GetMapping("/{uuidVotazione}")
    public ResponseEntity<VotazioneDTO> findVotazioneByUuidVotazione(@PathVariable UUID uuidVotazione) {
        UserDetails userDetails = getCurrentUser();
        User user = userService.getUserFromUserDetails(userDetails);
        UUID uuidUser = user.getUuidUser();
        VotazioneDTO votazioneDTO = userService.findVotazioneByUuidUserUuidVotazione(uuidUser, uuidVotazione);
        return ResponseEntity.ok().body(votazioneDTO);
    }

    private UserDetails getCurrentUser() {
        return customUserDetailsService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private UUID getCurrentUserUuid() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            User currentUser = (User) userDetails;
            return currentUser.getUuidUser();
        }
        throw new RuntimeException("User not authenticated");
    }

    private User convertToCustomUser(UserDetails userDetails) {
        String username = userDetails.getUsername();
        return userService.findByUsername(username);
    }


    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        userService.resetPassword(email, newPassword);
        return ResponseEntity.ok("Password reset successfully");
    }

}

