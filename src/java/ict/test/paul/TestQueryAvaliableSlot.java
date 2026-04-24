/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.test.paul;

import ict.bean.*;
import ict.db.*;
import java.sql.Date;
import java.util.ArrayList;
import java.time.LocalDate;

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
        LocalDate today = LocalDate.now();
        ArrayList<AppointmentBean> csbs = csdb.queryAvailableSlot(1, Date.valueOf(today));
         for (AppointmentBean bean : csbs) {
            System.out.println(bean);
        }
    }
}
