/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.db;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import ict.bean.UserBean;

/**
 *
 * @author 240708635
 */
public class UserDB {

    private String url = "";
    private String username = "";
    private String password = "";
    private String query = "SELECT * FROM user ";

    public UserDB(String url, String username, String password) {
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

    public int getLargestCustId() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        int largestCustId = 0;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT MAX(userId) AS largest_value FROM user;";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            if (rs.next()) {
                largestCustId = rs.getInt("largest_value");
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return largestCustId;
    }

    public void createTable() {
        Statement stmnt = null;
        Connection cnnct = null;

        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS user ("
                    + "userId INT AUTO_INCREMENT,"
                    + "username VARCHAR(50) NOT NULL UNIQUE,"
                    + "password VARCHAR(255) NOT NULL,"
                    + "fullName VARCHAR(100) NOT NULL,"
                    + "email VARCHAR(100),"
                    + "phone VARCHAR(10),"
                    + "gender ENUM('M', 'F', 'O'),"
                    + "role ENUM('Patient', 'Staff', 'Admin') NOT NULL,"
                    + "clinicId INT,"
                    + "PRIMARY KEY (userId)"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean addRecord(UserBean ub) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "INSERT INTO user (username, password, fullName, email, phone, gender, role, clinicId) VALUES (?,?,?,?,?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, ub.getUsername());
            pStmnt.setString(2, ub.getPassword());
            pStmnt.setString(3, ub.getFullName());
            pStmnt.setString(4, ub.getEmail());
            pStmnt.setString(5, ub.getPhone());
            pStmnt.setString(6, ub.getGender());
            pStmnt.setString(7, ub.getRole());
            pStmnt.setInt(8, ub.getClinicId());
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

private ArrayList<UserBean> executeGenericQuery(String sql, Object... params) {
        ArrayList<UserBean> ubs = new ArrayList<>();
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

    public ArrayList<UserBean> queryUser() {
        return executeGenericQuery("");
    }

    public UserBean queryUserByID(String id) {
        ArrayList<UserBean> results = executeGenericQuery(" WHERE userid = ?", id);
        return results.isEmpty() ? null : results.get(0);
    }

    public ArrayList<UserBean> queryUserByName(String name) {
        return executeGenericQuery(" WHERE fullName LIKE ?", "%" + name + "%");
    }

    public UserBean queryUserByUsername(String username) {
        ArrayList<UserBean> results = executeGenericQuery(" WHERE username LIKE ?", "%" + username + "%");
        return results.isEmpty() ? null : results.get(0);
    }

    public ArrayList<UserBean> queryUserByPhone(String phone) {
        return executeGenericQuery(" WHERE phone LIKE ?", "%" + phone + "%");
    }

    public boolean delRecord(int custId) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        boolean isSuccess = false;

        try {
            cnnct = getConnection();
            String preQueryStatement = "DELETE FROM user WHERE userid = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setInt(1, custId);

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

    public int editRecord(UserBean ub) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;

        try {
            cnnct = getConnection();
            String preQueryStatement = "UPDATE user SET username = ?, password = ?, fullName = ?, email = ?, phone = ?, gender = ?, role = ?, clinicId = ? WHERE userid =  ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, ub.getUsername());
            pStmnt.setString(2, ub.getPassword());
            pStmnt.setString(3, ub.getFullName());
            pStmnt.setString(4, ub.getEmail());
            pStmnt.setString(5, ub.getPhone());
            pStmnt.setString(6, ub.getGender());
            pStmnt.setString(7, ub.getRole());
            pStmnt.setInt(8, ub.getClinicId());
            pStmnt.setInt(9, ub.getUserId());

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
            String preQueryStatement = "DROP TABLE user";
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

    public UserBean resultSetToBean(ResultSet rs) throws SQLException {
        UserBean ub = new UserBean();
        ub.setUserId(rs.getInt(1));
        ub.setUsername(rs.getString(2));
        ub.setPassword(rs.getString(3));
        ub.setFullName(rs.getString(4));
        ub.setEmail(rs.getString(5));
        ub.setPhone(rs.getString(6));
        ub.setGender(rs.getString(7));
        ub.setRole(rs.getString(8));
        ub.setClinicId(rs.getInt(9));
        return ub;
    }
}
