package it.softwaredoctor.sivota.controller;

import it.softwaredoctor.sivota.api.VotazioneApi;
import it.softwaredoctor.sivota.model.User;
import it.softwaredoctor.sivota.dto.VotazioneDTO;
//import it.softwaredoctor.sivota.service.CustomUserDetailsService;
import it.softwaredoctor.sivota.service.CustomUserDetailsService;
import it.softwaredoctor.sivota.service.EmailService;
import it.softwaredoctor.sivota.service.UserService;
import it.softwaredoctor.sivota.service.VotazioneService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/votazione")
public class VotazioneController implements VotazioneApi {

    private final VotazioneService votazioneService;
    private final EmailService emailService;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;


//    private UserDetails getCurrentUser() {
//        return customUserDetailsService
//                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
//    }


//    private UserDetails getCurrentUser() {
//        return userService
//                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
//    }


//    @PostMapping("/new/{uuidUser}")
//    public ResponseEntity<Void> createVotazione(@RequestBody VotazioneDTO votazioneDTO, @PathVariable UUID uuidUser) {
////    public ResponseEntity<Void> createVotazione(@RequestBody VotazioneDTO votazioneDTO, @PathVariable UUID uuidUser, @RequestHeader("Authorization") String authorizationHeader) {
//        try {
////            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
////            UserDetails currentUser = getCurrentUser();
////            if (votazioneDTO != null || currentUser != null) {
//                votazioneService.createVotazione(votazioneDTO, uuidUser);
//                URI location = ServletUriComponentsBuilder
//                        .fromCurrentRequest()
//                        .path("{/uuidVotazione}")
//                        .buildAndExpand(votazioneDTO.getUuidVotazione())
//                        .toUri();
//                return ResponseEntity.created(location).build();
////            }
//        } catch (EntityNotFoundException e) {
//            log.error("Utente con UUID {} non trovato", uuidUser);
//            return ResponseEntity.badRequest().build();
//        }
////        return null;
//    }

    @PostMapping("/{uuidVotazione}")
    public ResponseEntity<Void> updateVotazione(@PathVariable UUID uuidVotazione, @RequestBody VotazioneDTO votazioneDTO) {
        votazioneService.updateVotazioneByOwner(uuidVotazione, votazioneDTO);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{uuidVotazione}")
                .buildAndExpand(uuidVotazione)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping("/new")
    public ResponseEntity<Void> createVotazione(@RequestBody VotazioneDTO votazioneDTO) {
        try {
            UserDetails currentUser = getCurrentUser();
//            UserDetails userDetails = getCurrentUser();
//            User currentUser = convertToCustomUser(userDetails);
            if (votazioneDTO != null && currentUser != null) {
//                UUID uuidUser = ((User) currentUser).getUuidUser();
                UUID uuidVotazione = votazioneService.createVotazione(votazioneDTO, currentUser);
                URI location = ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/{uuidVotazione}")
                        .buildAndExpand(uuidVotazione)
                        .toUri();
                return ResponseEntity.created(location).build();
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (EntityNotFoundException e) {
            log.error("Utente non trovato", e);
            log.info(VotazioneController.class.getName());
            return ResponseEntity.badRequest().build();
        }
    }


    @PatchMapping("/{uuidVotazione}")
    public ResponseEntity<Void> getRisultatoNumerico(@RequestParam UUID uuidVotazione, @RequestParam String email) {
        UserDetails userDetails = getCurrentUser();
        User currentUser = (User) userDetails;
        UUID uuidUser = currentUser.getUuidUser();
        votazioneService.getRisultatoNumerico(uuidUser, uuidVotazione, email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/email/{uuidVotazione}")
    public ResponseEntity<Void> sendEmailVotazione(@PathVariable UUID uuidVotazione) {
        List<String> recipients = votazioneService.getVotantiEmailByVotazioneId(uuidVotazione);
        if (votazioneService.getVotantiEmailByVotazioneId(uuidVotazione).isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        emailService.sendEmail(recipients, uuidVotazione);
        return ResponseEntity.ok().build();
    }

//    private UserDetails getCurrentUser() {
//        String username = SecurityContextHolder.getContext().getAuthentication().getName();
//        log.info("Autenticazione utente: " + username);
//        return customUserDetailsService.loadUserByUsername(username);
//    }

    private UserDetails getCurrentUser() {
        return customUserDetailsService
                .loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    private User convertToCustomUser(UserDetails userDetails) {

        String username = userDetails.getUsername();

        return userService.findByUsername(username);
    }

}
