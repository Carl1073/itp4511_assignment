package ict.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import ict.bean.UserBean;
import ict.bean.UserBean.Gender;

public class User extends Database {
    public static ArrayList<UserBean> queryUser() {
        ArrayList<UserBean> beans = new ArrayList<>();
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from user");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                beans.add(new UserBean(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        Gender.fromValue(rs.getString("gender")),
                        rs.getInt("role_id")));
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return beans;
    }

    public static UserBean queryUserById(int user_id) {
        UserBean bean = null;
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from user where user_id = ?");
            pstmt.setInt(1, user_id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = new UserBean(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        Gender.fromValue(rs.getString("gender")),
                        rs.getInt("role_id"));
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    public static UserBean queryUserByName(String username) {
        UserBean bean = null;
        try {
            Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement("select * from user where username = ?");
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                bean = new UserBean(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        Gender.fromValue(rs.getString("gender")),
                        rs.getInt("role_id"));
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bean;
    }

    public static boolean addUser(UserBean user) {
        try {
            Connection conn = getConnection();
            String sql = "insert into user (username, password, first_name, last_name, email, gender, role_id) values (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getFirst_name());
            pstmt.setString(4, user.getLast_name());
            pstmt.setString(5, user.getEmail());
            pstmt.setString(6, user.getGender().getValue());
            pstmt.setInt(7, user.getRole_id());
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean delUser(int user_id) {
        try {
            Connection conn = getConnection();
            String sql = "delete from user where user_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, user_id);
            pstmt.executeUpdate();
            pstmt.close();
            conn.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
