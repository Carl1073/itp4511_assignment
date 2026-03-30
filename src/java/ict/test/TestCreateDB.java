/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.test;

import ict.db.PatientDB;

/**
 *
 * @author Tong
 */
public class TestCreateDB {

    public static void main(String[] arg) {
        String url ="jdbc:mysql://localhost:3306/";
        String username = "root";
        String password = "";   
        PatientDB custDb = new PatientDB(url, username, password);
        String dbname= "ITP4511_Assignment_DB";
        custDb.createDB(dbname);
    }
}
