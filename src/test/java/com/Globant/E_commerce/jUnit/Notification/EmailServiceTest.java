package com.Globant.E_commerce.jUnit.Notification;

import com.Globant.E_commerce.Notification.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.*;

class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendEmail() {
        String destination = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        emailService.sendEmail(destination, subject, text);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }
}