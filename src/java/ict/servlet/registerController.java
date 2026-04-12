/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.servlet;

import ict.bean.PatientBean;
import ict.db.PatientDB;
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

    private PatientDB db;

    @Override
    public void init() {
        String dbUser = "root";
        String dbPassword = "";
        String dbUrl = "jdbc:mysql://localhost:3306/ITP4511_Assignment_DB";
        db = new PatientDB(dbUrl, dbUser, dbPassword);
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
        // 1. Wrap parameters into a bean immediately
        PatientBean cb = new PatientBean(
                db.getLargestCustId() + 1,
                request.getParameter("name"),
                request.getParameter("username"),
                request.getParameter("pw"),
                request.getParameter("gender"),
                request.getParameter("address"),
                Date.valueOf(request.getParameter("dob")),
                request.getParameter("tel"),
                request.getParameter("email")
        );

        String cpw = request.getParameter("cpw");
        StringBuilder errorMsg = new StringBuilder();

        // 2. Validation
        if (db.isUsernameTaken(cb.getUsername())) {
            errorMsg.append("Username is already taken.<br/>");
        }
        if (!cb.getPassword().equals(cpw)) {
            errorMsg.append("Passwords do not match.<br/>");
        }

        // 3. Logic Flow
        if (errorMsg.length() > 0) {
            request.setAttribute("errorMsg", errorMsg.toString());
            request.setAttribute("tempCustomer", cb); // Just send the whole bean back
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        } else {
            db.addRecord(cb);
            request.getSession().setAttribute("patientBean", cb);
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
