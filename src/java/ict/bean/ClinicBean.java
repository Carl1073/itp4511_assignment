package ict.bean;

import java.io.Serializable;

public class ClinicBean implements Serializable {
    private int clinicId;
    private String clinicName;
    private String address;
    private boolean isWalkinEnabled;

    // Default No-argument Constructor
    public ClinicBean() {
    }

    // Constructor for all input values
    public ClinicBean(int clinicId, String clinicName, String address, boolean isWalkinEnabled) {
        this.clinicId = clinicId;
        this.clinicName = clinicName;
        this.address = address;
        this.isWalkinEnabled = isWalkinEnabled;
    }

    // Getters and Setters
    public int getClinicId() {
        return clinicId;
    }

    public void setClinicId(int clinicId) {
        this.clinicId = clinicId;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean getIsWalkinEnabled() {
        return isWalkinEnabled;
    }

    public void setIsWalkinEnabled(boolean isWalkinEnabled) {
        this.isWalkinEnabled = isWalkinEnabled;
    }
}