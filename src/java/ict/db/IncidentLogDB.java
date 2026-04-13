/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.db;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import ict.bean.IncidentLogBean;

/**
 *
 * @author 240708635
 */
public class IncidentLogDB {

    private String url = "";
    private String username = "";
    private String password = "";

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
                    + "FOREIGN KEY (userId) REFERENCES users(userId)"
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

    public ArrayList<IncidentLogBean> queryCust() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<IncidentLogBean> ibs = new ArrayList<>();

        IncidentLogBean ib = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM patient";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ib = this.reseltSetToBean(rs);
                ibs.add(ib);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return ibs;
    }

    // public PatientBean queryCustByID(String id) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // PatientBean ib = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE CUSTID = ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, id);
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // if (rs.next()) {
    // ib = reseltSetToBean(rs);
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
    // return ib;
    // }
    // public ArrayList<PatientBean> queryCustByName(String name) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> ibs = new ArrayList<>();
    // PatientBean ib = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE NAME LIKE ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, "%" + name + "%");
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // ib = new PatientBean();
    // ib.setCustId(rs.getString(1));
    // ib.setName(rs.getString(2));
    // ib.setTel(rs.getString(3));
    // ib.setAge(rs.getInt(4));
    // ibs.add(ib);
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
    // return ibs;
    // }
    // public ArrayList<PatientBean> queryCustByTel(String tel) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> ibs = new ArrayList<PatientBean>();
    // PatientBean ib = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE TEL LIKE ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, "%" + tel + "%");
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // ib = new PatientBean();
    // ib.setCustId(rs.getString(1));
    // ib.setName(rs.getString(2));
    // ib.setTel(rs.getString(3));
    // ib.setAge(rs.getInt(4));
    // ibs.add(ib);
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
    // return ibs;
    // }
    // public ArrayList<PatientBean> queryCust() {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> ibs = new ArrayList<PatientBean>();
    // PatientBean ib = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // ib = new PatientBean(rs.getString(1), rs.getString(2), rs.getString(3),
    // rs.getInt(4));
    // ibs.add(ib);
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
    // return ibs;
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

    // public int editRecord(PatientBean ib) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "UPDATE patient SET NAME = ?, TEL = ?, AGE = ?
    // WHERE CUSTID = ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, ib.getName());
    // pStmnt.setString(2, ib.getTel());
    // pStmnt.setInt(3, ib.getAge());
    // pStmnt.setString(4, ib.getCustId());
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

    public IncidentLogBean reseltSetToBean(ResultSet rs) throws SQLException {
        IncidentLogBean ib = new IncidentLogBean();
        ib.setLogId(rs.getInt(1));
        ib.setUserId(rs.getInt(2));
        ib.setEventType(rs.getString(3));
        ib.setDescription(rs.getString(4));
        ib.setCreatedAt(rs.getTimestamp(5));
        return ib;
    }
}
