package it.softwaredoctor.sivota.service;

import com.amazonaws.services.eventbridge.AmazonEventBridge;
import com.amazonaws.services.eventbridge.AmazonEventBridgeClientBuilder;
import com.amazonaws.services.eventbridge.model.PutEventsRequest;
import com.amazonaws.services.eventbridge.model.PutEventsRequestEntry;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.softwaredoctor.sivota.dto.DomandaDTO;
import it.softwaredoctor.sivota.dto.RispostaDTOAggiornamento;
import it.softwaredoctor.sivota.dto.VotazioneDTO;
import it.softwaredoctor.sivota.mapper.VotazioneMapper;
import it.softwaredoctor.sivota.model.*;
import it.softwaredoctor.sivota.repository.RispostaRepository;
import it.softwaredoctor.sivota.repository.RispostaVotanteRepository;
import it.softwaredoctor.sivota.repository.UserRepository;
import it.softwaredoctor.sivota.repository.VotazioneRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class VotazioneService {

    private final VotazioneRepository votazioneRepository;
    private final VotazioneMapper votazioneMapper;
    private final DomandaService domandaService;
    private final RispostaRepository rispostaRepository;
    private final UserService userService;
    private final EmailService emailService;
    private final TokenService tokenService;
    private final RispostaVotanteRepository rispostaVotanteRepository;

    private final AmazonEventBridge eventBridge = AmazonEventBridgeClientBuilder.defaultClient();

    public List<Votazione> getAllVotazioni() {
        return votazioneRepository.findAll();
    }

    public List<String> getEmailAddresses(UUID uuidVotazione) {
        Votazione votazione = votazioneRepository.findByUuidVotazione(uuidVotazione)
                .orElseThrow(() -> new NoSuchElementException("Votazione non trovata per l'UUID: " + uuidVotazione));
        return votazione.getVotantiEmail().stream()
                .map(String::trim)
                .filter(email -> !email.isEmpty())
                .distinct()
                .collect(Collectors.toList());
    }

    @Transactional
    public UUID createVotazione(VotazioneDTO votazioneDTO, UserDetails currentUser) throws JsonProcessingException {
        User user = userService.getUserFromUserDetails(currentUser);
        Votazione votazione = votazioneMapper.votazioneDTOToVotazione(votazioneDTO);

        LocalDateTime oraAttuale = LocalDateTime.now();
        LocalDateTime scadenza = oraAttuale.plusMinutes(votazioneDTO.getScadenza().getMinute());
        votazione.setScadenza(scadenza);
        votazione.setUser(user);
        List<Domanda> domande = new ArrayList<>();
        for (DomandaDTO domandaDTO : votazioneDTO.getDomande()) {
            Domanda domanda = domandaService.createDomanda(domandaDTO);
            domanda.setVotazione(votazione);
            domande.add(domanda);
        }
        votazione.setDomande(domande);
        votazioneRepository.save(votazione);
        getEmailAddresses(votazione.getUuidVotazione());

        // Pubblica evento su EventBridge
        String message = new ObjectMapper().writeValueAsString(votazione);
        PutEventsRequestEntry entry = new PutEventsRequestEntry()
                .withSource("my.application.votazione")
                .withDetail(message)
                .withDetailType("VotazioneCreated")
                .withResources("arn:aws:events:region:account-id:event-bus/default");

        PutEventsRequest request = new PutEventsRequest().withEntries(entry);
        eventBridge.putEvents(request);

        emailService.sendEmail(votazioneDTO.getVotantiEmail(), votazione.getUuidVotazione());
        return votazione.getUuidVotazione();
    }

    public void updateVotazioneByOwner(UUID uuidVotazione, VotazioneDTO votazioneDTO) {
        Optional<Votazione> votazioneOptional = votazioneRepository.findByUuidVotazione(uuidVotazione);
        if (votazioneOptional.isEmpty()) {
            throw new EntityNotFoundException("Votazione con ID " + uuidVotazione + " non trovata");
        }
        Votazione votazione = votazioneOptional.get();
        votazioneMapper.updateVotazioneFromDTO(votazioneDTO, votazione);
        votazioneRepository.save(votazione);
    }

    public VotazioneDTO getRisultatoNumerico(UUID uuidVotazione) {
        UUID uuidUser = userService.getCurrentUserUuid();
        Votazione votazione = userService.findVotazioneEntityByUuidUserUuidVotazione(uuidUser, uuidVotazione);
        List<Domanda> domande = votazione.getDomande();
        for (Domanda domanda : domande) {
            Map<Risposta, Integer> conteggiRisposte = new HashMap<>();
            for (Risposta risposta : domanda.getRisposte()) {
                conteggiRisposte.putIfAbsent(risposta, 0);
                if (risposta.getIsSelected()) {
                    int nuovoConteggio = conteggiRisposte.get(risposta) + 1;
                    conteggiRisposte.put(risposta, nuovoConteggio);
                }
            }
            for (Map.Entry<Risposta, Integer> entry : conteggiRisposte.entrySet()) {
                Risposta risposta = entry.getKey();
                Integer conteggioTotale = entry.getValue();
                risposta.setRisultatoNumerico(conteggioTotale);
            }
        }
        List<Risposta> risposteToUpdate = domande.stream()
                .flatMap(domanda -> domanda.getRisposte().stream())
                .collect(Collectors.toList());
        rispostaRepository.saveAll(risposteToUpdate);
        return votazioneMapper.votazioneToVotazioneDto(votazione);
    }

    public List<String> getVotantiEmailByVotazioneId(UUID votazioneId) {
        Votazione votazione = votazioneRepository.findByUuidVotazione(votazioneId)
                .orElseThrow(() -> new EntityNotFoundException("Votazione con ID " + votazioneId + " non trovata"));
        return votazione.getVotantiEmail();
    }

    public void updateAllRisposte(UUID uuidVotazione, List<RispostaDTOAggiornamento> aggiornamenti, String token) {
        Optional<Votazione> votazioneOpt = votazioneRepository.findByUuidVotazione(uuidVotazione);
        if (votazioneOpt.isEmpty()) {
            throw new RuntimeException("Votazione con UUID " + uuidVotazione + " non trovata.");
        }
        Votazione votazione = votazioneOpt.get();
        if (LocalDateTime.now().isAfter(votazione.getScadenza())) {
            throw new RuntimeException("La votazione è scaduta.");
        }
        String email = tokenService.getEmailFromToken(token);
        for (RispostaDTOAggiornamento dto : aggiornamenti) {
            Optional<Risposta> rispostaOpt = Optional.ofNullable(rispostaRepository.findByUuidRisposta(dto.getUuidRisposta()));
            if (rispostaOpt.isPresent()) {
                Risposta risposta = rispostaOpt.get();
                risposta.setIsSelected(dto.getIsSelected());
                if (!votazione.getIsAnonymous() && Boolean.TRUE.equals(risposta.getIsSelected())) {
                    List<RispostaVotante> votanti = risposta.getVotanti();
                    if (votanti == null) {
                        votanti = new ArrayList<>();
                        risposta.setVotanti(votanti);
                    }
                    RispostaVotante nuovoVotante = new RispostaVotante();
                    nuovoVotante.setRisposta(risposta);
                    nuovoVotante.setEmail(email);
                    votanti.add(nuovoVotante);
                    rispostaVotanteRepository.save(nuovoVotante);
                }
                rispostaRepository.save(risposta);
            }
        }
    }
}
