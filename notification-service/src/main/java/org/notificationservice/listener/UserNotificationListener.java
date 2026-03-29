package org.notificationservice.listener;

import org.notificationservice.model.UserNotificationEvent;
import org.notificationservice.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserNotificationListener {

    private static final Logger logger = LoggerFactory.getLogger(UserNotificationListener.class);
    private final EmailService emailService;

    public UserNotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
            topics = "${kafka.listener.notification.topic}",
            groupId = "${kafka.listener.notification.group-id}"
    )
    public void listen(UserNotificationEvent event) {
        logger.info("Получено событие: {} для {}", event.getOperationType(), event.getEmail());

        try {
            emailService.notifyUser(event.getEmail(), event.getOperationType());
            logger.info("Письмо успешно отправлено на {}", event.getEmail());
        } catch (Exception e) {
            logger.error("Ошибка при отправке письма: {}", e.getMessage());
        }
    }
}