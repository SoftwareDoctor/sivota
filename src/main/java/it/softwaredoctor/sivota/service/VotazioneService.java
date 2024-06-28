package it.softwaredoctor.sivota.service;

import it.softwaredoctor.sivota.dto.DomandaDTO;
import it.softwaredoctor.sivota.dto.RispostaDTO;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;


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
    public UUID createVotazione(VotazioneDTO votazioneDTO, UserDetails currentUser) {
        User user = (User) currentUser;

        Votazione votazione = votazioneMapper.votazioneDTOToVotazione(votazioneDTO);
        votazione.setUser(user);

        List<Domanda> domande = new ArrayList<>();
        for (DomandaDTO domandaDTO : votazioneDTO.getDomande()) {
            Domanda domanda = domandaService.createDomanda(domandaDTO);

            List<Risposta> risposte = new ArrayList<>();
            for (RispostaDTO rispostaDTO : domandaDTO.getRisposte()) {
                Risposta risposta = rispostaService.createRisposta(rispostaDTO);
                risposta.setDomanda(domanda);
                risposte.add(risposta);
                // Salvare l'indirizzo email in oggetto risposta solo se la votazione non è anonima e se l utente ha selezionato quella risposta
//                if (!Boolean.TRUE.equals(votazione.getIsAnonymous()) && risposta.getIsSelected()) {
//                        List<String> votantiEmail = risposta.getVotantiEmail();
//                        if (votantiEmail == null) {
//                            votantiEmail = new ArrayList<>();
//                        }
//                        risposta.setVotantiEmail(votantiEmail);
//                    }
                }
            domanda.setVotazione(votazione);
            domanda.setRisposte(risposte);
            domande.add(domanda);
        }
        votazione.setDomande(domande);
        votazioneRepository.save(votazione);

        // Creata la votazione inviare l'email
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
//
//        return votazioneDTO.getEmailCompilazione();
//    }






//    public Votazione getVotazione(UUID uuidVotazione) {
//        return votazioneRepository.findByUuidVotazione(uuidVotazione)
//                .orElseThrow(() -> new EntityNotFoundException("Votazione with UUID " + uuidVotazione + " not found"));
//    }


    //    @PreAuthorize("isAuthenticated()")
    public void getRisultatoNumerico(UUID uuidUser, UUID uuidVotazione, String votanteEmail) {
        Votazione votazione = userService.findVotazioneEntityByUuidUserUuidVotazione(uuidUser, uuidVotazione);
        List<Domanda> domande = votazione.getDomande();

        for (Domanda domanda : domande) {
            int risultatoNumericoTotale = 0;

            for (Risposta risposta : domanda.getRisposte()) {
                if (risposta.getIsSelected()) {
                    risultatoNumericoTotale += risposta.getRisultatoNumerico();
                } else {
                    risultatoNumericoTotale = risposta.getRisultatoNumerico();
                }
            }

            for (Risposta risposta : domanda.getRisposte()) {
                risposta.setRisultatoNumerico(risultatoNumericoTotale);
//                rispostaRepository.save(risposta);

                if(votazione.getIsAnonymous() == false) {
                    List<String> votantiEmail = risposta.getVotantiEmail();
                    if (risposta.getVotantiEmail().contains(votanteEmail)) {
                        votantiEmail.add(votanteEmail);
                        risposta.setVotantiEmail(votantiEmail);
                    }
                }

            }
        }
        List<Risposta> risposteToUpdate = domande.stream()
                .flatMap(domanda -> domanda.getRisposte().stream())
                .collect(Collectors.toList());
        rispostaRepository.saveAll(risposteToUpdate);
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


}
