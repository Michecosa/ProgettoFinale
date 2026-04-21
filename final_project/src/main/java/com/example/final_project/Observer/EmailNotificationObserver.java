package com.example.final_project.Observer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import com.example.final_project.Model.Ordine;

/**
 * Implementazione concreta dell'Observer per l'invio di notifiche via email.
 */
@Component
public class EmailNotificationObserver implements OrderObserver {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Override
    public void update(Ordine ordine) {
        if (mailSender == null) {
            System.out.println("JavaMailSender non configurato. Notifica simulata per l'ordine #" + ordine.getId());
            return;
        }

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(ordine.getUtente().getMail());
            message.setSubject("Conferma Ordine #" + ordine.getId());
            message.setText("Gentile " + ordine.getUtente().getUsername() + ",\n\n" +
                    "Il tuo ordine è stato ricevuto con successo!\n" +
                    "Indirizzo di consegna: " + ordine.getIndirizzo() + "\n" +
                    "Data prevista: " + ordine.getConsegna() + "\n" +
                    "Totale ordine: €" + String.format("%.2f", ordine.getTotale()) + "\n\n" +
                    "Grazie per averci scelto!");
            
            mailSender.send(message);
            System.out.println("Email inviata con successo a: " + ordine.getUtente().getMail());
        } catch (Exception e) {
            System.err.println("Errore durante l'invio dell'email: " + e.getMessage());
        }
    }
}
