package ict.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class NotificationBean implements Serializable {
    private int notifId;
    private int userId;
    private String message;
    private boolean isRead;
    private Timestamp createdAt;

    // Default No-argument Constructor
    public NotificationBean() {
    }

    // Constructor for all input values
    public NotificationBean(int notifId, int userId, String message, boolean isRead, Timestamp createdAt) {
        this.notifId = notifId;
        this.userId = userId;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getNotifId() {
        return notifId;
    }

    public void setNotifId(int notifId) {
        this.notifId = notifId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}