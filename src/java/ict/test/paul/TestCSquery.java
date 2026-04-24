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

/**
 *
 * @author Tong
 */
public class TestCSquery {

    public static void main(String[] arg) {
        String url = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
        String username = "root";
        String password = "";
        TimeslotDB tdb = new TimeslotDB(url, username, password);
        java.time.LocalDate today = java.time.LocalDate.now();
        ArrayList<TimeslotBean> timeslots = tdb.queryTimeslotbyDateClinicService(Date.valueOf(today), 2, 2);

        for (TimeslotBean bean : timeslots) {
            System.out.println(bean.getClinicId());
        }
    }
}
