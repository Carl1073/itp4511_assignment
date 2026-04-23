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
public class TestClinicQuery {

    public static void main(String[] arg) {
        String url = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
        String username = "root";
        String password = "";
        ClinicDB cdb = new ClinicDB(url, username, password);
        ArrayList<ClinicBean> cb = cdb.queryClinic();
         for (ClinicBean bean : cb) {
            System.out.println(bean.getOpenTime());
        }
    }
}
