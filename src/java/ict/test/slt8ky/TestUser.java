package ict.test.slt8ky;

import java.util.ArrayList;

import ict.bean.UserBean;
import ict.bean.UserBean.Gender;
import ict.db.User;

public class TestUser {
    public static void main(String[] arg) {
        try {
            User user = new User();
            
            /* queryUser */
            ArrayList<UserBean> arr = user.queryUser();
            if (arr != null) {
                for (UserBean bean : arr) {
                    System.out.println(bean.getUser_id());
                }
            }

            /* queryCustByID */
            UserBean bean = user.queryUserByID(1);
            if (bean != null) {
                System.out.println(bean.getUser_id());
            }

            /* queryCustByID */
            System.out.println(user.addUser(new UserBean("test", "test", "test", "test", "test@gmail.com", Gender.MALE, 1)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}