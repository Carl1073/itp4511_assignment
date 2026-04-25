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
            boolean isTaken = (ub == null)? false: true ;
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
