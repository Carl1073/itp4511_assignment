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
public class TimeslotDB {

    private String url = "";
    private String username = "";
    private String password = "";
    private String query = "SELECT t.*, cl.clinicName, s.serviceName "
            + "FROM timeslot t "
            + "LEFT JOIN clinic cl ON t.clinicId = cl.clinicId "
            + "LEFT JOIN service s ON t.serviceId = s.serviceId ";

    public TimeslotDB(String url, String username, String password) {
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
            String sql = "CREATE TABLE IF NOT EXISTS timeslot ("
                    + "timeslotId INT NOT NULL AUTO_INCREMENT,"
                    + "clinicId INT NOT NULL,"
                    + "serviceId INT NOT NULL,"
                    + "quotaPerSlot INT NOT NULL DEFAULT 2," // Max capacity for booking
                    + "date DATE default (CURRENT_DATE)," // Date of the service, default to current date
                    + "openTime TIME NOT NULL,"
                    + "PRIMARY KEY (timeslotId),"
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

    public boolean addRecord(TimeslotBean tb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "INSERT INTO timeslot (clinicId, serviceId, quotaPerSlot, date, openTime) VALUES (?,?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, tb.getClinicId());
            pStmnt.setInt(2, tb.getServiceId());
            pStmnt.setInt(3, tb.getQuotaPerSlot());
            pStmnt.setDate(4, tb.getDate());
            pStmnt.setTime(5, tb.getOpenTime());
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

    private ArrayList<TimeslotBean> executeGenericQuery(String sql, Object... params) {
        ArrayList<TimeslotBean> tbs = new ArrayList<>();
        // Try-with-resources automatically closes Connection, Statement, and ResultSet
        try (Connection cnnct = getConnection();
                PreparedStatement pStmnt = cnnct.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pStmnt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pStmnt.executeQuery()) {
                while (rs.next()) {
                    tbs.add(this.resultSetToBean(rs));
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return tbs;
    }

    public ArrayList<TimeslotBean> queryTimeslot() {
        return executeGenericQuery(query);
    }

    public ArrayList<TimeslotBean> queryTimeslotbyDate(Date date) {
        return executeGenericQuery(query + "where date = ? ", date);
    }

    public ArrayList<TimeslotBean> queryTimeslotbyDateClinicService(Date date, int clinicId, int serviceId) {
        return executeGenericQuery(query + "where date = ? and t.clinicId = ? and t.serviceId = ?", date, clinicId, serviceId);
    }

    public ArrayList<TimeslotBean> queryAvailableTimeslots(Date date, int clinicId, int serviceId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        try {
            cnnct = getConnection();
            // SQL to select timeslot details and calculate remaining quota by subtracting active appointments
            String preQueryStatement
                    = "SELECT t.*, (t.quotaPerSlot - COUNT(a.appId)) AS remaining "
                    + "FROM timeslot t "
                    + "LEFT JOIN appointment a ON t.timeslotId = a.timeslotId AND a.status != 'Cancelled' "
                    + "WHERE t.date = ? AND t.clinicId = ? AND t.serviceId = ? "
                    + "GROUP BY t.timeslotId";

            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setDate(1, date);
            pStmnt.setInt(2, clinicId);
            pStmnt.setInt(3, serviceId);

            ResultSet rs = pStmnt.executeQuery();
            ArrayList<TimeslotBean> list = new ArrayList<TimeslotBean>();

            while (rs.next()) {
                TimeslotBean tb = new TimeslotBean();
                tb.setTimeslotId(rs.getInt("timeslotId"));
                tb.setClinicId(rs.getInt("clinicId"));
                tb.setServiceId(rs.getInt("serviceId"));
                tb.setQuotaPerSlot(rs.getInt("quotaPerSlot"));
                tb.setDate(rs.getDate("date"));
                tb.setOpenTime(rs.getTime("openTime"));
                tb.setRemaining(rs.getInt("remaining"));

                list.add(tb);
            }
            return list;

        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (pStmnt != null) {
                try {
                    pStmnt.close();
                } catch (SQLException e) {
                }
            }
            if (cnnct != null) {
                try {
                    cnnct.close();
                } catch (SQLException sqlEx) {
                }
            }
        }
        return null;
    }

    public boolean delRecord(int timeslotId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        boolean isSuccess = false;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DELETE FROM timeslot WHERE timeslotId = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, timeslotId);

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

    public int editRecord(TimeslotBean tb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE timeslot SET quotaPerSlot = ?, DURATION = ? WHERE CLINICID = ? and SERVICEID = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, tb.getQuotaPerSlot());
            pStmnt.setInt(2, tb.getClinicId());
            pStmnt.setInt(3, tb.getServiceId());

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
            String preQueryStatement = "DROP TABLE timeslot";
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

    public TimeslotBean resultSetToBean(ResultSet rs) throws SQLException {
        TimeslotBean tb = new TimeslotBean();
        tb.setTimeslotId(rs.getInt(1));
        tb.setClinicId(rs.getInt(2));
        tb.setServiceId(rs.getInt(3));
        tb.setQuotaPerSlot(rs.getInt(4));
        tb.setDate(rs.getDate(5));
        tb.setOpenTime(rs.getTime(6));

        ClinicBean cb = new ClinicBean();
        cb.setClinicName(rs.getString("clinicName"));
//        cb.setAddress(rs.getString("address"));
//        cb.setOpenTime(rs.getTime("openTime"));
//        cb.setCloseTime(rs.getTime("closeTime"));
//        cb.setIsWalkinEnabled(rs.getBoolean("isWalkinEnabled"));
        tb.setClinicBean(cb);

        ServiceBean sb = new ServiceBean();
        sb.setServiceName(rs.getString("serviceName"));
//        sb.setDescription(rs.getString("description"));
//        sb.setPrice(rs.getDouble("price"));
        tb.setServiceBean(sb);

        return tb;
    }
}
