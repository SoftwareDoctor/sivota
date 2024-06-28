//package it.softwaredoctor.sivota.config;
//
//import it.softwaredoctor.sivota.model.OnRegistrationCompleteEvent;
//import it.softwaredoctor.sivota.service.EmailService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
//@RequiredArgsConstructor
//@Component
//public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
//
//    private final EmailService emailService;
//
//    @Override
//    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
//        this.confirmRegistration(event);
//    }
//
//    private void confirmRegistration(OnRegistrationCompleteEvent event) {
//        User user = event.getUser();
//        String token = UUID.randomUUID().toString();
//        tokenService.createVerificationToken(user, token);
//
//        String recipientAddress = user.getEmail();
//        String subject = "Email Verification";
//        String confirmationUrl = "http://localhost:8080/verify-email?token=" + token;
//        String message = "Click the link to verify your email: " + confirmationUrl;
//
//        emailService.sendEmail(recipientAddress, subject, message);
//    }
//
//    @Override
//    public boolean supportsAsyncExecution() {
//        return ApplicationListener.super.supportsAsyncExecution();
//    }
//}
