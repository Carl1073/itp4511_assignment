/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.controller.auth;

import ict.bean.UserBean;
import ict.database.User;
import ict.util.Command;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author slt8ky
 */
public class Login extends Command {

    public Login(HttpServletRequest req, HttpServletResponse res) {
        super(req, res);
    }

    @Override
    public void execute() {
        try {
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            UserBean user = User.queryUserByName(username);

            if (user == null) {
                req.setAttribute("msg", "User not found!");
                req.getRequestDispatcher("login.jsp").forward(req, res);
                return;
            }

            if (!password.equals(user.getPassword())) {
                req.setAttribute("msg", "Incorrect password!");
                req.getRequestDispatcher("login.jsp").forward(req, res);
                return;
            }

            HttpSession session = req.getSession();
            session.setAttribute("current_user", user);
            session.setMaxInactiveInterval(30 * 60);
            res.sendRedirect("home.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
