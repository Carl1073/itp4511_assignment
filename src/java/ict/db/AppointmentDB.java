/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.db;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import ict.bean.AppointmentBean;
import ict.bean.ClinicBean;
import ict.bean.ServiceBean;
import ict.bean.UserBean;

/**
 *
 * @author 240708635
 */
public class AppointmentDB {

    private String url = "";
    private String username = "";
    private String password = "";
    private String query = "SELECT a.* , u.fullName , c.clinicName , s.serviceName "
            + "FROM appointment a "
            + "LEFT JOIN user u ON a.patientId = u.userid "
            + "LEFT JOIN clinic c ON a.clinicId = c.clinicId "
            + "LEFT JOIN service s ON a.serviceId = s.serviceId ";

    public AppointmentDB(String url, String username, String password) {
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
            String sql = "CREATE TABLE IF NOT EXISTS appointment ("
                    + "appId INT AUTO_INCREMENT,"
                    + "patientId INT NOT NULL,"
                    + "clinicId INT NOT NULL,"
                    + "serviceId INT NOT NULL,"
                    + "appDate DATE NOT NULL,"
                    + "timeslot TIME NOT NULL,"
                    + "status ENUM('Confirmed', 'Arrived', 'No-show', 'Completed', 'Cancelled') DEFAULT 'Confirmed',"
                    + "cancelReason VARCHAR(255),"
                    + "PRIMARY KEY (appId),"
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

    public boolean addRecord(AppointmentBean cb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "INSERT INTO appointment (patientId, clinicId, serviceId, appDate, timeslot, status, cancelReason) VALUES (?,?,?,?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, cb.getPatientId());
            pStmnt.setInt(2, cb.getClinicId());
            pStmnt.setInt(3, cb.getServiceId());
            pStmnt.setDate(4, cb.getAppDate());
            pStmnt.setTime(5, cb.getTimeslot());
            pStmnt.setString(6, cb.getStatus());
            pStmnt.setString(7, cb.getCancelReason());
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

    private ArrayList<AppointmentBean> executeGenericQuery(String sql, Object... params) {
        ArrayList<AppointmentBean> abs = new ArrayList<>();
        // Try-with-resources automatically closes Connection, Statement, and ResultSet
        try (Connection cnnct = getConnection();
                PreparedStatement pStmnt = cnnct.prepareStatement(query + sql)) {

            for (int i = 0; i < params.length; i++) {
                pStmnt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pStmnt.executeQuery()) {
                while (rs.next()) {
                    abs.add(this.resultSetToBean(rs));
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return abs;
    }

    public ArrayList<AppointmentBean> queryApp() {
        return executeGenericQuery("");
    }

    public boolean delRecord(String custId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        boolean isSuccess = false;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DELETE FROM Appointment WHERE appid = ?";
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

    public int editRecord(AppointmentBean cb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE appointment SET patientId = ?, clinicId = ?, serviceId = ?, appDate = ?, timeslot = ?, status = ?, cancelReason = ? WHERE appid = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, cb.getPatientId());
            pStmnt.setInt(2, cb.getClinicId());
            pStmnt.setInt(3, cb.getServiceId());
            pStmnt.setDate(4, cb.getAppDate());
            pStmnt.setTime(5, cb.getTimeslot());
            pStmnt.setString(6, cb.getStatus());
            pStmnt.setString(7, cb.getCancelReason());
            pStmnt.setInt(8, cb.getAppId());

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
            String preQueryStatement = "DROP TABLE Appointment";
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

    public AppointmentBean resultSetToBean(ResultSet rs) throws SQLException {
        AppointmentBean ab = new AppointmentBean();
        ab.setAppId(rs.getInt(1));
        ab.setPatientId(rs.getInt(2));
        ab.setClinicId(rs.getInt(3));
        ab.setServiceId(rs.getInt(4));
        ab.setAppDate(rs.getDate(5));
        ab.setTimeslot(rs.getTime(6));
        ab.setStatus(rs.getString(7));
        ab.setCancelReason(rs.getString(8));

        UserBean ub = new UserBean();
        // ub.setUsername(rs.getString("username"));
        // ub.setPassword(rs.getString("password"));
        ub.setFullName(rs.getString("fullName"));
        // ub.setEmail(rs.getString("email"));
        // ub.setPhone(rs.getString("phone"));
        // ub.setGender(rs.getString("gender"));
        // ub.setRole(rs.getString("role"));
        // ub.setClinicId(rs.getInt("clinicId"));
        ab.setUserBean(ub);

        ClinicBean cb = new ClinicBean();
        cb.setClinicName(rs.getString("clinicName"));
        // cb.setAddress(rs.getString("address"));
        // cb.setOpenTime(rs.getTime("openTime"));
        // cb.setCloseTime(rs.getTime("closeTime"));
        // cb.setIsWalkinEnabled(rs.getBoolean("isWalkinEnabled"));
        ab.setClinicBean(cb);

        ServiceBean sb = new ServiceBean();
        sb.setServiceName(rs.getString("serviceName"));
        // sb.setDescription(rs.getString("description"));
        // sb.setPrice(rs.getDouble("price"));
        ab.setServiceBean(sb);
        return ab;
    }
}
