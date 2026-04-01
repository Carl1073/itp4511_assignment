package ict.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import ict.bean.UserBean;
import ict.bean.UserBean.Gender;

public class User extends Database {
    public ArrayList<UserBean> queryUser() throws Exception {
        Connection cnnct = getConnection();
        PreparedStatement pStmnt = null;
        UserBean bean = null;
        ArrayList<UserBean> arr = new ArrayList<>();
        cnnct = getConnection();
        String preQueryStatement = "select * from user";
        pStmnt = cnnct.prepareStatement(preQueryStatement);
        ResultSet rs = null;
        rs = pStmnt.executeQuery();
        while (rs.next()) {
            bean = new UserBean(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6),
                    Gender.fromChar(rs.getString(7).charAt(0)), rs.getInt(8));
            arr.add(bean);
        }
        pStmnt.close();
        cnnct.close();
        return arr;
    }

    public UserBean queryUserByID(int user_id) throws Exception {
        Connection cnnct = getConnection();
        PreparedStatement pStmnt = null;
        UserBean bean = null;
        cnnct = getConnection();
        String preQueryStatement = "select * from user where user_id = ?";
        pStmnt = cnnct.prepareStatement(preQueryStatement);
        pStmnt.setInt(1, user_id);
        ResultSet rs = null;
        rs = pStmnt.executeQuery();
        if (rs.next()) {
            bean = new UserBean(rs.getInt(1), rs.getString(2), rs.getString(3),
                    rs.getString(4), rs.getString(5), rs.getString(6),
                    Gender.fromChar(rs.getString(7).charAt(0)), rs.getInt(8));
        }
        pStmnt.close();
        cnnct.close();
        return bean;
    }

    public boolean addUser(UserBean bean) throws Exception {
        Connection cnnct = getConnection();
        PreparedStatement pStmnt = null;
        String preQueryStatement = "insert into user values (null, ?, ?, ?, ?, ?, ?, ?)";
        pStmnt = cnnct.prepareStatement(preQueryStatement);
        pStmnt.setString(1, bean.getUsername());
        pStmnt.setString(2, bean.getPassword());
        pStmnt.setString(3, bean.getFirst_name());
        pStmnt.setString(4, bean.getLast_name());
        pStmnt.setString(5, bean.getEmail());
        pStmnt.setString(6, String.valueOf(bean.getGender().code));
        pStmnt.setInt(7, bean.getRole_id());
        int rowCount = pStmnt.executeUpdate();
        pStmnt.close();
        cnnct.close();
        return rowCount >= 1;
    }

    public boolean delUser(UserBean bean) throws Exception {
        Connection cnnct = getConnection();
        PreparedStatement pStmnt = null;
        cnnct = getConnection();
        String preQueryStatement = "remove from user where user_id = ?";
        pStmnt = cnnct.prepareStatement(preQueryStatement);
        pStmnt.setInt(1, bean.getUser_id());
        int rowCount = pStmnt.executeUpdate();
        pStmnt.close();
        cnnct.close();
        return rowCount >= 1;
    }
}
