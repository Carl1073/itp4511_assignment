/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.db;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import ict.bean.ServiceBean;

/**
 *
 * @author 240708635
 */
public class ServiceDB {

    private String url = "";
    private String username = "";
    private String password = "";

    public ServiceDB(String url, String username, String password) {
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
            String sql = "CREATE TABLE IF NOT EXISTS service ("
                    + "serviceId INT AUTO_INCREMENT,"
                    + "serviceName VARCHAR(100) NOT NULL,"
                    + "description TEXT,"
                    + "PRIMARY KEY (serviceId)"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean addRecord(ServiceBean sb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "INSERT INTO service (serviceName, description) VALUES (?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, sb.getServiceName());
            pStmnt.setString(2, sb.getDescription());
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

    public ArrayList<ServiceBean> queryCust() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<ServiceBean> sbs = new ArrayList<>();

        ServiceBean sb = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM patient";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                sb = this.resultSetToBean(rs);
                sbs.add(sb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return sbs;
    }


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

    // public int editRecord(PatientBean sb) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "UPDATE patient SET NAME = ?, TEL = ?, AGE = ?
    // WHERE CUSTID = ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, sb.getName());
    // pStmnt.setString(2, sb.getTel());
    // pStmnt.setInt(3, sb.getAge());
    // pStmnt.setString(4, sb.getCustId());
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

    public ServiceBean resultSetToBean(ResultSet rs) throws SQLException {
        ServiceBean sb = new ServiceBean();
        sb.setServiceId(Integer.parseInt(rs.getString(1)));
        sb.setServiceName(rs.getString(2));
        sb.setDescription(rs.getString(3));
        return sb;
    }
}
