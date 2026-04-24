package ict.servlet;

import ict.bean.*;
import ict.db.*;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet(name = "handleService", urlPatterns = {"/handlePatientService"})
public class handlePatientService extends HttpServlet {

    private ServiceDB sdb;
    private ClinicDB cdb;
    private AppointmentDB adb;
    private TimeslotDB tdb;

    @Override
    public void init() {
        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");
        sdb = new ServiceDB(dbUrl, dbUser, dbPassword);
        cdb = new ClinicDB(dbUrl, dbUser, dbPassword);
        adb = new AppointmentDB(dbUrl, dbUser, dbPassword);
        tdb = new TimeslotDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);
        UserBean user = (UserBean) session.getAttribute("userBean");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if ("booking".equalsIgnoreCase(action)) {
            String tsIdStr = request.getParameter("tsId");
            if (tsIdStr != null) {
                int timeslotid = Integer.parseInt(tsIdStr);
                AppointmentBean ab = new AppointmentBean();
                ab.setPatientId(user.getUserId());
                ab.setTimeslotId(timeslotid);
                ab.setStatus("Confirmed");

                if (adb.addRecord(ab)) {
                    refreshAndForward(request, response, user.getUserId());
                } else {
                    response.sendRedirect("error.jsp");
                }
            }
        } else if ("cancel".equalsIgnoreCase(action)) {
            String appIdStr = request.getParameter("appId");
            if (appIdStr != null) {
                // Fetch the record first to modify it
                ArrayList<AppointmentBean> apps = adb.queryAppByUserId(user.getUserId());
                int targetId = Integer.parseInt(appIdStr);

                for (AppointmentBean ab : apps) {
                    if (ab.getAppId() == targetId) {
                        ab.setStatus("Cancelled");
                        adb.editRecord(ab); // Assuming editRecord handles the update
                        break;
                    }
                }
                refreshAndForward(request, response, user.getUserId());
            }
        } else if ("reschedule".equalsIgnoreCase(action)) {
            // 1. Get the ID of the appointment the user wants to change
            String appIdStr = request.getParameter("appId");

            if (appIdStr != null) {
                int appId = Integer.parseInt(appIdStr);

                // 2. Fetch the specific appointment details
                AppointmentBean targetApp = adb.queryAppByAppID(appId);
                System.out.println(targetApp);

                // 3. Fetch all possible options for the user to choose from
                ArrayList<ServiceBean> services = sdb.queryService();
                ArrayList<ClinicBean> clinics = cdb.queryClinic();
                // You might also want to fetch all available timeslots here
                ArrayList<TimeslotBean> availableSlots = tdb.queryAllAvailableTimeslots();
                // 4. Set attributes for the JSP
                request.setAttribute("targetApp", targetApp);
                request.setAttribute("services", services);
                request.setAttribute("clinics", clinics);
                request.setAttribute("availableSlots", availableSlots);

                // 5. CRITICAL: Use Forward, NOT sendRedirect
                RequestDispatcher rd = request.getRequestDispatcher("patient/reschedule.jsp");
                rd.forward(request, response);
            }
        } else if ("confirmReschedule".equalsIgnoreCase(action)) {
            String appIdStr = request.getParameter("appId");
            String newTsIdStr = request.getParameter("newTsId");

            if (appIdStr != null && newTsIdStr != null) {
                int appId = Integer.parseInt(appIdStr);
                int newTsId = Integer.parseInt(newTsIdStr);

                // 1. Prepare the bean for update (only changing the timeslot)
                AppointmentBean updateBean = new AppointmentBean();
                updateBean.setAppId(appId);
                updateBean.setTimeslotId(newTsId);
                updateBean.setStatus("Confirmed"); // Reset status if it was cancelled

                // 2. Update the database using your dynamic editRecord method
                int result = adb.editRecord(updateBean);

                if (result > 0) {
                    // 3. Fetch the fully populated bean (including names) for the success notification
                    AppointmentBean freshBean = adb.queryAppByAppID(appId);
                    request.setAttribute("appBean", freshBean);

                    // 4. Reload all appointments and forward to booking.jsp
                    refreshAndForward(request, response, user.getUserId());
                } else {
                    response.sendRedirect("error.jsp");
                }
            }
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("No such action!!!");
        }
    }

    // Helper method to reload list and forward to booking page
    private void refreshAndForward(HttpServletRequest request, HttpServletResponse response, int userId)
            throws ServletException, IOException {
        ArrayList<AppointmentBean> list = adb.queryAppByUserId(userId);
        request.setAttribute("allAppointments", list);
        RequestDispatcher rd = request.getRequestDispatcher("patient/booking.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
