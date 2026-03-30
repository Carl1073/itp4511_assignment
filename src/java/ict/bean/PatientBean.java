/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.bean;

import java.io.Serializable;
import java.sql.Date;

/**
 *
 * @author 240708635
 */
public class PatientBean implements Serializable, People {

    private int patId;
    private String name;
    private String username;
    private String password;
    private String gender;
    private String address;
    private Date dob;
    private String tel;
    private String email;
    private String role;

    public PatientBean() {
        setRole();
    }

    public PatientBean(int patId, String name, String username, String password, String gender, String address, Date dob,
            String tel, String email) {
        this.patId = patId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.dob = dob;
        this.tel = tel;
        this.email = email;
        setRole();
    }

    public String getAddress() {
        return address;
    }

    public int getPatId() {
        return patId;
    }

    public Date getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getGender() {
        return gender;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getTel() {
        return tel;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPatId(int patId) {
        this.patId = patId;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String toString() {
        return username;
    }

    public void setRole() {
        this.role = "customer";
    }

    @Override
    public String getRole() {
        return role;
    }
}
