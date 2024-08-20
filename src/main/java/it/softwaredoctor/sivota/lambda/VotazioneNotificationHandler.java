package it.softwaredoctor.sivota.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.softwaredoctor.sivota.model.Votazione;
import it.softwaredoctor.sivota.service.VotazioneService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VotazioneNotificationHandler implements RequestHandler<String, String> {

    private final VotazioneService votazioneService;
    private final EmailSender emailSender;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String handleRequest(String input, Context context) {
        try {
            // Deserializza l'input JSON in un oggetto Votazione
            Votazione votazione = objectMapper.readValue(input, Votazione.class);

            // Verifica se la votazione è scaduta
            if (isVotazioneExpired(votazione.getDataCreazione())) {
                List<String> emailAddresses = votazioneService.getEmailAddresses(votazione.getUuidVotazione());
                String emailMessage = buildEmailMessage(votazione);
                emailSender.sendEmails(emailAddresses, "Votazione Scaduta", emailMessage);
            }

        } catch (Exception e) {
            context.getLogger().log("Failed to process: " + e.getMessage());
            context.getLogger().log("Stack trace: " + getStackTraceAsString(e));
            return "Processing Failed";
        }
        return "Processed Successfully";
    }

    private boolean isVotazioneExpired(LocalDate dataCreazione) {
        LocalDate tenDaysAgo = LocalDate.now().minusDays(10);
        // Ritorna true se la data di creazione è precedente ai 10 giorni fa
        return dataCreazione.isBefore(tenDaysAgo);
    }

    private String buildEmailMessage(Votazione votazione) {
        return String.format("Caro %s,\n\nLa votazione per '%s' è scaduta. Se non hai ancora risposto non potrai più partecipare, altrimenti ignora questa email.\n\nCordiali saluti,\nIl Team",
                votazione.getUser().getName(), votazione.getTitolo());
    }

    private String getStackTraceAsString(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
