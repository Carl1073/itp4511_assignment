/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.test.paul;

import ict.bean.*;
import ict.db.*;

/**
 *
 * @author Tong
 */
public class TestUBEditRecord {

    public static void main(String[] arg) {
        String url = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
        String username = "root";
        String password = "";
        UserDB userDB = new UserDB(url, username, password);
        UserBean ub = new UserBean(
                3,
                "edittest3",
                "test",
                "Test",
                "Test",
                "Test",
                "M",
                "patient",
                0 
        );
        userDB.editRecord(ub);
    }
}
