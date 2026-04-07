/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.java;

import ict.db.*;

/**
 *
 * @author 240708635
 */
public class StartUp {

    // Change the method to accept parameters
    public static void startUp(String url, String username, String password) {
        PatientDB patientDb = new PatientDB(url, username, password);
        patientDb.createCustTable();
        ServiceDB serviceDb = new ServiceDB(url, username, password);
        serviceDb.createServiceTable();
    }
}
