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

    @Override
    public void init() {
        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");
        sdb = new ServiceDB(dbUrl, dbUser, dbPassword);
        cdb = new ClinicDB(dbUrl, dbUser, dbPassword);
        tdb = new TimeslotDB(dbUrl, dbUser, dbPassword);
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
        if ("service".equalsIgnoreCase(action)) {
            doSearch(request, response);
        } else if ("booking".equalsIgnoreCase(action)) {
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/patient/booking.jsp");
            rd.forward(request, response);
        } else if ("search".equalsIgnoreCase(action)) {
            Date date = Date.valueOf(request.getParameter("date"));
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            int clinicId = Integer.parseInt(request.getParameter("clinicId"));
            ArrayList<TimeslotBean> timeslots = tdb.queryTimeslotbyDateClinicService(date, clinicId, serviceId);
            request.setAttribute("timeslots", timeslots);
            doSearch(request, response);
        } else {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("No such action!!!");
        }
    }

    protected void doSearch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ArrayList<ServiceBean> services = sdb.queryService();
        request.setAttribute("services", services);
        ArrayList<ClinicBean> clinics = cdb.queryClinic();
        request.setAttribute("clinics", clinics);
        RequestDispatcher rd;
        rd = getServletContext().getRequestDispatcher("/patient/service.jsp");
        rd.forward(request, response);
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
