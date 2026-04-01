package ict.db;

import java.io.IOException;
import java.sql.*;

public abstract class Database {
    private String url = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
    private String username = "root";
    private String password = "hD3gLGMAsWnLk5m7";

    public Connection getConnection() throws SQLException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return DriverManager.getConnection(url, username, password);
    }
}