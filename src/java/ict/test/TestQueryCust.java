/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.test;

import ict.bean.CustomerBean;
import ict.db.CustomerDB;

/**
 *
 * @author Tong
 */
public class TestQueryCust {

    public static void main(String[] arg) {
        String url = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
        String username = "root";
        String password = "";
        CustomerDB custDb = new CustomerDB(url, username, password);
        CustomerBean cb = custDb.queryCust();
        System.out.println(cb.getName());
    }
}
