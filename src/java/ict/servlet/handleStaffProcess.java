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
        UserBean user = (UserBean) session.getAttribute("userBean");
        // Inside handleStaff.java doPost/doGet
        if ("submitIssue".equalsIgnoreCase(action)) {
            // 1. Extract form data
            String eventType = request.getParameter("eventType");
            String description = request.getParameter("description");
            int staffId = user.getUserId(); //

            // 2. Prepare the Bean
            IncidentLogBean ib = new IncidentLogBean();
            ib.setUserId(staffId);
            ib.setEventType(eventType);
            ib.setDescription(description);

            // 3. Database operation using IncidentLogDB
            boolean success = ildb.addRecord(ib);

            // 4. Use Redirect instead of Forward
            if (success) {
                // Redirect to a success page or home with a query parameter
                response.sendRedirect("staff/staffHome.jsp?status=success");
            } else {
                // Redirect to the form again with an error flag
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
