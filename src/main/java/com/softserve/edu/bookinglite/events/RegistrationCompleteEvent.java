package com.softserve.edu.bookinglite.events;

import com.softserve.edu.bookinglite.entity.User;
import org.springframework.context.ApplicationEvent;

public class RegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private User user;

    public RegistrationCompleteEvent(User user, String appUrl) {
        super(user);
        this.user = user;
        this.appUrl = appUrl;
    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
