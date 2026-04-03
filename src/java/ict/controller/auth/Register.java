/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.controller.auth;

import ict.bean.UserBean;
import ict.bean.UserBean.Gender;
import ict.database.User;
import ict.util.Command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 *
 * @author slt8ky
 */
public class Register extends Command {

    public Register(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    @Override
    public void execute() {
        try {
            UserBean user = new UserBean(req.getParameter("username"),
                    req.getParameter("password"), req.getParameter("first_name"), req.getParameter("last_name"),
                    req.getParameter("email"), Gender.fromValue(req.getParameter("gender")), 1);
            Boolean isSuccessed = User.addUser(user);
            System.out.println(isSuccessed);
            if (isSuccessed) {
                HttpSession session = req.getSession();
                session.setAttribute("current_user", User.queryUserByName(user.getUsername()));
                session.setMaxInactiveInterval(30 * 60);
                res.setContentType("application/json");
                JSONObject json = new JSONObject();
                json.put("success", isSuccessed);
                out.print(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
