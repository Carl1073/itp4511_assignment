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
public class YearlyReportBean {
    private String clinicName;
    private int yearlyPrice;
    private int yearly;

    public YearlyReportBean(String clinicName, int yearlyPrice, int yearly) {
        this.clinicName = clinicName;
        this.yearlyPrice = yearlyPrice;
        this.yearly = yearly;
    }

    public String getClinicName() {
        return clinicName;
    }

    public void setClinicName(String clinicName) {
        this.clinicName = clinicName;
    }

    public int getYearly() {
        return yearly;
    }

    public void setYearly(int yearly) {
        this.yearly = yearly;
    }

    public int getYearlyPrice() {
        return yearlyPrice;
    }

    public void setYearlyPrice(int yearlyPrice) {
        this.yearlyPrice = yearlyPrice;
    }
}