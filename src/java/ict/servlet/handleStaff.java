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
@WebServlet(name = "handleStaff", urlPatterns = {"/handleStaff"})
public class handleStaff extends HttpServlet {

    private ServiceDB sdb;
    private ClinicDB cdb;
    private TimeslotDB tdb;
    private NotificationDB ndb;
    private AppointmentDB adb;
    private QueueDB qdb;

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
        System.out.println(action);
        // Inside handleStaff.java doPost/doGet
        if ("queue".equalsIgnoreCase(action)) {
            // 1. Fetch clinics and services for the dropdown filters
            ArrayList<ClinicBean> clinics = cdb.queryClinic();
            ArrayList<ServiceBean> services = sdb.queryService();
            request.setAttribute("clinics", clinics);
            request.setAttribute("services", services);

            // 2. Get filter parameters (default to first clinic if not provided)
            String cIdParam = request.getParameter("clinicId");
            String sIdParam = request.getParameter("serviceId");

            if (cIdParam != null && sIdParam != null) {
                int clinicId = Integer.parseInt(cIdParam);
                int serviceId = Integer.parseInt(sIdParam);

                // 3. Query daily appointments for this clinic/service 
                Date date = Date.valueOf(LocalDate.now());

                ArrayList<AppointmentBean> apps = adb.queryAppByClinicServiceDate(clinicId, serviceId, date);
                System.out.println(apps);
                request.setAttribute("dailyAppointments", apps);

                // 4. Query live walk-in queue for this clinic/service 
                ArrayList<QueueBean> queues = qdb.queryQueueByClinicService(clinicId, serviceId);
                request.setAttribute("walkinQueues", queues);
            }

            RequestDispatcher rd = getServletContext().getRequestDispatcher("/staff/manageQueue.jsp");
            rd.forward(request, response);
        } else if ("process".equalsIgnoreCase(action)) {
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/staff/pendingBookings.jsp");
            rd.forward(request, response);
        } else if ("outcome".equalsIgnoreCase(action)) {
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/staff/visitOutcome.jsp");
            rd.forward(request, response);
        } else if ("issue".equalsIgnoreCase(action)) {
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/staff/reportIssue.jsp");
            rd.forward(request, response);
        } else if ("profile".equalsIgnoreCase(action)) {
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/staff/profile.jsp");
            rd.forward(request, response);
        }else {
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
