/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.test.paul;


import ict.db.PatientDB;

/**
 *
 * @author Tong
 */
public class TestDropCustTable {

    public static void main(String[] arg) {
        String url = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
        String username = "root";
        String password = "";
        PatientDB custDb = new PatientDB(url, username, password);
        custDb.dropCustTable();
    }
}
