package org.notificationservice.service;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
    void notifyUser(String email, String operation);
}
