package ict.bean;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class TimeslotBean implements Serializable {

    private int timeslotId;
    private int clinicId;
    private ClinicBean clinicBean = new ClinicBean();
    private int serviceId;
    private ServiceBean serviceBean = new ServiceBean();
    private Date date;
    private Time openTime;
    private int quotaPerSlot;
    private int remaining;

    // Default No-argument Constructor
    public TimeslotBean() {
        
    }

    // Constructor for all input values
    public TimeslotBean(int timeslotId, int clinicId, int serviceId, Date date, Time openTime, int quotaPerSlot) {

        this.timeslotId = timeslotId;
        this.clinicId = clinicId;
        this.serviceId = serviceId;
        this.date = date;
        this.openTime = openTime;
        this.quotaPerSlot = quotaPerSlot;
    }

    // Getters and Setters
    public int getTimeslotId() {
        return timeslotId;
    }

    public void setTimeslotId(int timeslotId) {
        this.timeslotId = timeslotId;
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

    public int getQuotaPerSlot() {
        return quotaPerSlot;
    }

    public void setQuotaPerSlot(int quotaPerSlot) {
        this.quotaPerSlot = quotaPerSlot;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getOpenTime() {
        return openTime;
    }

    public void setOpenTime(Time openTime) {
        this.openTime = openTime;
    }
    
    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }
    
    public String getClinicName(){
        return clinicBean.getClinicName();
    }
    
    public void setClinicName(String clinicName){
        clinicBean.setClinicName(clinicName);
    }

        public String getServiceName(){
        return serviceBean.getServiceName();
    }
    
    public void setServiceName(String serviceName){
        serviceBean.setServiceName(serviceName);
    }
}
