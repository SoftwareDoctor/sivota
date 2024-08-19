package it.softwaredoctor.sivota.lambda;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import java.util.List;

public class EmailSender {

    private final AmazonSimpleEmailService sesClient = AmazonSimpleEmailServiceClientBuilder.defaultClient();

    public void sendEmails(List<String> emailAddresses, String subject, String body) {
        for (String email : emailAddresses) {
            SendEmailRequest request = new SendEmailRequest()
                    .withDestination(new com.amazonaws.services.simpleemail.model.Destination().withToAddresses(email))
                    .withMessage(new com.amazonaws.services.simpleemail.model.Message()
                            .withBody(new com.amazonaws.services.simpleemail.model.Body()
                                    .withText(new com.amazonaws.services.simpleemail.model.Content()
                                            .withCharset("UTF-8").withData(body)))
                            .withSubject(new com.amazonaws.services.simpleemail.model.Content()
                                    .withCharset("UTF-8").withData(subject)))
                    .withSource("your-email@example.com");
            SendEmailResult result = sesClient.sendEmail(request);
            System.out.println("Email sent to " + email + " with message ID: " + result.getMessageId());
        }
    }
}

