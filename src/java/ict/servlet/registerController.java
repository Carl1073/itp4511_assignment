/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.servlet;

import ict.bean.*;
import ict.db.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.ResultSet;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Tong
 */
@WebServlet(name = "registerController", urlPatterns = {"/registerController"})
public class registerController extends HttpServlet {

    private UserDB db;

    @Override
    public void init() {
        String dbUser = "root";
        String dbPassword = "";
        String dbUrl = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
        db = new UserDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        switch (action) {
            case "register":
                doRegister(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    private void doRegister(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. Make a user bean
        UserBean ub = new UserBean(
                db.getLargestCustId(),
                request.getParameter("username"),
                request.getParameter("password"),
                request.getParameter("fullName"),
                request.getParameter("email"),
                request.getParameter("phone"),
                request.getParameter("gender"),
                request.getParameter("role"),
                0  //default no clinic assign
        );

        String cpw = request.getParameter("cpw");
        StringBuilder errorMsg = new StringBuilder();

        // 2. Validation
        if (db.queryUserByUsername(ub.getUsername()) != null) {
            errorMsg.append("Username is already taken.<br/>");
        }
        if (!ub.getPassword().equals(cpw)) {
            errorMsg.append("Passwords do not match.<br/>");
        }
        if (ub.getPassword().length() < 3) {
            errorMsg.append("Passwords length is shorter than 3.<br/>");
        }

        // 3. Logic Flow
        if (errorMsg.length() > 0) {
            request.setAttribute("errorMsg", errorMsg.toString());
            request.setAttribute("username",  ub.getUsername());
            request.setAttribute("password",  ub.getPassword());
            request.setAttribute("cpw", cpw);
            request.setAttribute("fullName",  ub.getFullName());
            request.setAttribute("email",  ub.getEmail());
            request.setAttribute("phone",  ub.getPhone());
            request.setAttribute("gender",  ub.getGender());
            request.setAttribute("role",  ub.getRole());
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } else {
            db.addRecord(ub);
            request.getSession().setAttribute("userBean", ub);
            request.getRequestDispatcher("/registerSuccess.jsp").forward(request, response);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
