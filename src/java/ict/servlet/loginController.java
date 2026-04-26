/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.servlet;

import ict.bean.UserBean;
import ict.db.UserDB;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Tong
 */
@WebServlet(name = "loginController", urlPatterns = {"/loginController"})
public class loginController extends HttpServlet {

    private UserDB db;

    @Override
    public void init() {
        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");
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
        String role = request.getParameter("role");
        System.out.println(action + ", " + role);

        if (!isAuthenticated(request) && !("authenticate".equalsIgnoreCase(action))) {
            System.out.println("test");
            doLogin(request, response);
            return;
        }
        if ("authenticate".equalsIgnoreCase(action)) {
            System.out.println("is doing authenticate");
            doAuthenticate(request, response);
        } else if ("logout".equalsIgnoreCase(action)) {
            doLogout(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
        }
    }

    private void doAuthenticate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String selectedRole = request.getParameter("role"); // The role the user claims to be
        String targetURL = "";
        HttpSession session = request.getSession(true);

        UserBean ub = db.queryUserByUsername(username);
        String errorMsg = "";

        if (ub == null) {
            errorMsg = "Username not registered. Please register.";
        } else if (!ub.getPassword().equals(password)) {
            errorMsg = "Password incorrect. Please check the password.";
        } else {
            // --- Role Hierarchy Logic ---
            String actualRole = ub.getRole().toLowerCase();
            String requestedRole = selectedRole.toLowerCase();
            boolean isAuthorized = false;

            if (actualRole.equals("admin")) {
                // Admin can enter as Admin, Staff, or Patient
                isAuthorized = true;
            } else if (actualRole.equals("staff")) {
                // Staff can enter as Staff or Patient, but not Admin
                if (requestedRole.equals("staff") || requestedRole.equals("patient")) {
                    isAuthorized = true;
                }
            } else if (actualRole.equals("patient")) {
                // Patient can only enter as Patient
                if (requestedRole.equals("patient")) {
                    isAuthorized = true;
                }
            }

            if (!isAuthorized) {
                errorMsg = "Access Denied. You do not have the permissions for the " + selectedRole + " role.";
            }
        }

        if (errorMsg.equals("")) {
            session.setAttribute("userBean", ub);
            // Redirect based on the role they SELECTED to log in as
            if (selectedRole.equalsIgnoreCase("patient")) {
                targetURL = "patient/patientHome.jsp";
            } else if (selectedRole.equalsIgnoreCase("staff")) {
                targetURL = "staff/staffHome.jsp";
            } else { // admin
                targetURL = "admin/adminHome.jsp";
            }
        } else {
            request.setAttribute("errorMsg", errorMsg);
            targetURL = "login.jsp";
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher("/" + targetURL);
        rd.forward(request, response);
    }

    private boolean isAuthenticated(HttpServletRequest request) {
        boolean result = false;
        HttpSession session = request.getSession();

        if (session.getAttribute("userBean") != null) {
            result = true;
        }
        return result;
    }

    private void doLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String targetURL = "login.jsp";
        RequestDispatcher rd;
        rd = getServletContext().getRequestDispatcher("/" + targetURL);
        rd.forward(request, response);
    }

    private void doLogout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute("patientBean");
            session.invalidate();
        }
        doLogin(request, response);
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
