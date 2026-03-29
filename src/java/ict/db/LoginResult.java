/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.db;

import ict.bean.CustomerBean;

/**
 *
 * @author Tong
 */
public class LoginResult {
    public int status;
    public CustomerBean customer;

    public LoginResult(int status, CustomerBean customer) {
        this.status = status;
        this.customer = customer;
    }
}
