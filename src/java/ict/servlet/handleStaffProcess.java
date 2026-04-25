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
import java.time.LocalDate;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Tong
 */
@WebServlet(name = "handleStaffProcess", urlPatterns = {"/handleStaffProcess"})
public class handleStaffProcess extends HttpServlet {

    private ServiceDB sdb;
    private ClinicDB cdb;
    private TimeslotDB tdb;
    private NotificationDB ndb;
    private AppointmentDB adb;
    private QueueDB qdb;
    private IncidentLogDB ildb;

    @Override
    public void init() {
        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");
        sdb = new ServiceDB(dbUrl, dbUser, dbPassword);
        cdb = new ClinicDB(dbUrl, dbUser, dbPassword);
        tdb = new TimeslotDB(dbUrl, dbUser, dbPassword);
        ndb = new NotificationDB(dbUrl, dbUser, dbPassword);
        adb = new AppointmentDB(dbUrl, dbUser, dbPassword);
        qdb = new QueueDB(dbUrl, dbUser, dbPassword);
        ildb = new IncidentLogDB(dbUrl, dbUser, dbPassword);
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
        HttpSession session = request.getSession(true);
        session.removeAttribute("description");
        UserBean user = (UserBean) session.getAttribute("userBean");
        // Inside handleStaff.java doPost/doGet
        if ("submitIssue".equalsIgnoreCase(action)) {
            // 1. Get parameters
            String appId = request.getParameter("appId");
            String eventType = request.getParameter("eventType");
            String rawDescription = request.getParameter("description");
            IncidentLogBean ib = new IncidentLogBean();

            // 2. Format description to include App ID if provided
            String finalDescription = rawDescription;
            if (appId != null && !appId.trim().isEmpty()) {
                ib.setAppId(Integer.parseInt(appId));
                finalDescription = "[AppID: " + appId + "] " + rawDescription;
            }

            // 3. Prepare the Bean
            ib.setUserId(user.getUserId()); // Get staff ID from session
            ib.setEventType(eventType);
            ib.setDescription(finalDescription);

            // 4. Save to DB
            boolean success = ildb.addRecord(ib);

            // 5. Redirect back to staff home with status
            if (success) {
                response.sendRedirect("staff/reportIssue.jsp?status=reported");
            } else {
                session.setAttribute("description", rawDescription);
                response.sendRedirect("staff/reportIssue.jsp?status=error");
            }
        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("No such action!!!");
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
