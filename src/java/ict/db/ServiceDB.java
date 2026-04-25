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
    private String query = "SELECT * FROM service";

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
            String sql = "CREATE TABLE IF NOT EXISTS service ("
                    + "serviceId INT AUTO_INCREMENT,"
                    + "serviceName VARCHAR(100) NOT NULL,"
                    + "description TEXT,"
                    + "price DECIMAL(10, 2) NOT NULL,"
                    + "duration INT NOT NULL DEFAULT 60," // Minutes per slot, default to 60 minutes
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
            String preQueryStatement = "INSERT INTO service (serviceName, description, price, duration) VALUES (?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, sb.getServiceName());
            pStmnt.setString(2, sb.getDescription());
            pStmnt.setDouble(3, sb.getPrice());
            pStmnt.setInt(4, sb.getDuration());
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

    private ArrayList<ServiceBean> executeGenericQuery(String sql, Object... params) {
        ArrayList<ServiceBean> ubs = new ArrayList<>();
        // Try-with-resources automatically closes Connection, Statement, and ResultSet
        try (Connection cnnct = getConnection();
                PreparedStatement pStmnt = cnnct.prepareStatement(query + sql)) {

            for (int i = 0; i < params.length; i++) {
                pStmnt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pStmnt.executeQuery()) {
                while (rs.next()) {
                    ubs.add(this.resultSetToBean(rs));
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return ubs;
    }

    public ArrayList<ServiceBean> queryService() {
        return executeGenericQuery("");
    }

    public ServiceBean queryServiceId(int serviceId) {
        ArrayList<ServiceBean> results = executeGenericQuery(" WHERE serviceId = ?", serviceId);
        return results.isEmpty() ? null : results.get(0);
    }

    public boolean delRecord(int serviceId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        boolean isSuccess = false;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DELETE FROM service WHERE SERVICEID = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, serviceId);

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

    public int editRecord(ServiceBean sb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE service SET serviceName = ?, description = ?, price = ?, duration = ? "
                    + " WHERE serviceId =  ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, sb.getServiceName());
            pStmnt.setString(2, sb.getDescription());
            pStmnt.setDouble(3, sb.getPrice());
            pStmnt.setInt(4, sb.getDuration());
            pStmnt.setInt(5, sb.getServiceId());

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
            String preQueryStatement = "DROP TABLE service";
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
        sb.setPrice(rs.getDouble(4));
        sb.setDuration(rs.getInt(5));
        return sb;
    }
}
