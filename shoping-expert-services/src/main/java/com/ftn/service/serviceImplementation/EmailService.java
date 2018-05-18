package com.ftn.service.serviceImplementation;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Created by milca on 5/15/2018.
 */
@Service
public class EmailService {
    private JavaMailSender javaMailSender = new JavaMailSenderImpl();

    public EmailService() {
        ((JavaMailSenderImpl) javaMailSender).setHost("smtp.gmail.com");
        ((JavaMailSenderImpl) javaMailSender).setPort(587);

        ((JavaMailSenderImpl) javaMailSender).setUsername("mgtest94@gmail.com");
        ((JavaMailSenderImpl) javaMailSender).setPassword("49tsetgm");

        Properties props = ((JavaMailSenderImpl) javaMailSender).getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
    }

    public void sendEmail(String subject, String message, String email) {
        SimpleMailMessage msg = new SimpleMailMessage();

        msg.setTo(email);
        msg.setSubject(subject);
        msg.setText(message);

        try {
            javaMailSender.send(msg);

            System.out.println("======Service email sent");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("======Service email something went wrong");

        }
    }
}
