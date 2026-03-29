package org.notificationservice.config;

import org.notificationservice.service.EmailService;
import org.notificationservice.service.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class ServiceConfig {
    @Bean
    public EmailService emailService(JavaMailSender mailSender,
                                     @Value("${app.mail.from}") String fromAddress) {
        return new EmailServiceImpl(mailSender, fromAddress);
    }
}
