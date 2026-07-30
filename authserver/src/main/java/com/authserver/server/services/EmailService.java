package com.authserver.server.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Async
    public void sendConfirmationEmail(String toEmail, String firstName) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("test@mail.com");
        message.setTo(toEmail);
        message.setSubject("Confirmation");

        message.setText("Hello, " + firstName + "!\n\n" +
                "Your accout was created!");

        mailSender.send(message);
    }
}
