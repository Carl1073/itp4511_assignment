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

    // -1 is error, 0 is success, 1 is no user, 2 is incorrect password
    public LoginResult isValidUser(String user, String pwd) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (Connection cnnct = getConnection();
                PreparedStatement pStmnt = cnnct.prepareStatement(sql)) {

            pStmnt.setString(1, user);

            try (ResultSet rs = pStmnt.executeQuery()) {
                if (rs.next()) {
                    // User exists, check password
                    if (rs.getString("password").equals(pwd)) {
                        // Success! Convert the current row to a bean
                        UserBean bean = reseltSetToBean(rs);
                        return new LoginResult(0, bean);
                    } else {
                        return new LoginResult(2, null); // Wrong password
                    }
                } else {
                    return new LoginResult(1, null); // User not found
                }
            }
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            return new LoginResult(-1, null); // System error
        }
    }

    public int getLargestCustId() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        int largestCustId = 0;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT MAX(column_name) AS largest_value FROM users;";
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
            String sql = "CREATE TABLE IF NOT EXISTS users ("
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
            String preQueryStatement = "INSERT INTO users (username, password, fullName, email, phone, gender, role, clinicId) VALUES (?,?,?,?,?,?,?,?)";
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

    public boolean isUsernameTaken(String username) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isRepeated = false;

        UserBean ub = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM users WHERE username = ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, username);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            if (rs.next()) {
                isRepeated = true;
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
        return isRepeated;
    }

    public ArrayList<UserBean> queryCust() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<UserBean> ubs = new ArrayList<>();

        UserBean ub = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM users";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ub = this.reseltSetToBean(rs);
                ubs.add(ub);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return ubs;
    }

    // public PatientBean queryCustByID(String id) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // PatientBean ub = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE CUSTID = ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, id);
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // if (rs.next()) {
    // ub = reseltSetToBean(rs);
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
    // return ub;
    // }
    // public ArrayList<PatientBean> queryCustByName(String name) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> ubs = new ArrayList<>();
    // PatientBean ub = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE NAME LIKE ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, "%" + name + "%");
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // ub = new PatientBean();
    // ub.setCustId(rs.getString(1));
    // ub.setName(rs.getString(2));
    // ub.setTel(rs.getString(3));
    // ub.setAge(rs.getInt(4));
    // ubs.add(ub);
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
    // return ubs;
    // }
    // public ArrayList<PatientBean> queryCustByTel(String tel) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> ubs = new ArrayList<PatientBean>();
    // PatientBean ub = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE TEL LIKE ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, "%" + tel + "%");
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // ub = new PatientBean();
    // ub.setCustId(rs.getString(1));
    // ub.setName(rs.getString(2));
    // ub.setTel(rs.getString(3));
    // ub.setAge(rs.getInt(4));
    // ubs.add(ub);
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
    // return ubs;
    // }
    // public ArrayList<PatientBean> queryCust() {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> ubs = new ArrayList<PatientBean>();
    // PatientBean ub = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // ub = new PatientBean(rs.getString(1), rs.getString(2), rs.getString(3),
    // rs.getInt(4));
    // ubs.add(ub);
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
    // return ubs;
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

    // public int editRecord(PatientBean ub) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "UPDATE patient SET NAME = ?, TEL = ?, AGE = ?
    // WHERE CUSTID = ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, ub.getName());
    // pStmnt.setString(2, ub.getTel());
    // pStmnt.setInt(3, ub.getAge());
    // pStmnt.setString(4, ub.getCustId());
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

    public UserBean reseltSetToBean(ResultSet rs) throws SQLException {
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
