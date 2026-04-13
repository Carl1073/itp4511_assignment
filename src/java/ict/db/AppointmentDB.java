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

/**
 *
 * @author 240708635
 */
public class AppointmentDB {

    private String url = "";
    private String username = "";
    private String password = "";

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
                    + "FOREIGN KEY (patientId) REFERENCES users(userId),"
                    + "FOREIGN KEY (clinicId) REFERENCES clinics(clinicId),"
                    + "FOREIGN KEY (serviceId) REFERENCES services(serviceId)"
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

    public ArrayList<AppointmentBean> queryCust() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<AppointmentBean> cbs = new ArrayList<>();

        AppointmentBean cb = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM patient";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                cb = this.reseltSetToBean(rs);
                cbs.add(cb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return cbs;
    }

    // public PatientBean queryCustByID(String id) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // PatientBean cb = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE CUSTID = ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, id);
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // if (rs.next()) {
    // cb = reseltSetToBean(rs);
    // }
    // pStmnt.close();
    // cnnct.close();
    // } catch (SQLException ex) {
    // while (ex != null) {
    // ex.printStackTrace();
    // ex = ex.getNextException();
    // }
    // } catch (IOException ex) {
    // ex.printStackTrace();
    // }
    // return cb;
    // }
    // public ArrayList<PatientBean> queryCustByName(String name) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> cbs = new ArrayList<>();
    // PatientBean cb = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE NAME LIKE ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, "%" + name + "%");
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // cb = new PatientBean();
    // cb.setCustId(rs.getString(1));
    // cb.setName(rs.getString(2));
    // cb.setTel(rs.getString(3));
    // cb.setAge(rs.getInt(4));
    // cbs.add(cb);
    // }
    // pStmnt.close();
    // cnnct.close();
    // } catch (SQLException ex) {
    // while (ex != null) {
    // ex.printStackTrace();
    // ex = ex.getNextException();
    // }
    // } catch (IOException ex) {
    // ex.printStackTrace();
    // }
    // return cbs;
    // }
    // public ArrayList<PatientBean> queryCustByTel(String tel) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> cbs = new ArrayList<PatientBean>();
    // PatientBean cb = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE TEL LIKE ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, "%" + tel + "%");
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // cb = new PatientBean();
    // cb.setCustId(rs.getString(1));
    // cb.setName(rs.getString(2));
    // cb.setTel(rs.getString(3));
    // cb.setAge(rs.getInt(4));
    // cbs.add(cb);
    // }
    // pStmnt.close();
    // cnnct.close();
    // } catch (SQLException ex) {
    // while (ex != null) {
    // ex.printStackTrace();
    // ex = ex.getNextException();
    // }
    // } catch (IOException ex) {
    // ex.printStackTrace();
    // }
    // return cbs;
    // }
    // public ArrayList<PatientBean> queryCust() {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> cbs = new ArrayList<PatientBean>();
    // PatientBean cb = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // cb = new PatientBean(rs.getString(1), rs.getString(2), rs.getString(3),
    // rs.getInt(4));
    // cbs.add(cb);
    // }
    // pStmnt.close();
    // cnnct.close();
    // } catch (SQLException ex) {
    // while (ex != null) {
    // ex.printStackTrace();
    // ex = ex.getNextException();
    // }
    // } catch (IOException ex) {
    // ex.printStackTrace();
    // }
    // return cbs;
    // }
    public boolean delRecord(String custId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        boolean isSuccess = false;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DELETE FROM patient WHERE CUSTID = ?";
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

    // public int editRecord(PatientBean cb) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "UPDATE patient SET NAME = ?, TEL = ?, AGE = ?
    // WHERE CUSTID = ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, cb.getName());
    // pStmnt.setString(2, cb.getTel());
    // pStmnt.setInt(3, cb.getAge());
    // pStmnt.setString(4, cb.getCustId());
    //
    // int rs = pStmnt.executeUpdate();
    // pStmnt.close();
    // cnnct.close();
    // return rs;
    // } catch (SQLException ex) {
    // while (ex != null) {
    // ex.printStackTrace();
    // ex = ex.getNextException();
    // }
    // } catch (IOException ex) {
    // ex.printStackTrace();
    // }
    // return 0;
    // }
    public void dropCustTable() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DROP TABLE patient";
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

    public AppointmentBean reseltSetToBean(ResultSet rs) throws SQLException {
        AppointmentBean cb = new AppointmentBean();
        cb.setAppId(rs.getInt(1));
        cb.setPatientId(rs.getInt(2));
        cb.setClinicId(rs.getInt(3));
        cb.setServiceId(rs.getInt(4));
        cb.setAppDate(rs.getDate(5));
        cb.setTimeslot(rs.getTime(6));
        cb.setStatus(rs.getString(7));
        cb.setCancelReason(rs.getString(8));
        return cb;
    }
}
