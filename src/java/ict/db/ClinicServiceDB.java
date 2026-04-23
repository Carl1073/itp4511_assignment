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
public class ClinicServiceDB {

    private String url = "";
    private String username = "";
    private String password = "";
    private String query = "SELECT cs.*, cl.clinicName, s.serviceName "
            + "FROM clinic_service cs "
            + "LEFT JOIN clinic cl ON cs.clinicId = cl.clinicId "
            + "LEFT JOIN service s ON cs.serviceId = s.serviceId ";

    public ClinicServiceDB(String url, String username, String password) {
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
            String sql = "CREATE TABLE IF NOT EXISTS clinic_service ("
                    + "clinicId INT NOT NULL,"
                    + "serviceId INT NOT NULL,"
                    + "quotaPerSlot INT NOT NULL DEFAULT 2," // Max capacity for booking
                    + "date DATE default today()," // Date of the service, default to current date
                    + "openTime TIME,"
                    + "duration INT NOT NULL DEFAULT 60," // Minutes per slot, default to 60 minutes
                    + "PRIMARY KEY (clinicId, serviceId),"
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

    public boolean addRecord(ClinicServiceBean cb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "INSERT INTO clinic_service (clinicId, serviceId, quotaPerSlot, duration) VALUES (?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, cb.getClinicId());
            pStmnt.setInt(2, cb.getServiceId());
            pStmnt.setInt(3, cb.getQuotaPerSlot());
            pStmnt.setInt(4, cb.getDuration());
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

    private ArrayList<ClinicServiceBean> executeGenericQuery(String sql, Object... params) {
        ArrayList<ClinicServiceBean> cbs = new ArrayList<>();
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

    public ArrayList<ClinicServiceBean> queryCS() {
        return executeGenericQuery("");
    }

    public boolean delRecord(int custId, int serviceId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        boolean isSuccess = false;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DELETE FROM clinic_service WHERE WHERE CLINICID = ? and SERVICEID = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, custId);
            pStmnt.setInt(2, serviceId);

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

    public int editRecord(ClinicServiceBean cb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE clinic_service SET quotaPerSlot = ?, DURATION = ? WHERE CLINICID = ? and SERVICEID = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, cb.getQuotaPerSlot());
            pStmnt.setInt(2, cb.getDuration());
            pStmnt.setInt(3, cb.getClinicId());
            pStmnt.setInt(4, cb.getServiceId());

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
            String preQueryStatement = "DROP TABLE clinic_service";
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

    public ClinicServiceBean resultSetToBean(ResultSet rs) throws SQLException {
        ClinicServiceBean csb = new ClinicServiceBean();
        csb.setClinicId(rs.getInt(1));
        csb.setServiceId(rs.getInt(2));
        csb.setQuotaPerSlot(rs.getInt(3));
        csb.setDuration(rs.getInt(4));

        ClinicBean cb = new ClinicBean();
        cb.setClinicName(rs.getString("clinicName"));
//        cb.setAddress(rs.getString("address"));
//        cb.setOpenTime(rs.getTime("openTime"));
//        cb.setCloseTime(rs.getTime("closeTime"));
//        cb.setIsWalkinEnabled(rs.getBoolean("isWalkinEnabled"));
        csb.setClinicBean(cb);

        ServiceBean sb = new ServiceBean();
        sb.setServiceName(rs.getString("serviceName"));
//        sb.setDescription(rs.getString("description"));
//        sb.setPrice(rs.getDouble("price"));
        csb.setServiceBean(sb);

        return csb;
    }
}
