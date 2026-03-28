/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.java;

import ict.db.CustomerDB;


/**
 *
 * @author 240708635
 */
public class StartUp {
    // Change the method to accept parameters
    public static void startUp(String url, String username, String password) {
        CustomerDB custDb = new CustomerDB(url, username, password);
        custDb.createCustTable();
    }
}