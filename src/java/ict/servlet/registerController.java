/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.servlet;

import ict.bean.CustomerBean;
import ict.db.CustomerDB;
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

    private CustomerDB db;

    @Override
    public void init() {
        String dbUser = "root";
        String dbPassword = "";
        String dbUrl = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
        db = new CustomerDB(dbUrl, dbUser, dbPassword);
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
        String name = request.getParameter("name");
        String username = request.getParameter("username");
        String pw = request.getParameter("pw");
        String cpw = request.getParameter("cpw");
        String gender = request.getParameter("gender");
        String address = request.getParameter("address");
        Date dob = Date.valueOf(request.getParameter("dob"));
        String tel = request.getParameter("tel");
        String email = request.getParameter("email");

        String errorMsg = "";
        // Check duplicate username
        if (db.isUsernameTaken(username)) {
            errorMsg += "Username is already taken. Please choose another.<br/>";
        }
        //Check the pw and cpw is match
        System.out.println(pw);
        System.out.println(cpw);

        if (!pw.equals(cpw)) {
            errorMsg += "Password is not match.<br/>";
        }
        if (!"".equals(errorMsg)) {
            request.setAttribute("errorMsg", errorMsg);
            request.setAttribute("name", name); // preserve input
            request.setAttribute("username", username);
            request.setAttribute("pw", pw);
            request.setAttribute("cpw", cpw);
            request.setAttribute("gender", gender);
            request.setAttribute("address", address);
            request.setAttribute("dob", dob);
            request.setAttribute("tel", tel);
            request.setAttribute("email", email);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        } else {
            HttpSession session = request.getSession(true);
            CustomerBean cb = new CustomerBean(db.getLargestCustId() + 1, name, username, pw, gender, address, dob, tel, email);
            db.addRecord(cb);
            session.setAttribute("customerBean", cb);
            request.setAttribute("username", username);
            request.setAttribute("pw", pw);
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
