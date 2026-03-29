package org.notificationservice.controller;

import org.notificationservice.model.NotificationRequest;
import org.notificationservice.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final EmailService emailService;

    public NotificationController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendDirectNotification(@RequestBody NotificationRequest request) {
        emailService.sendEmail(request.getEmail(), request.getSubject(), request.getMessage());
        return ResponseEntity.ok("Сообщение отправлено на " + request.getEmail());
    }
}
