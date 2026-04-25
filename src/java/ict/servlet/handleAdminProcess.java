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
@WebServlet(name = "handleAdminProcess", urlPatterns = {"/handleAdminProcess"})
public class handleAdminProcess extends HttpServlet {

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

        // Basic Access Control: Ensure only Admins can access
        if (user == null || !"admin".equalsIgnoreCase(user.getRole())) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        String targetJSP = "/admin/adminHome.jsp"; // Default page

        if ("saveUser".equalsIgnoreCase(action)) {
            // 1. Collect data from the editUser.jsp form
            String idStr = request.getParameter("id"); // Hidden field
            String username = request.getParameter("username");
            String fullName = request.getParameter("fullName");
            String password = request.getParameter("password");
            String role = request.getParameter("role");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String gender = request.getParameter("gender");

            UserBean ub = udb.queryUserByUsername(username);
            boolean isTaken = (ub == null) ? false : true;
            if (isTaken && idStr == null) {
                response.sendRedirect("admin/adminHome.jsp?error=isTaken&username=" + username);
                return;
            }

            ub = new UserBean();
            ub.setUsername(username);
            ub.setFullName(fullName);
            ub.setPassword(password);
            ub.setRole(role);
            ub.setGender(gender);
            ub.setEmail(email);
            ub.setPhone(phone);

            boolean success = false;
            if (idStr == null || idStr.isEmpty()) {
                // ADD NEW USER
                success = udb.addRecord(ub);
            } else {
                // EDIT EXISTING USER
                ub.setUserId(Integer.parseInt(idStr));
                success = udb.editRecord(ub) > 0;
            }

            // 2. Redirect to the list view with a status message
            if (success) {
                response.sendRedirect("admin/adminHome.jsp?status=doneUser");
            } else {
                response.sendRedirect("admin/adminHome.jsp?error=db_fail");
            }
            return;

        } else if ("deleteUser".equalsIgnoreCase(action)) {
            // 1. Get ID from the delete confirmation link
            int id = Integer.parseInt(request.getParameter("id"));

            // 2. Perform deletion
            boolean success = udb.delRecord(id);

            // 3. Redirect back to user list
            response.sendRedirect("admin/adminHome.jsp?deleted=" + success);
            return;

        } else if ("addClinic".equalsIgnoreCase(action)) {
            // Add new clinic
            String clinicName = request.getParameter("clinicName");
            String address = request.getParameter("address");
            String openTimeStr = request.getParameter("openTime");
            String closeTimeStr = request.getParameter("closeTime");
            String isWalkinStr = request.getParameter("isWalkinEnabled");

            boolean isWalkinEnabled = "true".equalsIgnoreCase(isWalkinStr) || "on".equalsIgnoreCase(isWalkinStr);

            ClinicBean cb = new ClinicBean();
            cb.setClinicName(clinicName);
            cb.setAddress(address);
            cb.setOpenTime(java.sql.Time.valueOf(openTimeStr + ":00"));
            cb.setCloseTime(java.sql.Time.valueOf(closeTimeStr + ":00"));
            cb.setIsWalkinEnabled(isWalkinEnabled);

            boolean success = cdb.addRecord(cb);

            if (success) {
                response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configClinic&successMsg=Clinic added successfully!");
            } else {
                response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configClinic&errorMsg=Failed to add clinic!");
            }
            return;

        } else if ("updateClinic".equalsIgnoreCase(action)) {
            // Update existing clinic
            String clinicIdStr = request.getParameter("clinicId");
            String clinicName = request.getParameter("clinicName");
            String address = request.getParameter("address");
            String openTimeStr = request.getParameter("openTime");
            String closeTimeStr = request.getParameter("closeTime");
            String isWalkinStr = request.getParameter("isWalkinEnabled");
            if (openTimeStr != null && openTimeStr.length() == 5) {
                openTimeStr += ":00";
            }
            if (closeTimeStr != null && closeTimeStr.length() == 5) {
                closeTimeStr += ":00";
            }

            boolean isWalkinEnabled = "true".equalsIgnoreCase(isWalkinStr) || "on".equalsIgnoreCase(isWalkinStr);

            ClinicBean cb = new ClinicBean();
            cb.setClinicId(Integer.parseInt(clinicIdStr));
            cb.setClinicName(clinicName);
            cb.setAddress(address);
            cb.setOpenTime(java.sql.Time.valueOf(openTimeStr));
            cb.setCloseTime(java.sql.Time.valueOf(closeTimeStr));
            cb.setIsWalkinEnabled(isWalkinEnabled);

            int result = cdb.editRecord(cb);

            if (result > 0) {
                response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configClinic&successMsg=Clinic updated successfully!");
            } else {
                response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configClinic&errorMsg=Failed to update clinic!");
            }
            return;

        } else if ("deleteClinic".equalsIgnoreCase(action)) {
            // Delete clinic
            String clinicIdStr = request.getParameter("clinicId");
            System.out.println("Delete Clinic - clinicIdStr: " + clinicIdStr);
            System.out.println("Delete Clinic - action: " + action);

            if (clinicIdStr == null || clinicIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configClinic&errorMsg=No clinic ID provided for deletion!");
                return;
            }

            try {
                int clinicId = Integer.parseInt(clinicIdStr);
                boolean success = cdb.delRecord(clinicId);

                if (success) {
                    response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configClinic&successMsg=Clinic deleted successfully!");
                } else {
                    response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configClinic&errorMsg=Failed to delete clinic!");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configClinic&errorMsg=Invalid clinic ID format!");
            }
            return;

        } else if ("addService".equalsIgnoreCase(action)) {
            // Add new service
            String serviceName = request.getParameter("serviceName");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String durationStr = request.getParameter("duration");
            
            double price = Double.parseDouble(priceStr);
            int duration = Integer.parseInt(durationStr);
            
            ServiceBean sb = new ServiceBean();
            sb.setServiceName(serviceName);
            sb.setDescription(description);
            sb.setPrice(price);
            sb.setDuration(duration);
            
            boolean success = sdb.addRecord(sb);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configService&successMsg=Service added successfully!");
            } else {
                response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configService&errorMsg=Failed to add service!");
            }
            return;

        } else if ("updateService".equalsIgnoreCase(action)) {
            // Update existing service
            String serviceIdStr = request.getParameter("serviceId");
            String serviceName = request.getParameter("serviceName");
            String description = request.getParameter("description");
            String priceStr = request.getParameter("price");
            String durationStr = request.getParameter("duration");
            
            double price = Double.parseDouble(priceStr);
            int duration = Integer.parseInt(durationStr);
            
            ServiceBean sb = new ServiceBean();
            sb.setServiceId(Integer.parseInt(serviceIdStr));
            sb.setServiceName(serviceName);
            sb.setDescription(description);
            sb.setPrice(price);
            sb.setDuration(duration);
            
            int result = sdb.editRecord(sb);
            
            if (result > 0) {
                response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configService&successMsg=Service updated successfully!");
            } else {
                response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configService&errorMsg=Failed to update service!");
            }
            return;

        } else if ("deleteService".equalsIgnoreCase(action)) {
            // Delete service
            String serviceIdStr = request.getParameter("serviceId");
            System.out.println("Delete Service - serviceIdStr: " + serviceIdStr);
            System.out.println("Delete Service - action: " + action);

            if (serviceIdStr == null || serviceIdStr.trim().isEmpty()) {
                response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configService&errorMsg=No service ID provided for deletion!");
                return;
            }

            try {
                int serviceId = Integer.parseInt(serviceIdStr);
                boolean success = sdb.delRecord(serviceId);

                if (success) {
                    response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configService&successMsg=Service deleted successfully!");
                } else {
                    response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configService&errorMsg=Failed to delete service!");
                }
            } catch (NumberFormatException e) {
                response.sendRedirect(request.getContextPath() + "/handleAdmin?action=configService&errorMsg=Invalid service ID format!");
            }
            return;
        } else {
            // Handle unknown actions
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.println("No such admin action!!!");
            return;
        }
    }

    @Override
    public String getServletInfo() {
        return "Controller for Administrator tasks";
    }
}
