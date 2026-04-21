package com.example.final_project.Observer;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.example.final_project.Model.Ordine;
import com.example.final_project.Model.Utente;

@Component
public class EmailNotificationObserver implements OrderObserver, UserObserver, RegistrationObserver {

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Override
    public void update(Utente utente) {
        if (mailSender == null) {
            System.out.println("JavaMailSender non configurato. Notifica cambio password simulata per: " + utente.getUsername());
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(utente.getMail());
            helper.setSubject("Sicurezza Account: Password Modificata");
            
            String content = "<p>Gentile <strong>" + utente.getUsername() + "</strong>,</p>" +
                             "<p>Ti confermiamo che la password del tuo account &egrave; stata modificata con successo.</p>" +
                             "<p>Se non hai richiesto tu questa modifica, ti preghiamo di contattare immediatamente il supporto.</p>";
            
            helper.setText(getHtmlTemplate("Password Aggiornata", content), true);

            mailSender.send(message);
            System.out.println("Email di cambio password inviata a: " + utente.getMail());
        } catch (Exception e) {
            System.err.println("Errore durante l'invio dell'email: " + e.getMessage());
        }
    }

    @Override
    public void onRegistrazione(Utente utente) {
        if (mailSender == null) {
            System.out.println("JavaMailSender non configurato. Benvenuto simulato per: " + utente.getUsername());
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(utente.getMail());
            helper.setSubject("Benvenuto su CodeShop!");
            
            String content = "<p>Gentile <strong>" + utente.getUsername() + "</strong>,</p>" +
                             "<p>La registrazione su <strong>CodeShop</strong> &egrave; avvenuta con successo!</p>" +
                             "<p>Esplora la nostra collezione di snippet, progetti completi e script database creati da sviluppatori per sviluppatori.</p>" +
                             "<div style='text-align: center;'><a href='#' class='button'>Inizia lo Shopping</a></div>";
            
            helper.setText(getHtmlTemplate("Benvenuto a Bordo!", content), true);

            mailSender.send(message);
            System.out.println("Email di benvenuto inviata a: " + utente.getMail());
        } catch (Exception e) {
            System.err.println("Errore durante l'invio dell'email di benvenuto: " + e.getMessage());
        }
    }

    @Override
    public void update(Ordine ordine) {
        if (mailSender == null) {
            System.out.println("JavaMailSender non configurato. Notifica simulata per l'ordine #" + ordine.getId());
            return;
        }

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            
            helper.setTo(ordine.getUtente().getMail());
            helper.setSubject("Conferma Ordine #" + ordine.getId() + " - CodeShop");
            
            String content = "<p>Gentile <strong>" + ordine.getUtente().getUsername() + "</strong>,</p>" +
                             "<p>Il tuo ordine &egrave; stato ricevuto con successo!</p>" +
                             "<div style='background: rgba(255,255,255,0.05); padding: 20px; border-radius: 12px; margin: 20px 0; border: 1px solid rgba(255,255,255,0.1);'>" +
                             "<p style='margin: 0;'><strong>Email di ricezione codice:</strong> " + ordine.getIndirizzo() + "</p>" +
                             "<p style='margin: 10px 0 0 0; font-size: 1.2rem;'><strong>Totale: € " + String.format("%.2f", ordine.getTotale()) + "</strong></p>" +
                             "</div>" +
                             "<p>Riceverai il tuo codice digitale a breve all'indirizzo indicato.</p>";
            
            helper.setText(getHtmlTemplate("Grazie per il tuo Acquisto!", content), true);

            mailSender.send(message);
            System.out.println("Email inviata con successo a: " + ordine.getUtente().getMail());
        } catch (Exception e) {
            System.err.println("Errore durante l'invio dell'email: " + e.getMessage());
        }
    }

    private String getHtmlTemplate(String title, String content) {
        return "<!DOCTYPE html>" +
               "<html>" +
               "<head>" +
               "<meta charset='UTF-8'>" +
               "<style>" +
               "@import url('https://fonts.googleapis.com/css2?family=Outfit:wght@300;400;600;700;800&display=swap');" +
               "body { font-family: 'Outfit', Arial, sans-serif; background-color: #0f172a; color: #f1f5f9; margin: 0; padding: 0; -webkit-font-smoothing: antialiased; }" +
               ".container { max-width: 600px; margin: 20px auto; background-color: #1e293b; border-radius: 24px; overflow: hidden; border: 1px solid rgba(255,255,255,0.1); box-shadow: 0 20px 50px rgba(0,0,0,0.3); }" +
               ".header { background: linear-gradient(135deg, #6366f1 0%, #a855f7 50%, #ec4899 100%); padding: 40px 20px; text-align: center; }" +
               ".header h1 { margin: 0; color: #ffffff; font-size: 32px; font-weight: 800; letter-spacing: -1.5px; text-transform: uppercase; }" +
               ".content { padding: 40px; line-height: 1.8; color: #cbd5e1; font-size: 16px; }" +
               ".content h2 { color: #ffffff; font-size: 24px; font-weight: 700; margin-top: 0; margin-bottom: 20px; }" +
               ".footer { padding: 30px; text-align: center; font-size: 13px; color: #64748b; background-color: rgba(15,23,42,0.4); border-top: 1px solid rgba(255,255,255,0.05); }" +
               ".button { display: inline-block; padding: 14px 35px; background: linear-gradient(135deg, #6366f1 0%, #a855f7 100%); color: #ffffff !important; text-decoration: none; border-radius: 14px; font-weight: 700; margin: 20px 0; box-shadow: 0 10px 15px rgba(99,102,241,0.3); }" +
               "strong { color: #ffffff; }" +
               "</style>" +
               "</head>" +
               "<body>" +
               "<div class='container'>" +
               "<div class='header'><h1>CodeShop</h1></div>" +
               "<div class='content'>" +
               "<h2>" + title + "</h2>" +
               content +
               "</div>" +
               "<div class='footer'>" +
               "<p>&copy; 2025 <strong>CodeShop</strong>. Tutti i diritti riservati.</p>" +
               "<p>La piattaforma premium per sviluppatori.</p>" +
               "</div>" +
               "</div>" +
               "</body>" +
               "</html>";
    }
}
