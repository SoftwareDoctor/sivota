package it.softwaredoctor.sivota.model;

import it.softwaredoctor.sivota.model.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private final String confirmationUrl;

    public OnRegistrationCompleteEvent(User user, String confirmationUrl) {
        super(user);
        this.confirmationUrl = confirmationUrl;
    }

    public User getUser() {
        return (User) getSource();
    }

    public String getConfirmationUrl() {
        return confirmationUrl;
    }
}
