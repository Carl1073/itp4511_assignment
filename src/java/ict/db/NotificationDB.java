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

/**
 *
 * @author 240708635
 */
public class NotificationDB {

    private String url = "";
    private String username = "";
    private String password = "";

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
            String preQueryStatement = "INSERT INTO notification (userId, message, isRead) VALUES (?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, nb.getUserId());
            pStmnt.setString(2, nb.getMessage());
            pStmnt.setBoolean(3, nb.getIsRead());
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
    
    public ArrayList<NotificationBean> queryCust() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<NotificationBean> nbs = new ArrayList<>();

        NotificationBean nb = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM patient";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                nb = this.reseltSetToBean(rs);
                nbs.add(nb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return nbs;
    }

    // public PatientBean queryCustByID(String id) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // PatientBean nb = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE CUSTID = ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, id);
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // if (rs.next()) {
    // nb = reseltSetToBean(rs);
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
    // return nb;
    // }
    // public ArrayList<PatientBean> queryCustByName(String name) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> nbs = new ArrayList<>();
    // PatientBean nb = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE NAME LIKE ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, "%" + name + "%");
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // nb = new PatientBean();
    // nb.setCustId(rs.getString(1));
    // nb.setName(rs.getString(2));
    // nb.setTel(rs.getString(3));
    // nb.setAge(rs.getInt(4));
    // nbs.add(nb);
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
    // return nbs;
    // }
    // public ArrayList<PatientBean> queryCustByTel(String tel) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> nbs = new ArrayList<PatientBean>();
    // PatientBean nb = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE TEL LIKE ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, "%" + tel + "%");
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // nb = new PatientBean();
    // nb.setCustId(rs.getString(1));
    // nb.setName(rs.getString(2));
    // nb.setTel(rs.getString(3));
    // nb.setAge(rs.getInt(4));
    // nbs.add(nb);
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
    // return nbs;
    // }
    // public ArrayList<PatientBean> queryCust() {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> nbs = new ArrayList<PatientBean>();
    // PatientBean nb = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // nb = new PatientBean(rs.getString(1), rs.getString(2), rs.getString(3),
    // rs.getInt(4));
    // nbs.add(nb);
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
    // return nbs;
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

    // public int editRecord(PatientBean nb) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "UPDATE patient SET NAME = ?, TEL = ?, AGE = ?
    // WHERE CUSTID = ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, nb.getName());
    // pStmnt.setString(2, nb.getTel());
    // pStmnt.setInt(3, nb.getAge());
    // pStmnt.setString(4, nb.getCustId());
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

    public NotificationBean reseltSetToBean(ResultSet rs) throws SQLException {
        NotificationBean nb = new NotificationBean();
        nb.setNotifId(rs.getInt(1));
        nb.setUserId(rs.getInt(2));
        nb.setMessage(rs.getString(3));
        nb.setIsRead(rs.getBoolean(4));
        nb.setCreatedAt(rs.getTimestamp(5));
        return nb;
    }
}
