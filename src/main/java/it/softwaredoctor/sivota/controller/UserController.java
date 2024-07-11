package it.softwaredoctor.sivota.controller;

import it.softwaredoctor.sivota.dto.UserDTO;
import it.softwaredoctor.sivota.dto.UserLoginDTO;
import it.softwaredoctor.sivota.dto.VotazioneDTO;
import it.softwaredoctor.sivota.model.User;
import it.softwaredoctor.sivota.model.Votazione;
import it.softwaredoctor.sivota.service.CustomUserDetailsService;
import it.softwaredoctor.sivota.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.AuthenticationException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
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
//    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody UserLoginDTO loginUser) {
        boolean loggedIn = userService.login(loginUser);

        if (loggedIn) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.info("User {} logged in successfully.", loginUser.getUsername());
            return ResponseEntity.ok().build();
        } else {
            log.warn("Failed login attempt for user {} due to incorrect credentials or disabled account.", loginUser.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

    }

//    @PatchMapping("/token")
//    public ResponseEntity<Void> confirmationToken(@RequestParam String token) {
//        if(Boolean.FALSE.equals(token)) {
//            log.warn("Failed token confirmation for token {}.", token);
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
//        } else {
//            log.info("Token {} confirmed successfully.", token);
//            return ResponseEntity.ok().build();
//        }

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



//    @PostMapping("/")
//    public ResponseEntity<Void> createUser(@RequestBody UserDTO userDTO) {
//        try {
//           userService.createUser(userDTO);
//            URI location = ServletUriComponentsBuilder
//                    .fromCurrentRequest()
//                    .path("{id}")
//                    .buildAndExpand(userDTO.getUuidUser())
//                    .toUri();
////            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////            System.out.print("********AUTH: " + auth);
//            return ResponseEntity.created(location).build();
//        } catch (DuplicateKeyException e) {
//            log.error("Username or Email already exists");
//            return ResponseEntity.status(HttpStatus.CONFLICT).build();
//        }
//    }

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
        UserDetails userDetails = getCurrentUser();
        User currentUser = convertToCustomUser(userDetails);
        UUID uuidUser = currentUser.getUuidUser();
        userService.deleteAllVotazioniByUser(uuidUser);
        return ResponseEntity.noContent().build();
    }
    @DeleteMapping("/{uuidVotazione}")
    public ResponseEntity<Void> deleteVotazioniByUserAndUuidVotazione(@PathVariable UUID uuidVotazione) {
        UserDetails userDetails = getCurrentUser();
        User currentUser = (User) userDetails;
        UUID uuidUser = currentUser.getUuidUser();
        userService.deleteVotazioniByUserAndUuidVotazione(uuidUser, uuidVotazione);
        return ResponseEntity.noContent().build();
    }


//    @GetMapping("/all/{uuidUser}")
//    public ResponseEntity<List<Votazione>> findAllByUuidUser(@PathVariable UUID uuidUser) {
//        List<Votazione> votazioni = userService.findAllByUuidUser(uuidUser);
//         return ResponseEntity.ok().body(votazioni);
//    }

    @GetMapping("/all/")
    public ResponseEntity<List<VotazioneDTO>> findAllByUuidUser() {
        UserDetails userDetails = getCurrentUser();
        User user = userService.getUserFromUserDetails(userDetails);
        UUID uuidUser = user.getUuidUser();
        List<VotazioneDTO> votazioniDTO = userService.findAllByUuidUser(uuidUser);
        return ResponseEntity.ok().body(votazioniDTO);
    }

    @GetMapping("/{uuidVotazione}")
    public ResponseEntity<VotazioneDTO> findVotazioneByUuidUser(@PathVariable UUID uuidVotazione) {
        UserDetails userDetails = getCurrentUser();
        User currentUser = (User) userDetails;
        UUID uuidUser = currentUser.getUuidUser();
        VotazioneDTO votazioneDTO = userService.findVotazioneByUuidUserUuidVotazione(uuidUser, uuidVotazione);
        return ResponseEntity.ok().body(votazioneDTO);
    }

    @PostMapping("/reset")
    public ResponseEntity<String> resetPassword(@RequestParam String email, @RequestParam String newPassword) {
        userService.resetPassword(email, newPassword);
        return ResponseEntity.ok("Password reset successfully");
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


}

