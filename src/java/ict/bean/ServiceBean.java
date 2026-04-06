/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.bean;

import ict.ocp.*;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

/**
 *
 * @author 240708635
 */
public class ServiceBean implements Serializable {
    private String serviceId;
    private String _custId;
    private Date date;
    private Time time;
    private String location;
    private ServiceType serviceType;
    private ServiceMode serviceMode;
    private String status;

    public ServiceBean() {
    }

    public ServiceBean(String serviceId, String _custId, Date date, Time time, String location, ServiceType serviceType,
            ServiceMode serviceMode, String status) {
        this.serviceId = serviceId;
        this._custId = _custId;
        this.date = date;
        this.time = time;
        this.location = location;
        this.serviceType = serviceType;
        this.serviceMode = serviceMode;
        this.status = status;
    }

    public Date getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getServiceId() {
        return serviceId;
    }

    public ServiceMode getServiceMode() {
        return serviceMode;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public String getStatus() {
        return status;
    }

    public Time getTime() {
        return time;
    }

    public String get_custId() {
        return _custId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public void setServiceMode(ServiceMode serviceMode) {
        this.serviceMode = serviceMode;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public void set_custId(String _custId) {
        this._custId = _custId;
    }

}
