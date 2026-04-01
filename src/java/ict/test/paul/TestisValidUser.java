/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.test.paul;


import ict.bean.PatientBean;
import ict.db.PatientDB;
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
        PatientDB custDb = new PatientDB(url, username, password);
        LoginResult cb = custDb.isValidUser("abc123", "123");
        System.out.println(cb.patient);
    }
}
