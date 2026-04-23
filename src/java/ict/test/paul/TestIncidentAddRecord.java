/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.test.paul;

import ict.bean.*;
import ict.db.*;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 *
 * @author Tong
 */
public class TestIncidentAddRecord {

    public static void main(String[] arg) {
        String url = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
        String username = "root";
        String password = "";
        IncidentLogDB incidentLogDB = new IncidentLogDB(url, username, password);
        int userid = 1;
        IncidentLogBean ilb = new IncidentLogBean(
                0,
                userid,
                "abc",
                "abc",
                Timestamp.valueOf( LocalDateTime.now())
        );
        incidentLogDB.addRecord(ilb);
        ArrayList<IncidentLogBean> ilbChecking = incidentLogDB.queryIncidentLogByUserId(userid);
        for (IncidentLogBean bean : ilbChecking) {
            System.out.println(bean);
        }
    }
}
