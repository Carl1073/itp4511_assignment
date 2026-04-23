package ict.bean;

import java.io.Serializable;

public class ServiceBean implements Serializable {

    private int serviceId;
    private String serviceName;
    private String description;
    private double price;
    private int duration; // in minute

    // Default No-argument Constructor
    public ServiceBean() {
    }

    // Constructor for all input values
    public ServiceBean(int serviceId, String serviceName, String description, double price, int duration) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    // Getters and Setters
    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
