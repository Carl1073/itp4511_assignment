// Old version
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.db;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import ict.bean.PatientBean;

/**
 *
 * @author 240708635
 */
public class PatientDB {

    private String url = "";
    private String username = "";
    private String password = "";

    public PatientDB(String url, String username, String password) {
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
        String sql = "SELECT * FROM patient WHERE username = ?";

        try (Connection cnnct = getConnection();
                PreparedStatement pStmnt = cnnct.prepareStatement(sql)) {

            pStmnt.setString(1, user);

            try (ResultSet rs = pStmnt.executeQuery()) {
                if (rs.next()) {
                    // User exists, check password
                    if (rs.getString("password").equals(pwd)) {
                        // Success! Convert the current row to a bean
                        PatientBean bean = reseltSetToBean(rs);
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
            String preQueryStatement = "SELECT MAX(column_name) AS largest_value FROM patient;";
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

    public void createCustTable() {
        Statement stmnt = null;
        Connection cnnct = null;

        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS patient ("
                    + "custId int NOT NULL AUTO_INCREMENT,"
                    + "name varchar(50) NOT NULL,"
                    + "username varchar(50) NOT NULL,"
                    + "password varchar(50) NOT NULL,"
                    + "gender varchar(1) NOT NULL,"
                    + "address varchar(100) NOT NULL,"
                    + "dob date NULL,"
                    + "tel varchar(10) NULL,"
                    + "email varchar(50) NULL,"
                    + "PRIMARY KEY (custId)"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean addRecord(PatientBean cb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "INSERT INTO patient (name, username, password, gender, address, dob, tel, email) VALUES (?,?,?,?,?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, cb.getName());
            pStmnt.setString(2, cb.getUsername());
            pStmnt.setString(3, cb.getPassword());
            pStmnt.setString(4, cb.getGender());
            pStmnt.setString(5, cb.getAddress());
            pStmnt.setDate(6, cb.getDob());
            pStmnt.setString(7, cb.getTel());
            pStmnt.setString(8, cb.getEmail());
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

        PatientBean cb = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM patient WHERE username = ?";
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

    public ArrayList<PatientBean> queryCust() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ArrayList<PatientBean> cbs = new ArrayList<>();

        PatientBean cb = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "SELECT * FROM patient";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                cb = this.reseltSetToBean(rs);
                cbs.add(cb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
        }
        return cbs;
    }

    // public PatientBean queryCustByID(String id) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // PatientBean cb = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE CUSTID = ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, id);
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // if (rs.next()) {
    // cb = reseltSetToBean(rs);
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
    // return cb;
    // }
    // public ArrayList<PatientBean> queryCustByName(String name) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> cbs = new ArrayList<>();
    // PatientBean cb = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE NAME LIKE ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, "%" + name + "%");
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // cb = new PatientBean();
    // cb.setCustId(rs.getString(1));
    // cb.setName(rs.getString(2));
    // cb.setTel(rs.getString(3));
    // cb.setAge(rs.getInt(4));
    // cbs.add(cb);
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
    // return cbs;
    // }
    // public ArrayList<PatientBean> queryCustByTel(String tel) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> cbs = new ArrayList<PatientBean>();
    // PatientBean cb = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient WHERE TEL LIKE ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, "%" + tel + "%");
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // cb = new PatientBean();
    // cb.setCustId(rs.getString(1));
    // cb.setName(rs.getString(2));
    // cb.setTel(rs.getString(3));
    // cb.setAge(rs.getInt(4));
    // cbs.add(cb);
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
    // return cbs;
    // }
    // public ArrayList<PatientBean> queryCust() {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // ArrayList<PatientBean> cbs = new ArrayList<PatientBean>();
    // PatientBean cb = null;
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "SELECT * FROM patient";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // ResultSet rs = null;
    // rs = pStmnt.executeQuery();
    // while (rs.next()) {
    // cb = new PatientBean(rs.getString(1), rs.getString(2), rs.getString(3),
    // rs.getInt(4));
    // cbs.add(cb);
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
    // return cbs;
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

    // public int editRecord(PatientBean cb) {
    // Connection cnnct = null;
    // PreparedStatement pStmnt = null;
    //
    // try {
    // cnnct = getConnection();
    // String preQueryStatement = "UPDATE patient SET NAME = ?, TEL = ?, AGE = ?
    // WHERE CUSTID = ?";
    // pStmnt = cnnct.prepareStatement(preQueryStatement);
    // pStmnt.setString(1, cb.getName());
    // pStmnt.setString(2, cb.getTel());
    // pStmnt.setInt(3, cb.getAge());
    // pStmnt.setString(4, cb.getCustId());
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

    public PatientBean reseltSetToBean(ResultSet rs) throws SQLException {
        PatientBean cb = new PatientBean();
        cb.setPatId(rs.getInt(1));
        cb.setName(rs.getString(2));
        cb.setUsername(rs.getString(3));
        cb.setPassword(rs.getString(4));
        cb.setGender(rs.getString(5));
        cb.setAddress(rs.getString(6));
        cb.setDob(rs.getDate(7));
        cb.setTel(rs.getString(8));
        cb.setEmail(rs.getString(9));
        return cb;
    }
}
