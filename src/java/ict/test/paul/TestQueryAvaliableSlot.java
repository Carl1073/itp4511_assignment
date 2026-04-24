/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.test.paul;

import ict.bean.*;
import ict.db.*;
import java.util.ArrayList;

/**
 *
 * @author Tong
 */
public class TestQueryAvaliableSlot {

    public static void main(String[] arg) {
        String url = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
        String username = "root";
        String password = "";
        AppointmentDB csdb = new AppointmentDB(url, username, password);
        java.time.LocalDate today = java.time.LocalDate.now();
        ArrayList<AppointmentBean> csbs = csdb.queryAvailableSlot(1, today);
         for (AppointmentBean bean : csbs) {
            System.out.println(bean);
        }
    }
}
