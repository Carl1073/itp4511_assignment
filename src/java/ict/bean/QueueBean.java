package ict.bean;

import java.io.Serializable;
import java.sql.Timestamp;

public class QueueBean implements Serializable {
    private int queueId;
    private int patientId;
    private UserBean userBean;
    private int clinicId;
    private ClinicBean clinicBean;
    private int serviceId;
    private ServiceBean serviceBean;
    private int queueNumber;
    private Timestamp entryTime;
    private String status;

    // Default No-argument Constructor
    public QueueBean() {
    }

    // Constructor for all input values
    public QueueBean(int queueId, int patientId, int clinicId, int serviceId, 
                     int queueNumber, Timestamp entryTime, String status) {
        this.queueId = queueId;
        this.patientId = patientId;
        this.clinicId = clinicId;
        this.serviceId = serviceId;
        this.queueNumber = queueNumber;
        this.entryTime = entryTime;
        this.status = status;
    }

    // Getters and Setters
    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
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

    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }

    public Timestamp getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(Timestamp entryTime) {
        this.entryTime = entryTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public ClinicBean getClinicBean() {
        return clinicBean;
    }

    public ServiceBean getServiceBean() {
        return serviceBean;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setClinicBean(ClinicBean clinicBean) {
        this.clinicBean = clinicBean;
    }

    public void setServiceBean(ServiceBean serviceBean) {
        this.serviceBean = serviceBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }
}