package it.softwaredoctor.sivota.service;

import it.softwaredoctor.sivota.model.OnRegistrationCompleteEvent;
import it.softwaredoctor.sivota.model.User;
import it.softwaredoctor.sivota.model.Votazione;
import it.softwaredoctor.sivota.utility.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService implements ApplicationListener<OnRegistrationCompleteEvent> {

    private final JavaMailSender javaMailSender;
    private final TokenService tokenService;

    @Value("${spring.mail.username}")
//    private String sender = "andrea_italiano87@yahoo.com";
    private String sender;


    //    @PreAuthorize("isAuthenticated()")
//    public void sendEmail(List<String> recipient, UUID uuidVotazione) {
//        try {
//            log.info("Sending email to {} with subject {}", recipient, EmailDetails.SUBJECT);
////            SimpleMailMessage email = new SimpleMailMessage();
//
//            String emailBody = EmailDetails.generateBodyLinkWithUUID(uuidVotazione);
//
//            MimeMessage email = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(email, true);
//            helper.setTo(recipient.toArray(new String[0]));
//            helper.setFrom(sender);
//            helper.setSubject(EmailDetails.SUBJECT);
//            helper.setText(emailBody, true);
//
//            // Aggiunta di un allegato
////            FileSystemResource file = new FileSystemResource(new File("path/to/attachment.pdf"));
////            helper.addAttachment("Attachment.pdf", file);
//
//            javaMailSender.send(email);
//            log.info("Mail sent successfully");
//        } catch (MailException exception) {
//            log.debug("Failure occurred while sending email");
//        } catch (MessagingException e) {
//            throw new RuntimeException(e);
//        }
//    }


    public void sendEmail(List<String> recipientEmails, UUID uuidVotazione) {
        try {
            log.info("Sending email to {} with subject {}", recipientEmails, EmailDetails.SUBJECT);

            for (String email : recipientEmails) {
                String token = tokenService.generateToken(email, uuidVotazione);

                // Crea l'URL con i parametri uuidVotazione e token
                String url = String.format("http://localhost:8080/api/v1/votazione/view?uuidVotazione=%s&token=%s", uuidVotazione, token);

                String emailBody = EmailDetails.generateBodyLinkWithUUID(url);

                MimeMessage emailMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(emailMessage, true);
                helper.setTo(email);
                helper.setFrom(sender);
                helper.setSubject(EmailDetails.SUBJECT);
                helper.setText(emailBody, true);

                javaMailSender.send(emailMessage);

                log.info("Mail sent to {} successfully", email);
            }
        } catch (MailException exception) {
            log.error("Failure occurred while sending email", exception);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }


    public void sendEmailConfirm(String recipient, String confirmationUrl) throws MessagingException {

        String emailBody = EmailDetails.generateBodyEmailConfirm(confirmationUrl);

        MimeMessage email = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(email, true);
        helper.setTo(recipient);
        helper.setFrom(sender);
        helper.setSubject(EmailDetails.SUBJECTCONFIRM);
        helper.setText(emailBody, true);
        javaMailSender.send(email);
        log.info("Mail sent successfully");
    }

    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        User user = event.getUser();
        String recipientEmail = user.getEmail();
        String confirmationUrl = event.getConfirmationUrl();

        try {
            log.info("Sending confirmation email to {}", recipientEmail);
            sendEmailConfirm(recipientEmail, confirmationUrl);
        } catch (MessagingException e) {
            log.error("Failed to send confirmation email to {}", recipientEmail, e);
            throw new RuntimeException("Failed to send confirmation email to " + recipientEmail, e);
        }
    }




    @Override
    public boolean supportsAsyncExecution() {
        return ApplicationListener.super.supportsAsyncExecution();
    }
}
