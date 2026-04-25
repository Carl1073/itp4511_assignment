/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.db;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import ict.bean.NotificationBean;
import ict.bean.UserBean;

/**
 *
 * @author 240708635
 */
public class NotificationDB {

    private String url = "";
    private String username = "";
    private String password = "";
    private String query = "SELECT n.*, u.fullName "
            + "FROM notification n "
            + "LEFT JOIN user u ON n.userId = u.userId ";

    public NotificationDB(String url, String username, String password) {
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
            String sql = "CREATE TABLE IF NOT EXISTS notification ("
                    + "notifId INT AUTO_INCREMENT,"
                    + "userId INT NOT NULL,"
                    + "message TEXT NOT NULL,"
                    + "isRead BOOLEAN DEFAULT FALSE,"
                    + "createdAt TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                    + "PRIMARY KEY (notifId),"
                    + "FOREIGN KEY (userId) REFERENCES user(userId)"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean addRecord(NotificationBean nb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "INSERT INTO notification (userId, message) VALUES (?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, nb.getUserId());
            pStmnt.setString(2, nb.getMessage());
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

    private ArrayList<NotificationBean> executeGenericQuery(String sql, Object... params) {
        ArrayList<NotificationBean> nbs = new ArrayList<>();
        // Try-with-resources automatically closes Connection, Statement, and ResultSet
        try (Connection cnnct = getConnection();
                PreparedStatement pStmnt = cnnct.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pStmnt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pStmnt.executeQuery()) {
                while (rs.next()) {
                    nbs.add(this.resultSetToBean(rs));
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return nbs;
    }

    public ArrayList<NotificationBean> queryNotification() {
        return executeGenericQuery(query);
    }

    public ArrayList<NotificationBean> queryNotificationByUserId(int userId) {
        return executeGenericQuery(query + " Where u.userId = ? ORDER BY createdAt DESC ", userId);
    }

    public boolean delRecord(String custId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        boolean isSuccess = false;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DELETE FROM notification WHERE notifId = ?";
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

    public boolean markAsRead(int notifId) {
        String sql = "UPDATE notification SET isRead = true WHERE notifId = ?";
        try (Connection cnnct = getConnection();
                PreparedStatement pStmnt = cnnct.prepareStatement(sql)) {
            pStmnt.setInt(1, notifId);
            return pStmnt.executeUpdate() > 0;
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public int editRecord(NotificationBean nb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE notification SET userId = ?, message = ?, isRead = ? WHERE notifId = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, nb.getUserId());
            pStmnt.setString(2, nb.getMessage());
            pStmnt.setBoolean(3, nb.getIsRead());
            pStmnt.setInt(4, nb.getNotifId());

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
            String preQueryStatement = "DROP TABLE notification";
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

    public NotificationBean resultSetToBean(ResultSet rs) throws SQLException {
        NotificationBean nb = new NotificationBean();
        nb.setNotifId(rs.getInt(1));
        nb.setUserId(rs.getInt(2));
        nb.setMessage(rs.getString(3));
        nb.setIsRead(rs.getBoolean(4));
        nb.setCreatedAt(rs.getTimestamp(5));

        UserBean ub = new UserBean();
//        ub.setUserId(rs.getInt("userId")); // From user table
        ub.setFullName(rs.getString("fullName"));
//        ub.setEmail(rs.getString("email"));
        nb.setUserBean(ub);
        return nb;
    }
}
