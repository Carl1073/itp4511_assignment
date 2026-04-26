/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.bean;

/**
 *
 * @author slt8ky
 */
public class MonthlyReportBean {
    private String clinicName;
    private int monthlyPrice;
    private int monthly;

    public MonthlyReportBean(String clinicName, int monthlyPrice, int monthly) {
        this.clinicName = clinicName;
        this.monthlyPrice = monthlyPrice;
        this.monthly = monthly;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public int getMonthly() {
        return monthly;
    }

    public void setMonthly(int monthly) {
        this.monthly = monthly;
    }

    public int getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(int monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }
}