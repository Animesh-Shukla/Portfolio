package com.animesh_shukla.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;

import com.animesh_shukla.model.Contact;

@ExtendWith(MockitoExtension.class)
public class send_emailTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private send_email service;

    @BeforeEach
    void setUp() {
        // set recipient field (simulates @Value injection)
        ReflectionTestUtils.setField(service, "recipient", "owner@example.com");
    }

    @Test
    void sendSimpleEmail_callsMailSenderWithCorrectMessage() {
        service.sendSimpleEmail("to@example.com", "Subject", "Body text");

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage msg = captor.getValue();
        assertArrayEquals(new String[] {"to@example.com"}, msg.getTo());
        assertEquals("Subject", msg.getSubject());
        assertEquals("Body text", msg.getText());
    }

    @Test
    void sendContactNotification_buildsAndSendsMessage() {
        Contact contact = mock(Contact.class);
        when(contact.getName()).thenReturn("Alice");
        when(contact.getEmail()).thenReturn("alice@example.com");
        when(contact.getPhone_number()).thenReturn("1234567890");
        when(contact.getMessage()).thenReturn("Hello there");

        service.sendContactNotification(contact);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage sent = captor.getValue();
        assertArrayEquals(new String[] {"owner@example.com"}, sent.getTo());
        assertTrue(sent.getSubject().contains("Alice"));
        assertTrue(sent.getText().contains("Hello there"));
        assertTrue(sent.getText().contains("alice@example.com"));
        assertTrue(sent.getText().contains("1234567890"));
    }

    @Test
    void sendContactNotification_propagatesMailException() {
        Contact contact = mock(Contact.class);
        when(contact.getName()).thenReturn("Bob");
        when(contact.getEmail()).thenReturn("bob@example.com");
        when(contact.getPhone_number()).thenReturn("000");
        when(contact.getMessage()).thenReturn("X");

        doThrow(new MailException("send failed"){}).when(mailSender).send(any(SimpleMailMessage.class));

        assertThrows(MailException.class, () -> service.sendContactNotification(contact));
        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}
