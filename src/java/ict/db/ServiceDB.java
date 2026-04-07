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
import ict.ocp.ServiceType;
import ict.ocp.ServiceMode;

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

    public void createServiceTable() {
        Connection cnnct = null;
        Statement stmnt = null;

        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            // Defined custId as an INT to match the patient table's PK
            // Added FOREIGN KEY constraint to maintain referential integrity
            String sql = "CREATE TABLE IF NOT EXISTS service ("
                    + "serviceId varchar(50) NOT NULL,"
                    + "custId int NOT NULL,"
                    + "serviceDate date NOT NULL,"
                    + "serviceTime time NOT NULL,"
                    + "location varchar(100) NOT NULL,"
                    + "serviceType varchar(50) NOT NULL,"
                    + "serviceMode varchar(50) NOT NULL,"
                    + "status varchar(20) NOT NULL,"
                    + "PRIMARY KEY (serviceId),"
                    + "FOREIGN KEY (custId) REFERENCES patient(custId)"
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
            String sql = "INSERT INTO service (serviceId, custId, serviceDate, serviceTime, location, serviceType, serviceMode, status) VALUES (?,?,?,?,?,?,?,?)";
            pStmnt = cnnct.prepareStatement(sql);
            pStmnt.setString(1, sb.getServiceId());
            // Parsed String _custId from ServiceBean to int for the DB
            pStmnt.setInt(2, Integer.parseInt(sb.get_custId()));
            pStmnt.setDate(3, sb.getDate());
            pStmnt.setTime(4, sb.getTime());
            pStmnt.setString(5, sb.getLocation());
            pStmnt.setString(6, sb.getServiceType().toString());
            pStmnt.setString(7, sb.getServiceMode().toString());
            pStmnt.setString(8, sb.getStatus());

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

    public ArrayList<ServiceBean> queryService() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<ServiceBean> services = new ArrayList<>();

        try {
            cnnct = getConnection();
            String sql = "SELECT * FROM service";
            pStmnt = cnnct.prepareStatement(sql);
            ResultSet rs = pStmnt.executeQuery();

            while (rs.next()) {
                services.add(resultSetToBean(rs));
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return services;
    }

    public ArrayList<ServiceBean> queryServiceByCustId(int custId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<ServiceBean> services = new ArrayList<>();

        try {
            cnnct = getConnection();
            String sql = "SELECT * FROM service WHERE custId = ?";
            pStmnt = cnnct.prepareStatement(sql);
            pStmnt.setInt(1, custId);
            ResultSet rs = pStmnt.executeQuery();

            while (rs.next()) {
                services.add(resultSetToBean(rs));
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return services;
    }

    public boolean delRecord(String serviceId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;

        try {
            cnnct = getConnection();
            String sql = "DELETE FROM service WHERE serviceId = ?";
            pStmnt = cnnct.prepareStatement(sql);
            pStmnt.setString(1, serviceId);

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

    public ServiceBean resultSetToBean(ResultSet rs) throws SQLException {
        ServiceBean sb = new ServiceBean();
        sb.setServiceId(rs.getString("serviceId"));
        // Mapped back to the String field in ServiceBean
        sb.set_custId(String.valueOf(rs.getInt("custId")));
        sb.setDate(rs.getDate("serviceDate"));
        sb.setTime(rs.getTime("serviceTime"));
        sb.setLocation(rs.getString("location"));

        try {
            String serviceType = rs.getString("serviceType").replaceAll("\\s", "");
            Class<?> clazz = Class.forName("icp.ocp." + serviceType);
            sb.setServiceType((ServiceType) clazz.getDeclaredConstructor().newInstance());

            String serviceMode = rs.getString("serviceMode").replaceAll("\\s", "");
            clazz = Class.forName("icp.ocp." + serviceMode);
            sb.setServiceMode((ServiceMode) clazz.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.setStatus(rs.getString("status"));
        return sb;
    }
}
