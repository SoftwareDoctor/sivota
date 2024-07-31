package it.softwaredoctor.sivota.service;

import it.softwaredoctor.sivota.dto.DomandaDTO;
import it.softwaredoctor.sivota.dto.RispostaDTO;
import it.softwaredoctor.sivota.dto.RispostaDTOAggiornamento;
import it.softwaredoctor.sivota.dto.VotazioneDTO;
import it.softwaredoctor.sivota.mapper.DomandaMapper;
import it.softwaredoctor.sivota.mapper.VotazioneMapper;
import it.softwaredoctor.sivota.model.Domanda;
import it.softwaredoctor.sivota.model.Risposta;
import it.softwaredoctor.sivota.model.User;
import it.softwaredoctor.sivota.model.Votazione;
import it.softwaredoctor.sivota.repository.DomandaRepository;
import it.softwaredoctor.sivota.repository.RispostaRepository;
import it.softwaredoctor.sivota.repository.UserRepository;
import it.softwaredoctor.sivota.repository.VotazioneRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@RequiredArgsConstructor
@Service
public class VotazioneService {

    private final VotazioneRepository votazioneRepository;
    private final VotazioneMapper votazioneMapper;
    private final DomandaService domandaService;
    private final UserRepository userRepository;
    private final RispostaRepository rispostaRepository;
    private final UserService userService;
    private final RispostaService rispostaService;
    private final EmailService emailService;
    private final CustomUserDetailsService customUserDetailsService;
    private final TokenService tokenService;

    //    @PreAuthorize("isAuthenticated()")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @Transactional
//    public UUID createVotazione(VotazioneDTO votazioneDTO, UserDetails currentUser) {
//       User user = (User) currentUser;
////        User user = userRepository.findByUuidUser(uuidUser)
////                .orElseThrow(() -> new EntityNotFoundException("Utente con UUID " + uuidUser + " non trovato"));
//
//        Votazione votazione = votazioneMapper.votazioneDTOToVotazione(votazioneDTO);
//        votazione.setUser(user);
//        List<Domanda> domande = new ArrayList<>();
//        for (DomandaDTO domandaDTO : votazioneDTO.getDomande()) {
//            Domanda domanda = domandaService.createDomanda(domandaDTO);
//            domanda.setVotazione(votazione);
//            domande.add(domanda);
//
//            for (Risposta risposta : domanda.getRisposte()) {
//                if (risposta.getVotantiEmail() != null) {
//                    votazione.getVotantiEmail().addAll(risposta.getVotantiEmail());
//                }
//            }
//        }
//        votazione.setDomande(domande);
//
//        votazioneRepository.save(votazione);
//        return votazione.getUuidVotazione();
//    }


//    public UUID createVotazione(VotazioneDTO votazioneDTO, UserDetails currentUser) {
//        User user = (User) currentUser;
//
//        Votazione votazione = votazioneMapper.votazioneDTOToVotazione(votazioneDTO);
//        votazione.setUser(user);
//
//        List<Domanda> domande = new ArrayList<>();
//        for (DomandaDTO domandaDTO : votazioneDTO.getDomande()) {
//            Domanda domanda = domandaService.createDomanda(domandaDTO);
//
//
//            List<Risposta> risposte = new ArrayList<>();
//            for (RispostaDTO rispostaDTO : domandaDTO.getRisposte()) {
//                Risposta risposta = rispostaService.createRisposta(rispostaDTO);
//                risposta.setDomanda(domanda);
//                risposte.add(risposta);
//
//                if (rispostaDTO.getVotantiEmail() != null) {
//                    votazione.getVotantiEmail().addAll(rispostaDTO.getVotantiEmail());
//                }
//            }
//            domanda.setVotazione(votazione);
//            domanda.setRisposte(risposte);
//            domande.add(domanda);
//        }
//
//        votazione.setDomande(domande);
//
//        votazioneRepository.save(votazione);
//
//        return votazione.getUuidVotazione();
//    }






    //Il metodo createVotazione esegue i seguenti step:
    //1. Crea la votazione 2. Crea le domande 3. Crea le risposte 4. Salva tutto 5. Invio email alle email votantiEmail
    @Transactional
    public UUID createVotazione(VotazioneDTO votazioneDTO, UserDetails currentUser) {
        User user = userService.getUserFromUserDetails(currentUser);
        Votazione votazione = votazioneMapper.votazioneDTOToVotazione(votazioneDTO);
        votazione.setUser(user);
        List<Domanda> domande = new ArrayList<>();
        for (DomandaDTO domandaDTO : votazioneDTO.getDomande()) {
            Domanda domanda = domandaService.createDomanda(domandaDTO);
            domanda.setVotazione(votazione);
            domande.add(domanda);
        }
        votazione.setDomande(domande);
        votazioneRepository.save(votazione);
        emailService.sendEmail(votazioneDTO.getVotantiEmail(), votazione.getUuidVotazione());
        return votazione.getUuidVotazione();
    }




    // Metodo per l'aggiornamento della votazione da parte dell'utente creatore
    public void updateVotazioneByOwner(UUID uuidVotazione, VotazioneDTO votazioneDTO) {
        Optional<Votazione> votazioneOptional = votazioneRepository.findByUuidVotazione(uuidVotazione);
        if (votazioneOptional.isEmpty()) {
            throw new EntityNotFoundException("Votazione con ID " + uuidVotazione + " non trovata");
        }
        Votazione votazione = votazioneOptional.get();
        votazioneMapper.updateVotazioneFromDTO(votazioneDTO, votazione);
        votazioneRepository.save(votazione);
    }


//    // Metodo per ottenere l'indirizzo email del compilatore
//    private String getEmailCompilazione(VotazioneDTO votazioneDTO) {
//        return votazioneDTO.getEmailCompilazione();
//    }


//    public Votazione getVotazione(UUID uuidVotazione) {
//        return votazioneRepository.findByUuidVotazione(uuidVotazione)
//                .orElseThrow(() -> new EntityNotFoundException("Votazione with UUID " + uuidVotazione + " not found"));
//    }

    public VotazioneDTO getRisultatoNumerico(UUID uuidVotazione) {
        // Ottieni l'utente corrente e la votazione
        UUID uuidUser = userService.getCurrentUserUuid();
        Votazione votazione = userService.findVotazioneEntityByUuidUserUuidVotazione(uuidUser, uuidVotazione);

        // Ottieni tutte le domande nella votazione
        List<Domanda> domande = votazione.getDomande();

        // Itera su tutte le domande
        for (Domanda domanda : domande) {
            // Crea una mappa per tenere traccia dei conteggi delle risposte per ciascuna risposta
            Map<Risposta, Integer> conteggiRisposte = new HashMap<>();

            // Itera su tutte le risposte per ciascuna domanda
            for (Risposta risposta : domanda.getRisposte()) {
                // Inizializza il conteggio della risposta se non è già presente nella mappa
                conteggiRisposte.putIfAbsent(risposta, 0);

                // Incrementa il conteggio se la risposta è selezionata
                if (risposta.getIsSelected()) {
                    int nuovoConteggio = conteggiRisposte.get(risposta) + 1;
                    conteggiRisposte.put(risposta, nuovoConteggio);
                }
            }

            // Aggiorna il conteggio per ogni risposta
            for (Map.Entry<Risposta, Integer> entry : conteggiRisposte.entrySet()) {
                Risposta risposta = entry.getKey();
                Integer conteggioTotale = entry.getValue();
                risposta.setRisultatoNumerico(conteggioTotale);
            }
        }

        // Salva tutte le risposte aggiornate
        List<Risposta> risposteToUpdate = domande.stream()
                .flatMap(domanda -> domanda.getRisposte().stream())
                .collect(Collectors.toList());
        rispostaRepository.saveAll(risposteToUpdate);

        // Converti l'oggetto Votazione aggiornato in VotazioneDTO
        return votazioneMapper.votazioneToVotazioneDto(votazione);
    }



//    public void markedVotazioneAnonymousOrWithEmail(UUID uuidVotazione) {
//       Optional <Votazione> votazioneOptional = votazioneRepository.findByUuidVotazione(uuidVotazione);
//       Votazione votazione = votazioneOptional.get();
//       if(votazione.getIsAnonymous() == false) {
//           for (Risposta risposta : votazione.getDomande().get(0).getRisposte()) {
//               risposta.setVotantiEmail(votazione.getVotantiEmail());
//               rispostaRepository.save(risposta);
//           }
//       }
//        votazioneRepository.save(votazione);
//    }

//    public void markedVotazioneAnonymousOrWithEmail(UUID uuidVotazione, String emailCompilazione) {
//        Optional<Votazione> votazioneOptional = votazioneRepository.findByUuidVotazione(uuidVotazione);
//        if (votazioneOptional.isEmpty()) {
//
//            return;
//        }
//        Votazione votazione = votazioneOptional.get();
//
//        if (!Boolean.TRUE.equals(votazione.getIsAnonymous())) {
//            List<String> votantiEmail = votazione.getVotantiEmail();
//
//            for (Risposta risposta : votazione.getDomande().get(0).getRisposte()) {
//
//                if (votantiEmail.contains(emailCompilazione)) {
//                    List<String> rispostaVotantiEmail = risposta.getVotantiEmail();
//                    if (rispostaVotantiEmail == null) {
//                        rispostaVotantiEmail = new ArrayList<>();
//                    }
//                    rispostaVotantiEmail.add(emailCompilazione);
//                    risposta.setVotantiEmail(rispostaVotantiEmail);
//                    rispostaRepository.save(risposta);
//                }
//            }
//        }
//        votazioneRepository.save(votazione);
//    }


    public List<String> getVotantiEmailByVotazioneId(UUID votazioneId) {
        Votazione votazione = votazioneRepository.findByUuidVotazione(votazioneId)
                .orElseThrow(() -> new EntityNotFoundException("Votazione con ID " + votazioneId + " non trovata"));
        return votazione.getVotantiEmail();
    }


//    public void updateRisposte(UUID votazioneId, List<Risposta> risposteAggiornate) {
//        // Recupera la votazione tramite l'UUID
//        Optional<Votazione> votazioneOptional = votazioneRepository.findByUuidVotazione(votazioneId);
//        if (!votazioneOptional.isPresent()) {
//            throw new RuntimeException("Votazione non trovata con id: " + votazioneId);
//        }
//        Votazione votazione = votazioneOptional.get();
//        boolean updateMade = false;  // Flag per tracciare se è stata fatta un'aggiornamento
//
//        // Crea una lista di domande e risposte
//        List<Domanda> domande = votazione.getDomande();
//
//        // Per ogni risposta aggiornata
//        for (Risposta rispostaAggiornata : risposteAggiornate) {
//            // Ottieni l'ID della domanda associata alla risposta aggiornata
//            Long idDomandaAggiornata = rispostaAggiornata.getDomanda().getId();
//
//            // Trova la domanda corrispondente nella votazione
//            for (Domanda domanda : domande) {
//                if (domanda.getId().equals(idDomandaAggiornata)) {
//                    // Trova la risposta esistente per la stessa domanda e testo
//                    for (Risposta rispostaEsistente : domanda.getRisposte()) {
//                        if (rispostaEsistente.getTesto().equals(rispostaAggiornata.getTesto())) {
//                            // Aggiorna solo se i dati sono cambiati
//                            if (!rispostaEsistente.getIsSelected().equals(rispostaAggiornata.getIsSelected())) {
//                                rispostaEsistente.setDataRisposta(LocalDate.now());
//                                rispostaEsistente.setIsSelected(rispostaAggiornata.getIsSelected());
//                                updateMade = true;
//                            }
//                            break; // Uscire dal ciclo una volta trovata la risposta
//                        }
//                    }
//                    break; // Uscire dal ciclo una volta trovata la domanda
//                }
//            }
//        }
//
//        // Salva solo se ci sono stati aggiornamenti
//        if (updateMade) {
//            votazioneRepository.save(votazione);
//        }
//    }

    public void updateAllRisposte(UUID uuidVotazione, List<RispostaDTOAggiornamento> aggiornamenti, String token) {
        Optional<Votazione> votazioneOpt = votazioneRepository.findByUuidVotazione(uuidVotazione);
        if (votazioneOpt.isEmpty()) {
            throw new RuntimeException("Votazione con UUID " + uuidVotazione + " non trovata.");
        }

        Votazione votazione = votazioneOpt.get();

        for (RispostaDTOAggiornamento dto : aggiornamenti) {
            Optional<Risposta> rispostaOpt = Optional.ofNullable(rispostaRepository.findByUuidRisposta(dto.getUuidRisposta()));

            if (rispostaOpt.isPresent()) {
                Risposta risposta = rispostaOpt.get();
                risposta.setIsSelected(dto.getIsSelected());

                // Se la risposta è selezionata, aggiorna la data di risposta
                if (dto.getIsSelected() != null && dto.getIsSelected()) {
                    risposta.setDataRisposta(LocalDate.now());
                } else {
                    risposta.setDataRisposta(null);
                }

                // Aggiungi l'email del votante alla lista se la votazione non è anonima
                if (!votazione.getIsAnonymous() && Boolean.TRUE.equals(risposta.getIsSelected())) {
                    String email = tokenService.getEmailFromToken(token);
                    List<String> votantiEmail = risposta.getVotantiEmail();

                    if (votantiEmail == null) {
                        votantiEmail = new ArrayList<>();
                    }

                    if (!votantiEmail.contains(email)) {
                        votantiEmail.add(email);
                    }

                    risposta.setVotantiEmail(votantiEmail);
                }

                rispostaRepository.save(risposta);
            }
        }
    }
}

