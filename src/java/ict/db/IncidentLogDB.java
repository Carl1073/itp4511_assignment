/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.db;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import ict.bean.*;

/**
 *
 * @author 240708635
 */
public class IncidentLogDB {

    private String url = "";
    private String username = "";
    private String password = "";
    private String query = "SELECT l.*, u.fullName, u.email, u.phone "
            + "FROM incident_log l "
            + "LEFT JOIN user u ON l.userId = u.userId ";

    public IncidentLogDB(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException, IOException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return DriverManager.getConnection(url, username, password);
    }

    public void createDB(String createDB) {
        Connection cnnct = null;
        Statement stmnt = null;

        try {
            cnnct = getConnection(); // the connection
            stmnt = cnnct.createStatement(); // create statement

            String sql = "CREATE DATABASE " + createDB;
            stmnt.execute(sql);

            stmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createTable() {
        Statement stmnt = null;
        Connection cnnct = null;

        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS incident_log ("
                    + "logId INT AUTO_INCREMENT,"
                    + "userId INT NOT NULL," // Target patient or staff
                    + "eventType VARCHAR(50) NOT NULL," // e.g., "Repeated No-show" or "Service Suspension"
                    + "description TEXT," // Details for Admin review
                    + "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "PRIMARY KEY (logId),"
                    + "FOREIGN KEY (userId) REFERENCES user(userId)"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean addRecord(IncidentLogBean ib) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "INSERT INTO incident_log (userId, eventType, description) VALUES (?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, ib.getUserId());
            pStmnt.setString(2, ib.getEventType());
            pStmnt.setString(3, ib.getDescription());
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    private ArrayList<IncidentLogBean> executeGenericQuery(String sql, Object... params) {
        ArrayList<IncidentLogBean> list = new ArrayList<>();
        try (Connection conn = getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Set parameters dynamically
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(resultSetToBean(rs));
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public ArrayList<IncidentLogBean> queryIncidentLog() {
        String sql = query;
        return executeGenericQuery(sql);
    }

    public ArrayList<IncidentLogBean> queryIncidentLogByUserId(int userId) {
        String sql = query + " where l.userid = ?";
        return executeGenericQuery(sql, userId);
    }

    public boolean delRecord(String custId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        boolean isSuccess = false;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DELETE FROM incident_log WHERE logid = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, custId);

            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public int editRecord(IncidentLogBean ib) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE incident_log SET userId = ?, eventType = ?, description = ? WHERE logid = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, ib.getUserId());
            pStmnt.setString(2, ib.getEventType());
            pStmnt.setString(3, ib.getDescription());
            pStmnt.setInt(4, ib.getLogId());

            int rs = pStmnt.executeUpdate();
            pStmnt.close();
            cnnct.close();
            return rs;
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public void dropCustTable() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DROP TABLE incident_log";
            pStmnt = cnnct.prepareStatement(preQueryStatement);

            pStmnt.execute();
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public IncidentLogBean resultSetToBean(ResultSet rs) throws SQLException {
        IncidentLogBean ib = new IncidentLogBean();
        ib.setLogId(rs.getInt(1));
        ib.setUserId(rs.getInt(2));
        ib.setEventType(rs.getString(3));
        ib.setDescription(rs.getString(4));
        ib.setCreatedAt(rs.getTimestamp(5));

        UserBean ub = new UserBean();
        // ub.setUserId(rs.getInt("userId")); // From user table
        ub.setFullName(rs.getString("fullName"));
        // ub.setEmail(rs.getString("email"));
        ib.setUserBean(ub);
        return ib;
    }
}
