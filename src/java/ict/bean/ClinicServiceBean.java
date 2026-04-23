package ict.bean;

import java.io.Serializable;

public class ClinicServiceBean implements Serializable {
    private int clinicId;
    private ClinicBean clinicBean;
    private int serviceId;
    private ServiceBean serviceBean;
    private int quotaPerSlot;
    private int duration; // in minute

    // Default No-argument Constructor
    public ClinicServiceBean() {
    }

    // Constructor for all input values
    public ClinicServiceBean(int clinicId, int serviceId, int quotaPerSlot, int duration) {
        this.clinicId = clinicId;
        this.serviceId = serviceId;
        this.quotaPerSlot = quotaPerSlot;
        this.duration = duration;
    }

    // Getters and Setters
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

    public int getQuotaPerSlot() {
        return quotaPerSlot;
    }

    public void setQuotaPerSlot(int quotaPerSlot) {
        this.quotaPerSlot = quotaPerSlot;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public ClinicBean getClinicBean() {
        return clinicBean;
    }

    public void setClinicBean(ClinicBean clinicBean) {
        this.clinicBean = clinicBean;
    }

    public ServiceBean getServiceBean() {
        return serviceBean;
    }

    public void setServiceBean(ServiceBean serviceBean) {
        this.serviceBean = serviceBean;
    }
}