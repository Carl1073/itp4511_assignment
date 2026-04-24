package ict.bean;

import java.io.Serializable;

public class AppointmentBean implements Serializable {
    private int appId;
    private int patientId;
    private UserBean userBean;
    private int timeslotId;
    private TimeslotBean timeslotBean;
    private String status;
    private String cancelReason;

    // Default No-argument Constructor
    public AppointmentBean() {
    }

    // Constructor for all input values
    public AppointmentBean(int appId, int patientId, int timeslotId, String status, String cancelReason) {
        this.appId = appId;
        this.patientId = patientId;
        this.timeslotId = timeslotId;
        this.status = status;
        this.cancelReason = cancelReason;
    }

    // Getters and Setters
    public int getAppId() {
        return appId;
    }

    public void setAppId(int appId) {
        this.appId = appId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(int timeslotId) {
        this.timeslotId = timeslotId;
    }

    public TimeslotBean getTimeslotBean() {
        return timeslotBean;
    }

    public void setTimeslotBean(TimeslotBean timeslotBean) {
        this.timeslotBean = timeslotBean;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }


    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}