/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.db;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import ict.bean.ClinicBean;

/**
 *
 * @author 240708635
 */
public class ClinicDB {

    private String url = "";
    private String username = "";
    private String password = "";
    private String query = "SELECT * FROM clinic ";

    public ClinicDB(String url, String username, String password) {
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
            String sql = "CREATE TABLE IF NOT EXISTS clinic ("
                    + "clinicId INT AUTO_INCREMENT,"
                    + "clinicName VARCHAR(50) NOT NULL UNIQUE,"
                    + "address VARCHAR(255),"
                    + "openTime TIME DEFAULT '09:00:00',"
                    + "closeTime TIME DEFAULT '18:00:00',"
                    + "isWalkinEnabled BOOLEAN DEFAULT TRUE,"
                    + "PRIMARY KEY (clinicId)"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean addRecord(ClinicBean cb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "INSERT INTO clinic (clinicName, address, openTime, closeTime, isWalkinEnabled) VALUES (?,?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, cb.getClinicName());
            pStmnt.setString(2, cb.getAddress());
            pStmnt.setTime(3, cb.getOpenTime());
            pStmnt.setTime(4, cb.getCloseTime());
            pStmnt.setBoolean(5, cb.getIsWalkinEnabled());
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

    private ArrayList<ClinicBean> executeGenericQuery(String sql, Object... params) {
        ArrayList<ClinicBean> cbs = new ArrayList<>();
        // Try-with-resources automatically closes Connection, Statement, and ResultSet
        try (Connection cnnct = getConnection();
                PreparedStatement pStmnt = cnnct.prepareStatement(query + sql)) {

            for (int i = 0; i < params.length; i++) {
                pStmnt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pStmnt.executeQuery()) {
                while (rs.next()) {
                    cbs.add(this.resultSetToBean(rs));
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return cbs;
    }

    public ArrayList<ClinicBean> queryClinic() {
        return executeGenericQuery("");
    }

    public ClinicBean getClinicById(int id) {
        ArrayList<ClinicBean> results = executeGenericQuery(" WHERE clinicId = ? ", id);
        return results.isEmpty() ? null : results.get(0);
    }

// In your DAO / Database class
    public boolean delRecord(int clinicId) {
        Connection cnnct = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            cnnct.setAutoCommit(false); // Start transaction

            // 1. Delete associated timeslots first
            PreparedStatement ps1 = cnnct.prepareStatement("DELETE FROM timeslot WHERE clinicId = ?");
            ps1.setInt(1, clinicId);
            ps1.executeUpdate();

            // 2. Delete associated queue records
            PreparedStatement ps2 = cnnct.prepareStatement("DELETE FROM queue WHERE clinicId = ?");
            ps2.setInt(1, clinicId);
            ps2.executeUpdate();

            // 3. Finally, delete the clinic
            PreparedStatement ps3 = cnnct.prepareStatement("DELETE FROM clinic WHERE clinicId = ?");
            ps3.setInt(1, clinicId);

            int rowCount = ps3.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
                cnnct.commit(); // Save changes
            } else {
                cnnct.rollback();
            }
        } catch (SQLException | IOException ex) {
            if (cnnct != null) {
                try {
                    cnnct.rollback();
                } catch (SQLException e) {
                }
            }
            ex.printStackTrace();
        } finally {
            // Always close connections in finally block
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException e) {
                }
            }
        }
        return isSuccess;
    }

    public int editRecord(ClinicBean cb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE clinic SET CLINICNAME = ?, ADDRESS = ?, OPENTIME = ?, CLOSETIME = ?, ISWALKINENABLED = ? WHERE CLINICID = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, cb.getClinicName());
            pStmnt.setString(2, cb.getAddress());
            pStmnt.setTime(3, cb.getOpenTime());
            pStmnt.setTime(4, cb.getCloseTime());
            pStmnt.setBoolean(5, cb.getIsWalkinEnabled());
            pStmnt.setInt(6, cb.getClinicId());

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

    public void dropClinicTable() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DROP TABLE clinic";
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

    public ClinicBean resultSetToBean(ResultSet rs) throws SQLException {
        ClinicBean cb = new ClinicBean();
        cb.setClinicId(rs.getInt(1));
        cb.setClinicName(rs.getString(2));
        cb.setAddress(rs.getString(3));
        cb.setOpenTime(rs.getTime(4));
        cb.setCloseTime(rs.getTime(5));
        cb.setIsWalkinEnabled(rs.getBoolean(6));
        return cb;
    }
}
