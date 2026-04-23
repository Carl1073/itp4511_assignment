package ict.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class IncidentLogBean implements Serializable {
    private int logId;
    private int userId;
    private UserBean userBean;
    private String eventType;
    private String description;
    private Timestamp createdAt;

    // Default No-argument Constructor
    public IncidentLogBean() {
    }

    // Constructor for all input values
    public IncidentLogBean(int logId, int userId, String eventType, String description, Timestamp createdAt) {
        this.logId = logId;
        this.userId = userId;
        this.eventType = eventType;
        this.description = description;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}