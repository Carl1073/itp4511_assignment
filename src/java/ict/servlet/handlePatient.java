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
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 *
 * @author Tong
 */
@WebServlet(name = "handlePatient", urlPatterns = {"/handlePatient"})
public class handlePatient extends HttpServlet {

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

        if ("service".equalsIgnoreCase(action)) {
            ArrayList<ServiceBean> services = sdb.queryService();
            request.setAttribute("services", services);
            ArrayList<ClinicBean> clinics = cdb.queryClinic();
            request.setAttribute("clinics", clinics);
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/patient/service.jsp");
            rd.forward(request, response);
        } else if ("booking".equalsIgnoreCase(action)) {
            ArrayList<AppointmentBean> list = adb.queryAppByUserId(user.getUserId());
            System.out.println(list);
            request.setAttribute("allAppointments", list);
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/patient/booking.jsp");
            rd.forward(request, response);
        } else if ("notification".equalsIgnoreCase(action)) {
            ArrayList<NotificationBean> notifications = ndb.queryNotificationByUserId(user.getUserId());
            request.setAttribute("notifications", notifications);
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/patient/notification.jsp");
            rd.forward(request, response);
        } else if ("profile".equalsIgnoreCase(action)) {
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/patient/profile.jsp");
            rd.forward(request, response);
        } else if ("walkin".equalsIgnoreCase(action)) {
            ArrayList<ClinicBean> clinics = cdb.queryClinic();
            ArrayList<ServiceBean> services = sdb.queryService();

            request.setAttribute("clinics", clinics);
            request.setAttribute("services", services);

            RequestDispatcher rd = getServletContext().getRequestDispatcher("/patient/walkin.jsp");
            rd.forward(request, response);
        } else if ("getQueueStatus".equalsIgnoreCase(action)) {
            int patientId = ((UserBean) request.getSession().getAttribute("userBean")).getUserId();
            int clinicId = Integer.parseInt(request.getParameter("clinicId"));
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));

            int currentProgress = qdb.getCurrentCallingNumber(clinicId, serviceId);
            int latestJoined = qdb.getNextQueueNumber(clinicId, serviceId) - 1;

            // Return data as simple JSON
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            String queueNumber = "\"--\"";
            int estimatedMinutes = 0;

            QueueBean qb = qdb.getQueueNumber(patientId, clinicId, serviceId);
            if (qb != null) {
                if (qb.getStatus().equalsIgnoreCase("waiting")) {
                    queueNumber = String.valueOf(qb.getQueueNumber());
                    // Calculate estimated time: (queue number - current progress) * 15 minutes
                    if (qb.getQueueNumber() > currentProgress) {
                        estimatedMinutes = (qb.getQueueNumber() - currentProgress) * 15;
                    }
                } else if ((qb.getStatus().equalsIgnoreCase("called") && currentProgress == qb.getQueueNumber())) {
                    queueNumber = String.valueOf(qb.getQueueNumber());
                    estimatedMinutes = 0; // Currently being called
                }
            }

            // Store in session for persistence
            session.setAttribute("estimatedMinutes", estimatedMinutes);
            session.setAttribute("lastCurrentProgress", currentProgress);

            String json = "{\"latest\":" + (latestJoined < 0 ? 0 : latestJoined)
                    + ", \"current\":" + currentProgress
                    + ", \"currentQueueNumber\":" + queueNumber
                    + ", \"estimatedMinutes\":" + estimatedMinutes + "}";
            response.getWriter().write(json);

            return; // Stop further processing
        } else if ("search".equalsIgnoreCase(action)) {
            ArrayList<ServiceBean> services = sdb.queryService();
            request.setAttribute("services", services);
            ArrayList<ClinicBean> clinics = cdb.queryClinic();
            request.setAttribute("clinics", clinics);
            Date date = Date.valueOf(request.getParameter("date"));
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            int clinicId = Integer.parseInt(request.getParameter("clinicId"));
            for (int i = 0; i < services.size(); i++) {
                ServiceBean s = services.get(i);
                if (serviceId == s.getServiceId()) {
                    request.setAttribute("service", s.getServiceName());
                    break;
                }
            }
            for (int i = 0; i < services.size(); i++) {
                ClinicBean c = clinics.get(i);
                if (clinicId == c.getClinicId()) {
                    request.setAttribute("clinic", c.getClinicName());
                    break;
                }
            }
            ArrayList<TimeslotBean> timeslots = tdb.queryAvailableTimeslots(date, clinicId, serviceId);
            System.out.println(timeslots.size());
            request.setAttribute("timeslots", timeslots);
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/patient/service.jsp");
            rd.forward(request, response);
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
