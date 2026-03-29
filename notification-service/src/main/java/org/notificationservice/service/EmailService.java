package org.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;
    private final String fromAddress;

    public EmailService(JavaMailSender mailSender,
                        @Value("${app.mail.from}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            logger.info("Сообщение отправлено на {}", to);
        } catch (MailException e) {
            logger.error("Ошибка отправки сообщения на {}: {}", to, e.getMessage(), e);
        }
    }

    public void notifyUser(String email, String operation) {
        String subject;
        String text;

        if ("DELETE".equals(operation)) {
            subject = "Удаление аккаунта";
            text = "Здравствуйте! Ваш аккаунт был удалён.";
        } else if ("CREATE".equals(operation)) {
            subject = "Создание аккаунта";
            text = "Здравствуйте! Ваш аккаунт на сайте ваш сайт был успешно создан.";
        } else {
            logger.warn("Неизвестная операция: {}", operation);
            return;
        }

        sendEmail(email, subject, text);
    }
}