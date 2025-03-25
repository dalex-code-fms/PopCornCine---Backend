package fr.popcorncine.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendConfirmationEmail(String toEmail, String confirmationLink){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("popcorncine.email.test1@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Confirmation d'inscription");
        message.setText("Bonjour,\n\n" +
                "Merci de vous être inscrit sur notre site. " +
                "Veuillez confirmer votre inscription en cliquant sur le lien suivant :\n\n" +
                confirmationLink + "\n\n" +
                "Cordialement,\nL'équipe PopcornCine");

        javaMailSender.send(message);
    }

}
