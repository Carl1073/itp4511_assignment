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
public class CustomerBean implements Serializable{
    private int custId;
    private String name;
    private String username;
    private String password;
    private String gender;
    private String address;
    private Date dob;
    private String tel;
    private String email;

    public CustomerBean() {
    }

    public CustomerBean(int custId, String name, String username, String password, String gender, String address, Date dob,
            String tel, String email) {
        this.custId = custId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.address = address;
        this.dob = dob;
        this.tel = tel;
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public int getCustId() {
        return custId;
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

    public void setCustId(int custId) {
        this.custId = custId;
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
    
    public String toString(){
        return username;
    }
}