package it.softwaredoctor.sivota.controller;

import it.softwaredoctor.sivota.api.VotazioneApi;
import it.softwaredoctor.sivota.dto.RispostaDTOAggiornamento;
import it.softwaredoctor.sivota.model.Risposta;
import it.softwaredoctor.sivota.model.User;
import it.softwaredoctor.sivota.dto.VotazioneDTO;
import it.softwaredoctor.sivota.model.Votazione;
import it.softwaredoctor.sivota.service.*;
import jakarta.persistence.EntityNotFoundException;
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
@RequestMapping("/api/v1/votazione")
public class VotazioneController implements VotazioneApi {

    private final VotazioneService votazioneService;
    private final EmailService emailService;
    private final UserService userService;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;


    @PatchMapping("/conteggio")
    public ResponseEntity<VotazioneDTO> updateConteggio(@RequestParam("uuid") UUID uuidVotazione) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            VotazioneDTO votazione = votazioneService.getRisultatoNumerico(uuidVotazione);
            return new ResponseEntity<>(votazione, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PatchMapping("/view")
    public ResponseEntity<Void> updateAllRisposte(@RequestParam("uuidVotazione") UUID uuidVotazione, @RequestParam("token") String token,
                                                 @RequestBody List<RispostaDTOAggiornamento> aggiornamenti) {
        try {
            // Chiama il servizio per aggiornare tutte le risposte
            votazioneService.updateAllRisposte(uuidVotazione, aggiornamenti, token);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
//    public ResponseEntity<Void> updateRisposte(@RequestParam("uuidVotazione") UUID uuidVotazione, @RequestParam("token") String token, @RequestBody List<Risposta> risposte) {
//        boolean isValid = tokenService.validateToken(uuidVotazione, token);
//        if (isValid) {
//            votazioneService.updateRisposte(uuidVotazione, risposte);
//            return new ResponseEntity<>(HttpStatus.OK);
//        } else {
//            log.error("Error not valid token");
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
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
            return ResponseEntity.badRequest().build();
        }
    }


//    @PatchMapping("/{uuidVotazione}/{email}")
//    public ResponseEntity<Void> getRisultatoNumerico(@RequestParam UUID uuidVotazione, @RequestParam String email) {
//        votazioneService.getRisultatoNumerico(uuidVotazione, email);
//        return ResponseEntity.ok().build();
//    }

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
