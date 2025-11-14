package com.animesh_shukla.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.animesh_shukla.model.Contact;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Service
public class send_email {

    private final JavaMailSender mailSender;

    @Value("${app.contact.recipient:parzival9987@gmail.com}")
    private String recipient;

    @Autowired
    public send_email(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(subject);
        msg.setText(text);
        mailSender.send(msg);
    }

    // New: send contact form data to configured recipient
    public void sendContactNotification(Contact contact) throws MailException {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(recipient);
        msg.setSubject("New contact from " + contact.getName());
        StringBuilder sb = new StringBuilder();
        sb.append("Name: ").append(contact.getName()).append("\n");
        sb.append("Email: ").append(contact.getEmail()).append("\n");
        sb.append("Phone: ").append(contact.getPhone_number()).append("\n\n");
        sb.append("Message:\n").append(contact.getMessage());
        msg.setText(sb.toString());
        mailSender.send(msg);
    }

}
