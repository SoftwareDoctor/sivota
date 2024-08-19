package it.softwaredoctor.sivota.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import it.softwaredoctor.sivota.model.Votazione;
import it.softwaredoctor.sivota.service.VotazioneService;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import java.util.List;


@RequiredArgsConstructor
public class VotazioneNotificationHandler implements RequestHandler<Object, String> {

    private final VotazioneService votazioneService;
    private final EmailSender emailSender;

    @Override
    public String handleRequest(Object input, Context context) {
        try {
            // Ottieni tutte le votazioni
            List<Votazione> votazioni = votazioneService.getAllVotazioni();

            // Per ogni votazione, verifica se è scaduta
            for (Votazione votazione : votazioni) {
                if (isVotazioneExpired(votazione.getDataCreazione())) {
                    List<String> emailAddresses = votazioneService.getEmailAddresses(votazione.getUuidVotazione());
                    String emailMessage = buildEmailMessage(votazione);
                    emailSender.sendEmails(emailAddresses, "Votazione Scaduta", emailMessage);
                }
            }

        } catch (Exception e) {
            context.getLogger().log("Failed to process: " + e.getMessage());
            context.getLogger().log("Stack trace: " + getStackTraceAsString(e));
        }
        return "Processed Successfully";
    }

    private boolean isVotazioneExpired(LocalDate dataCreazione) {
        LocalDate tenDaysAgo = LocalDate.now().minusDays(10);
        // Ritorna true se la data di creazione è precedente ai 10 giorni fa
        return dataCreazione.isBefore(tenDaysAgo);
    }

    private String buildEmailMessage(Votazione votingEvent) {
        return String.format("Caro %s,\n\nLa votazione per '%s' è scaduta. Se non hai ancora risposto non potrai più partecipare, altrimenti ignora questa email.\n\nCordiali saluti,\nIl Team",
                votingEvent.getUser().getName(), votingEvent.getTitolo());
    }

    private String getStackTraceAsString(Exception e) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        return sb.toString();
    }
}
