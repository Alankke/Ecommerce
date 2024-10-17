package com.Globant.E_commerce.Notification;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String destination, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("alaneduardokennedy@gmail.com");
        message.setTo(destination);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }
}