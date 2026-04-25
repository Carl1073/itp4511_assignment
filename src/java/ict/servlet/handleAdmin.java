package ict.servlet;

import ict.bean.*;
import ict.db.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

/**
 * Handle Admin Actions
 */
@WebServlet(name = "handleAdmin", urlPatterns = {"/handleAdmin"})
public class handleAdmin extends HttpServlet {

    private UserDB udb;
    private ClinicDB cdb;
    private ServiceDB sdb;
    private TimeslotDB tdb;
    private AppointmentDB adb;
    private IncidentLogDB ildb; 

    @Override
    public void init() {
        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");
        
        udb = new UserDB(dbUrl, dbUser, dbPassword);
        cdb = new ClinicDB(dbUrl, dbUser, dbPassword);
        sdb = new ServiceDB(dbUrl, dbUser, dbPassword);
        tdb = new TimeslotDB(dbUrl, dbUser, dbPassword);
        adb = new AppointmentDB(dbUrl, dbUser, dbPassword);
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

        // Basic Access Control: Ensure only Admins can access
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String targetJSP = "/admin/adminHome.jsp"; // Default page

        if ("manageUsers".equalsIgnoreCase(action)) {
            // Fetch all users to display in the management table
            ArrayList<UserBean> users = udb.queryUser(); 
            request.setAttribute("users", users);
            targetJSP = "/admin/manageUsers.jsp";

        } else if ("configClinic".equalsIgnoreCase(action)) {
            ArrayList<ClinicBean> clinics = cdb.queryClinic();
            request.setAttribute("clinicList", clinics);
            targetJSP = "/admin/configClinic.jsp";

        } else if ("editClinic".equalsIgnoreCase(action)) {
            ArrayList<ClinicBean> clinics = cdb.queryClinic();
            request.setAttribute("clinicList", clinics);
            
            String clinicIdStr = request.getParameter("clinicId");
            if (clinicIdStr != null && !clinicIdStr.isEmpty()) {
                int clinicId = Integer.parseInt(clinicIdStr);
                ClinicBean selectedClinic = cdb.getClinicById(clinicId);
                request.setAttribute("clinic", selectedClinic);
            }
            targetJSP = "/admin/configClinic.jsp";

        } else if ("configService".equalsIgnoreCase(action)) {
            ArrayList<ServiceBean> services = sdb.queryService();
            request.setAttribute("serviceList", services);
            targetJSP = "/admin/configService.jsp";

        } else if ("editService".equalsIgnoreCase(action)) {
            ArrayList<ServiceBean> services = sdb.queryService();
            request.setAttribute("serviceList", services);
            
            String serviceIdStr = request.getParameter("serviceId");
            if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
                int serviceId = Integer.parseInt(serviceIdStr);
                ServiceBean selectedService = sdb.queryServiceId(serviceId);
                request.setAttribute("service", selectedService);
            }
            targetJSP = "/admin/configService.jsp";

        } else if ("manageQuota".equalsIgnoreCase(action)) {
            // Logic to fetch capacity/timeslots
            targetJSP = "/admin/configTimeslot.jsp";

        } else if ("reports".equalsIgnoreCase(action)) {
            // Analytics logic here
            targetJSP = "/admin/reports.jsp";

        } else if ("viewLogs".equalsIgnoreCase(action)) {
            // Fetch records from incident_log table
            targetJSP = "/admin/viewLogs.jsp";

        } else if ("settings".equalsIgnoreCase(action)) {
            targetJSP = "/admin/systemSettings.jsp";

        } else if ("editUserPage".equalsIgnoreCase(action)) {
            ArrayList<ClinicBean> clinics = cdb.queryClinic();
            request.setAttribute("clinics", clinics);
            String idStr = request.getParameter("id");
            if (idStr != null) {
                // Fetch specific user data for the edit form
                int id = Integer.parseInt(idStr);
                UserBean userToEdit = udb.queryUserByID(id);
                request.setAttribute("editUser", userToEdit);
            }
            // Forward to the form page (if no ID, it's "Add" mode)
            targetJSP = "/admin/editUser.jsp";
        } else if ("profile".equalsIgnoreCase(action)) {
            RequestDispatcher rd;
            rd = getServletContext().getRequestDispatcher("/admin/profile.jsp");
            rd.forward(request, response);
        }else {
            // Handle unknown actions
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("No such admin action!!!");
            return;
        }

        RequestDispatcher rd = getServletContext().getRequestDispatcher(targetJSP);
        rd.forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Controller for Administrator tasks";
    }
}