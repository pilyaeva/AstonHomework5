package org.notificationservice.model;

public class NotificationRequest {
    private String email;
    private String message;
    private String subject;

    public NotificationRequest() {}

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getSubject() { return subject; }
    public void setSubject(String subject) { this.subject = subject; }
}
