package org.userservice.model;

public class UserNotificationEvent {

    private String email;
    private String operationType;

    public UserNotificationEvent() {
    }

    public UserNotificationEvent(String email, String operationType) {
        this.email = email;
        this.operationType = operationType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }
}