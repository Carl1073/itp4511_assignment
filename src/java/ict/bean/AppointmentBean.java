package ict.bean;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class AppointmentBean implements Serializable {
    private int appId;
    private int patientId;
    private int clinicId;
    private int serviceId;
    private Date appDate;
    private Time timeslot;
    private String status;
    private String cancelReason;

    // Default No-argument Constructor
    public AppointmentBean() {
    }

    // Constructor for all input values
    public AppointmentBean(int appId, int patientId, int clinicId, int serviceId, 
                           Date appDate, Time timeslot, String status, String cancelReason) {
        this.appId = appId;
        this.patientId = patientId;
        this.clinicId = clinicId;
        this.serviceId = serviceId;
        this.appDate = appDate;
        this.timeslot = timeslot;
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

    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public Date getAppDate() {
        return appDate;
    }

    public void setAppDate(Date appDate) {
        this.appDate = appDate;
    }

    public Time getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(Time timeslot) {
        this.timeslot = timeslot;
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
}