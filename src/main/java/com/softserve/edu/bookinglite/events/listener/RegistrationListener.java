package com.softserve.edu.bookinglite.events.listener;

import com.softserve.edu.bookinglite.entity.User;
import com.softserve.edu.bookinglite.events.RegistrationCompleteEvent;
import com.softserve.edu.bookinglite.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<RegistrationCompleteEvent> {



    private final JavaMailSender mailSender;

    private final UserService userService;

    @Autowired
    RegistrationListener(JavaMailSender mailSender, UserService userService){
        this.mailSender = mailSender;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        this.sendVerificationToken(event);
    }
    private void sendVerificationToken(RegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.createVerificationToken(user, token);

        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getAppUrl() + "/api/registrationconfirm?token=" + token;
        String message = "For Verification go to " + confirmationUrl;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }
}
