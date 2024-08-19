package it.softwaredoctor.sivota.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.softwaredoctor.sivota.model.Votazione;
import it.softwaredoctor.sivota.service.VotazioneService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class VotazioneNotificationHandler implements RequestHandler<SNSEvent, String> {

    private final VotazioneService votazioneService;
    private final EmailSender emailSender;

    @Override
    public String handleRequest(SNSEvent event, Context context) {
        for (SNSEvent.SNSRecord record : event.getRecords()) {
            // Estrai il messaggio SNS
            String message = record.getSNS().getMessage();
            try {
                // Deserializza il messaggio JSON
                Votazione votazione = new ObjectMapper().readValue(message, Votazione.class);

                // Ottieni indirizzi email e invia notifiche
                List<String> emailAddresses = votazioneService.getEmailAddresses(votazione.getUuidVotazione());
                String emailMessage = buildEmailMessage(votazione);
                emailSender.sendEmails(emailAddresses, "Votazione Scaduta", emailMessage);
            } catch (Exception e) {
                context.getLogger().log("Failed to send emails: " + e.getMessage());
            }
        }
        return "Processed Successfully";
    }

    private String buildEmailMessage(Votazione votingEvent) {
        return String.format("Car %s,\n\nLa votazione per '%s' è scaduta. Grazie per la partecipazione.\n\nCordiali saluti,\nIl Team",
                votingEvent.getUser().getName(), votingEvent.getTitolo());
    }
}
