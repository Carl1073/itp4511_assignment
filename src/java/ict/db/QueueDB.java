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
public class QueueDB {

    private String url = "";
    private String username = "";
    private String password = "";
    private String query = "SELECT q.* , u.fullName , c.clinicName , s.serviceName "
            + "FROM queue q "
            + "LEFT JOIN user u ON q.patientId = u.userid "
            + "LEFT JOIN clinic c ON q.clinicId = c.clinicId "
            + "LEFT JOIN service s ON q.serviceId = s.serviceId ";

    public QueueDB(String url, String username, String password) {
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
            String sql = "CREATE TABLE IF NOT EXISTS queue ("
                    + "queueId INT AUTO_INCREMENT,"
                    + "patientId INT NOT NULL,"
                    + "clinicId INT NOT NULL,"
                    + "serviceId INT NOT NULL,"
                    + "queueNumber INT NOT NULL,"
                    + "entryTime TIME DEFAULT CURRENT_TIMESTAMP,"
                    + "status ENUM('Waiting', 'Called', 'Skipped', 'Served') DEFAULT 'Waiting',"
                    + "PRIMARY KEY (queueId),"
                    + "FOREIGN KEY (patientId) REFERENCES user(userId),"
                    + "FOREIGN KEY (clinicId) REFERENCES clinic(clinicId),"
                    + "FOREIGN KEY (serviceId) REFERENCES service(serviceId)"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean addRecord(QueueBean qb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "INSERT INTO queue (patientId, clinicId, serviceId, queueNumber, status) VALUES (?,?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, qb.getPatientId());
            pStmnt.setInt(2, qb.getClinicId());
            pStmnt.setInt(3, qb.getServiceId());
            pStmnt.setInt(4, qb.getQueueNumber());
            pStmnt.setString(5, qb.getStatus());
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

    // Add this to QueueDB.java
    public int getCurrentCallingNumber(int clinicId, int serviceId) {
        int currentNum = 0;
        // We look for the maximum number that is NOT 'Waiting' to see who was last called
        String sql = "SELECT MAX(queueNumber) FROM queue WHERE clinicId = ? AND serviceId = ? "
                + "AND status IN ('Called', 'Served') AND DATE(entryTime) = CURDATE()";
        try (Connection cnnct = getConnection();
                PreparedStatement pStmnt = cnnct.prepareStatement(sql)) {
            pStmnt.setInt(1, clinicId);
            pStmnt.setInt(2, serviceId);
            try (ResultSet rs = pStmnt.executeQuery()) {
                if (rs.next()) {
                    currentNum = rs.getInt(1);
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return currentNum;
    }

    // Add this method to QueueDB.java
    public int getNextQueueNumber(int clinicId, int serviceId) {
        int nextNum = 1;
        String sql = "SELECT MAX(queueNumber) FROM queue WHERE clinicId = ? AND serviceId = ? AND DATE(entryTime) = CURDATE()";
        try (Connection cnnct = getConnection();
                PreparedStatement pStmnt = cnnct.prepareStatement(sql)) {
            pStmnt.setInt(1, clinicId);
            pStmnt.setInt(2, serviceId);
            try (ResultSet rs = pStmnt.executeQuery()) {
                if (rs.next()) {
                    nextNum = rs.getInt(1) + 1;
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return nextNum;
    }

    private ArrayList<QueueBean> executeGenericQuery(String sql, Object... params) {
        ArrayList<QueueBean> qbs = new ArrayList<>();
        // Try-with-resources automatically closes Connection, Statement, and ResultSet
        try (Connection cnnct = getConnection();
                PreparedStatement pStmnt = cnnct.prepareStatement(query + sql)) {

            for (int i = 0; i < params.length; i++) {
                pStmnt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pStmnt.executeQuery()) {
                while (rs.next()) {
                    qbs.add(this.resultSetToBean(rs));
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return qbs;
    }

    public ArrayList<QueueBean> queryQueue() {
        return executeGenericQuery("");
    }

    public QueueBean queryQueueById(int queueId) {
        ArrayList<QueueBean> qbs = executeGenericQuery("WHERE q.queueId = ?", queueId);
        return qbs.isEmpty() ? null : qbs.get(0);
    }

    public boolean delRecord(String custId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        boolean isSuccess = false;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DELETE FROM queue WHERE queueId = ?";
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

    public int editRecord(QueueBean qb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE queue SET patientId = ?, clinicId = ?, serviceId = ?, queueNumber = ?, status = ? WHERE queueId = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, qb.getPatientId());
            pStmnt.setInt(2, qb.getClinicId());
            pStmnt.setInt(3, qb.getServiceId());
            pStmnt.setInt(4, qb.getQueueNumber());
            pStmnt.setString(5, qb.getStatus());
            pStmnt.setInt(6, qb.getQueueId());

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
            String preQueryStatement = "DROP TABLE queue";
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

    public QueueBean resultSetToBean(ResultSet rs) throws SQLException {
        QueueBean qb = new QueueBean();
        qb.setQueueId(rs.getInt(1));
        qb.setPatientId(rs.getInt(2));
        qb.setClinicId(rs.getInt(3));
        qb.setServiceId(rs.getInt(4));
        qb.setQueueNumber(rs.getInt(5));
        qb.setEntryTime(rs.getTimestamp(6));
        qb.setStatus(rs.getString(7));

        UserBean ub = new UserBean();
//        ub.setUsername(rs.getString("username"));
//        ub.setPassword(rs.getString("password"));
        ub.setFullName(rs.getString("fullName"));
//        ub.setEmail(rs.getString("email"));
//        ub.setPhone(rs.getString("phone"));
//        ub.setGender(rs.getString("gender"));
//        ub.setRole(rs.getString("role"));
//        ub.setClinicId(rs.getInt("clinicId"));
        qb.setUserBean(ub);

        ClinicBean cb = new ClinicBean();
        cb.setClinicName(rs.getString("clinicName"));
//        cb.setAddress(rs.getString("address"));
//        cb.setOpenTime(rs.getTime("openTime"));
//        cb.setCloseTime(rs.getTime("closeTime"));
//        cb.setIsWalkinEnabled(rs.getBoolean("isWalkinEnabled"));
        qb.setClinicBean(cb);

        ServiceBean sb = new ServiceBean();
        sb.setServiceName(rs.getString("serviceName"));
//        sb.setDescription(rs.getString("description"));
//        sb.setPrice(rs.getDouble("price"));
        qb.setServiceBean(sb);

        return qb;
    }
}
