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

        message.setFrom("vlad.slobodeaniuc00@e-uvt.ro");
        message.setTo(toEmail);
        message.setSubject("Super Confirmation");

        message.setText("Salutare, " + firstName + "!\n\n" +
                "Ty bro. " +
                "Contul tău a fost creat cu succes și este gata de utilizare!\n\n" +
                "Bon samedi\nCalule");

        mailSender.send(message);
    }
}
