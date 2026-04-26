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
    private SystemSettingsDB ssdb; 

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
        ildb = new IncidentLogDB(dbUrl, dbUser, dbPassword);
        ssdb = new SystemSettingsDB(dbUrl, dbUser, dbPassword);
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
            ArrayList<UserBean> users = udb.queryUserWithClinic(); 
            request.setAttribute("users", users);
            ArrayList<ClinicBean> clinics = cdb.queryClinic(); 
            request.setAttribute("clinics", clinics);
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
            // Fetch all clinics for dropdown
            ArrayList<ClinicBean> clinics = cdb.queryClinic();
            request.setAttribute("clinicList", clinics);
            
            // Fetch all services for the table
            ArrayList<ServiceBean> services = sdb.queryService();
            request.setAttribute("serviceList", services);
            
            // Check if clinic and date are selected
            String clinicIdStr = request.getParameter("clinicId");
            String dateStr = request.getParameter("date");
            
            
            if (clinicIdStr != null && !clinicIdStr.isEmpty() && dateStr != null && !dateStr.isEmpty()) {
                try {
                    int clinicId = Integer.parseInt(clinicIdStr);
                    java.sql.Date date = java.sql.Date.valueOf(dateStr);
                    
                    // Fetch existing timeslots for this clinic and date
                    ArrayList<TimeslotBean> existingTimeslots = tdb.queryTimeslotbyDateClinic(date, clinicId);
                    request.setAttribute("existingTimeslots", existingTimeslots);
                    
                    // Set selected clinic and date
                    ClinicBean selectedClinic = cdb.getClinicById(clinicId);
                    request.setAttribute("selectedClinic", selectedClinic);
                    request.setAttribute("selectedDate", dateStr);
                    System.out.println(dateStr);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            targetJSP = "/admin/configTimeslot.jsp";

        } else if ("reports".equalsIgnoreCase(action)) {
            // Fetch filter options
            ArrayList<ClinicBean> clinics = cdb.queryClinic();
            ArrayList<ServiceBean> services = sdb.queryService();
            request.setAttribute("clinicList", clinics);
            request.setAttribute("serviceList", services);

            // Get filter parameters
            String clinicIdStr = request.getParameter("clinicId");
            String serviceIdStr = request.getParameter("serviceId");
            String monthYear = request.getParameter("monthYear");
            String status = request.getParameter("status");
            String reportType = request.getParameter("reportType");

            Integer clinicId = null;
            Integer serviceId = null;

            if (clinicIdStr != null && !clinicIdStr.isEmpty()) {
                clinicId = Integer.parseInt(clinicIdStr);
            }
            if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
                serviceId = Integer.parseInt(serviceIdStr);
            }

            // Default to current month if not specified
            if (monthYear == null || monthYear.isEmpty()) {
                java.time.LocalDate now = java.time.LocalDate.now();
                monthYear = now.getYear() + "-" + String.format("%02d", now.getMonthValue());
            }

            if ("appointments".equals(reportType) || reportType == null) {
                // Fetch appointment records with filters
                ArrayList<AppointmentBean> appointments = adb.getAppointmentsWithFilters(clinicId, serviceId, monthYear, status);
                request.setAttribute("appointments", appointments);
                request.setAttribute("reportType", "appointments");
            } else if ("utilisation".equals(reportType)) {
                // Fetch utilisation data
                ArrayList<Object[]> utilisationData = adb.getUtilisationData(monthYear);
                request.setAttribute("utilisationData", utilisationData);
                request.setAttribute("reportType", "utilisation");
            } else if ("noshows".equals(reportType)) {
                // Fetch no-show summary
                ArrayList<Object[]> noShowData = adb.getNoShowSummary(monthYear);
                request.setAttribute("noShowData", noShowData);
                request.setAttribute("reportType", "noshows");
            }

            // Set filter values for form
            request.setAttribute("selectedClinicId", clinicIdStr);
            request.setAttribute("selectedServiceId", serviceIdStr);
            request.setAttribute("selectedMonthYear", monthYear);
            request.setAttribute("selectedStatus", status);

            targetJSP = "/admin/reports.jsp";
        } else if ("viewLogs".equalsIgnoreCase(action)) {
            // Fetch records from incident_log table
            String category = request.getParameter("category");
            String thresholdStr = request.getParameter("threshold");
            int threshold = 3; // default
            if (thresholdStr != null && !thresholdStr.isEmpty()) {
                try {
                    threshold = Integer.parseInt(thresholdStr);
                } catch (NumberFormatException e) {
                    threshold = 3;
                }
            }

            if ("incidentLogs".equals(category) || category == null) {
                ArrayList<IncidentLogBean> logs = ildb.queryIncidentLog();
                request.setAttribute("logs", logs);
                request.setAttribute("category", "incidentLogs");
            } else if ("noShows".equals(category)) {
                ArrayList<UserBean> users = adb.getUsersWithRepeatedStatus("No-show", threshold);
                request.setAttribute("users", users);
                request.setAttribute("category", "noShows");
                request.setAttribute("threshold", threshold);
            } else if ("cancellations".equals(category)) {
                ArrayList<UserBean> users = adb.getUsersWithRepeatedStatus("Cancelled", threshold);
                request.setAttribute("users", users);
                request.setAttribute("category", "cancellations");
                request.setAttribute("threshold", threshold);
            }
            targetJSP = "/admin/viewLogs.jsp";

        } else if ("settings".equalsIgnoreCase(action)) {
            SystemSettingBean setting = ssdb.querySettingByKey("max_active_bookings_per_patient");
            if (setting != null) {
                ArrayList<SystemSettingBean> settings = new ArrayList<>();
                settings.add(setting);
                request.setAttribute("settings", settings);
            } else {
                // If not exists, create it
                ssdb.createTable(); // This will insert defaults
                SystemSettingBean setting2 = ssdb.querySettingByKey("max_active_bookings_per_patient");
                if (setting2 != null) {
                    ArrayList<SystemSettingBean> settings = new ArrayList<>();
                    settings.add(setting2);
                    request.setAttribute("settings", settings);
                }
            }
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