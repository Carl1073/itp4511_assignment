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
import ict.bean.TimeslotBean;
import ict.bean.UserBean;

/**
 *
 * @author 240708635
 */
public class AppointmentDB {

    private String url = "";
    private String username = "";
    private String password = "";
    private String query = "SELECT a.*, u.fullName, t.date, t.openTime, c.clinicId , c.clinicName, s.serviceId ,s.serviceName "
            + "FROM appointment a "
            + "JOIN user u ON a.patientId = u.userId "
            + "JOIN timeslot t ON a.timeslotId = t.timeslotId "
            + "JOIN clinic c ON t.clinicId = c.clinicId "
            + "JOIN service s ON t.serviceId = s.serviceId ";

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
        return DriverManager.getConnection(url + "?useSSL=false", username, password);
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
                    + "timeslotId INT NOT NULL,"
                    + "status ENUM('Confirmed', 'Arrived', 'No-show', 'Completed', 'Cancelled') DEFAULT 'Confirmed',"
                    + "cancelReason VARCHAR(255),"
                    + "PRIMARY KEY (appId),"
                    + "FOREIGN KEY (patientId) REFERENCES user(userId),"
                    + "FOREIGN KEY (timeslotId) REFERENCES timeslot(timeslotId)"
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
            String preQueryStatement = "INSERT INTO appointment (patientId, timeslotId, status, cancelReason) VALUES (?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, cb.getPatientId());
            pStmnt.setInt(2, cb.getTimeslotId());
            pStmnt.setString(3, cb.getStatus());
            pStmnt.setString(4, cb.getCancelReason());
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
                PreparedStatement pStmnt = cnnct.prepareStatement(sql)) {

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
        return executeGenericQuery(query);
    }

    public AppointmentBean queryAppByAppID(int id) {
        ArrayList<AppointmentBean> results = executeGenericQuery(query + " WHERE appId = ? ", id);
        return results.isEmpty() ? null : results.get(0);
    }

    public AppointmentBean queryAppByTsID(int id) {
        ArrayList<AppointmentBean> results = executeGenericQuery(query + " WHERE a.timeslotId = ? ", id);
        return results.isEmpty() ? null : results.get(0);
    }

    public ArrayList<AppointmentBean> queryAppByUserId(int userId) {
        return executeGenericQuery(query + " Where u.userid = ? ", userId);
    }

    public ArrayList<AppointmentBean> queryAppByClinicServiceDate(int clinicId, int serviceId, Date date) {
        return executeGenericQuery(query + " Where c.clinicId = ? and s.serviceId = ? and t.date = ? ", clinicId, serviceId, date);
    }

    public ArrayList<AppointmentBean> queryAvailableSlot(int clinicId, Date date) {
        return executeGenericQuery("SELECT  t.*, "
                + "    (t.quotaPerSlot - COUNT(a.appId)) AS remaining "
                + "FROM timeslot t "
                + "LEFT JOIN appointment a ON t.timeslotId = a.timeslotId "
                + "    AND a.status NOT IN ('Cancelled') "
                + "WHERE t.clinicId = ? AND t.date = ? "
                + "GROUP BY t.timeslotId "
                + "HAVING remaining > 0; ", clinicId, date);
    }

    public int countAppointmentsByTimeslotId(int timeslotid) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        int count = 0;
        try {
            cnnct = getConnection();
            // Only count appointments that are not cancelled
            String preQueryStatement = "SELECT COUNT(*) FROM appointment WHERE timeslotId = ? AND status != 'Cancelled'";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, timeslotid);

            ResultSet rs = pStmnt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
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
        return count;
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

    public int editRecord(AppointmentBean ab) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();

            // Start building the query
            StringBuilder sql = new StringBuilder("UPDATE appointment SET ");
            ArrayList<Object> params = new ArrayList<>();

            // Check each field and add to SQL if not null/zero
            if (ab.getPatientId() != 0) {
                sql.append("patientId = ?, ");
                params.add(ab.getPatientId());
            }
            if (ab.getTimeslotId() != 0) {
                sql.append("timeslotId = ?, ");
                params.add(ab.getTimeslotId());
            }
            if (ab.getStatus() != null) {
                sql.append("status = ?, ");
                params.add(ab.getStatus());
            }
            if (ab.getCancelReason() != null) {
                sql.append("cancelReason = ?, ");
                params.add(ab.getCancelReason());
            }

            // Remove the trailing comma and space
            if (params.isEmpty()) {
                return 0; // Nothing to update
            }
            sql.setLength(sql.length() - 2);

            // Add the WHERE clause
            sql.append(" WHERE appId = ?");
            params.add(ab.getAppId());

            pStmnt = cnnct.prepareStatement(sql.toString());

            // Set parameters dynamically
            for (int i = 0; i < params.size(); i++) {
                pStmnt.setObject(i + 1, params.get(i));
            }

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
        ab.setTimeslotId(rs.getInt(3));
        ab.setStatus(rs.getString(4));
        ab.setCancelReason(rs.getString(5));

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

        TimeslotBean tb = new TimeslotBean();
        tb.setClinicName(rs.getString("clinicName"));
        tb.setServiceName(rs.getString("serviceName"));
        tb.setTimeslotId(rs.getInt(3));
        tb.setClinicId(rs.getInt("clinicId"));
        tb.setServiceId(rs.getInt("serviceId"));
        tb.setDate(rs.getDate("date"));
        tb.setOpenTime(rs.getTime("openTime"));
        ab.setTimeslotBean(tb);
        return ab;
    }
}
