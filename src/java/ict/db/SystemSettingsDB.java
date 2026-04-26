/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.db;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import ict.bean.SystemSettingBean;

/**
 *
 * @author Tong
 */
public class SystemSettingsDB {

    private String url = "";
    private String username = "";
    private String password = "";
    private String query = "SELECT * FROM system_settings ";

    public SystemSettingsDB(String url, String username, String password) {
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
            String sql = "CREATE TABLE IF NOT EXISTS system_settings (" +
                    "setting_key VARCHAR(50) PRIMARY KEY," +
                    "setting_value VARCHAR(255)," +
                    "description TEXT," +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP" +
                    ")";
            stmnt.execute(sql);

            // Insert default setting if not exist
            String defaultKey = "max_active_bookings_per_patient";
            String defaultValue = "5";
            String defaultDesc = "Maximum number of active bookings a patient can have at any time";

            if (querySettingByKey(defaultKey) == null) {
                SystemSettingBean sb = new SystemSettingBean(defaultKey, defaultValue, defaultDesc);
                addRecord(sb);
            }

            stmnt.close();
            cnnct.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean addRecord(SystemSettingBean sb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "INSERT INTO system_settings (setting_key, setting_value, description) VALUES (?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, sb.getSettingKey());
            pStmnt.setString(2, sb.getSettingValue());
            pStmnt.setString(3, sb.getDescription());
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

    private ArrayList<SystemSettingBean> executeGenericQuery(String sql, Object... params) {
        ArrayList<SystemSettingBean> list = new ArrayList<>();
        // Try-with-resources automatically closes Connection, Statement, and ResultSet
        try (Connection cnnct = getConnection();
                PreparedStatement pStmnt = cnnct.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                pStmnt.setObject(i + 1, params[i]);
            }

            try (ResultSet rs = pStmnt.executeQuery()) {
                while (rs.next()) {
                    list.add(this.resultSetToBean(rs));
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    public ArrayList<SystemSettingBean> querySettings() {
        return executeGenericQuery(query);
    }

    public SystemSettingBean querySettingByKey(String key) {
        ArrayList<SystemSettingBean> results = executeGenericQuery(query + " WHERE setting_key = ?", key);
        return results.isEmpty() ? null : results.get(0);
    }

    public boolean delRecord(String key) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        boolean isSuccess = false;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DELETE FROM system_settings WHERE setting_key = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, key);

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

    public int editRecord(SystemSettingBean sb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();

            String sql = "UPDATE system_settings SET setting_value = ? WHERE setting_key = ?";
            pStmnt = cnnct.prepareStatement(sql);
            pStmnt.setString(1, sb.getSettingValue());
            pStmnt.setString(2, sb.getSettingKey());

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

    public void dropTable() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DROP TABLE system_settings";
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

    public SystemSettingBean resultSetToBean(ResultSet rs) throws SQLException {
        SystemSettingBean sb = new SystemSettingBean();
        sb.setSettingKey(rs.getString("setting_key"));
        sb.setSettingValue(rs.getString("setting_value"));
        sb.setDescription(rs.getString("description"));
        sb.setUpdatedAt(rs.getTimestamp("updated_at"));
        return sb;
    }
}