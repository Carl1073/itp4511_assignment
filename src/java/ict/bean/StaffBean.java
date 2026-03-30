/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.bean;

import java.io.Serializable;

/**
 *
 * @author 240708635
 */
public class StaffBean implements Serializable, People {

    private String stafffId;
    private String name;
    private String password;
    private String role;

    @Override
    public String getRole() {
        return role;
    }
}
