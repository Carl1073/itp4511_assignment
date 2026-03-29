/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.test;

import ict.bean.CustomerBean;
import ict.db.CustomerDB;
import ict.db.LoginResult;

/**
 *
 * @author Tong
 */
public class TestisValidUser {

    public static void main(String[] arg) {
        String url = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
        String username = "root";
        String password = "";
        CustomerDB custDb = new CustomerDB(url, username, password);
        LoginResult cb = custDb.isValidUser("abc123", "123");
        System.out.println(cb.customer);
    }
}
