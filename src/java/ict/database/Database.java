/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author slt8ky
 */
public abstract class Database {
    public static Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB?allowPublicKeyRetrieval=true&useSSL=false", "root", "hD3gLGMAsWnLk5m7");
    }
}
