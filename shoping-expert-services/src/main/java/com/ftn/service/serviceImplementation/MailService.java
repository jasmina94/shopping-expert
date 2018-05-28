package com.ftn.service.serviceImplementation;

import com.ftn.service.IMailService;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Created by Jasmina on 22/05/2018.
 */
@Service
public class MailService implements IMailService {

    private final String username = "zijdev@gmail.com";
    private final String password = "mojalozinka12";

    @Override
    public boolean sendForgottenPassword(String emailTo, String passwordToSent, String firstname, String lastname) {
        String messageTo = emailTo, messageSubject = "Shopping expert",
                messageText = "Hi " + firstname + " " + lastname +"," +
                "\t\n\t\nWe have received your request for password recovery." +
                "\t\nYour password is: " + passwordToSent + " so you can sign in with it."+
                "\t\n\t\nBest Regards,\t\nYour Shopping expert team.";

        Properties props = new Properties();
        props.put("mail.smtp.user", username);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "25");
        props.put("mail.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.EnableSSL.enable", "true");
        props.setProperty("mail.smtp.port", "587");
        props.setProperty("mail.smtp.socketFactory.port", "587");


        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        MimeMessage msg = new MimeMessage(session);
        try {
            msg.setSubject(messageSubject);
            msg.setText(messageText);
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailTo));

            Transport.send(msg);

            return  true;
        } catch (AddressException e) {
            e.printStackTrace();
            return false;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
