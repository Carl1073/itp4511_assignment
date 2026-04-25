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
    private QueueDB qdb;
    private NotificationDB ndb;
    private UserDB udb;

    @Override
    public void init() {
        String dbUser = this.getServletContext().getInitParameter("dbUser");
        String dbPassword = this.getServletContext().getInitParameter("dbPassword");
        String dbUrl = this.getServletContext().getInitParameter("dbUrl");
        sdb = new ServiceDB(dbUrl, dbUser, dbPassword);
        cdb = new ClinicDB(dbUrl, dbUser, dbPassword);
        adb = new AppointmentDB(dbUrl, dbUser, dbPassword);
        tdb = new TimeslotDB(dbUrl, dbUser, dbPassword);
        qdb = new QueueDB(dbUrl, dbUser, dbPassword);
        ndb = new NotificationDB(dbUrl, dbUser, dbPassword);
        udb = new UserDB(dbUrl, dbUser, dbPassword);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(true);
        UserBean user = (UserBean) session.getAttribute("userBean");
        NotificationBean nb = new NotificationBean();
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if ("booking".equalsIgnoreCase(action)) {
            String tsIdStr = request.getParameter("tsId");
            if (tsIdStr != null) {
                int timeslotid = Integer.parseInt(tsIdStr);
                int patientId = user.getUserId();

                // 1. Check if the patient already has a booking for this timeslot
                // This prevents the same user from booking the same slot twice
                ArrayList<AppointmentBean> existingApps = adb.queryAppByUserId(patientId);
                boolean alreadyBooked = false;
                for (AppointmentBean existing : existingApps) {
                    if (existing.getTimeslotId() == timeslotid && !"Cancelled".equals(existing.getStatus())) {
                        alreadyBooked = true;
                        break;
                    }
                }

                if (alreadyBooked) {
                    response.sendRedirect("patient/patientHome.jsp?error=alreadyBooked");
                    return;
                }

                // 2. Check slot capacity (quotaPerSlot vs current confirmed appointments)
                TimeslotBean tb = tdb.queryTimeslotByTsId(timeslotid);
                int currentBookings = adb.countAppointmentsByTimeslotId(timeslotid); // You may need to add this method to AppointmentDB

                if (currentBookings >= tb.getQuotaPerSlot()) {
                    response.sendRedirect("patient/patientHome.jsp?error=full");
                    return;
                }

                // 3. Proceed with booking if checks pass
                AppointmentBean ab = new AppointmentBean();
                ab.setPatientId(patientId);
                ab.setTimeslotId(timeslotid);
                ab.setStatus("Confirmed");

                if (adb.addRecord(ab)) {
                    // Notification logic
                    nb = new NotificationBean();
                    nb.setUserId(patientId);
                    nb.setMessage("You have successfully made a booking.<br/>Clinic: " + tb.getClinicName()
                            + "<br/>Service: " + tb.getServiceName()
                            + "<br/>Date: " + tb.getDate()
                            + "<br/>Time: " + tb.getOpenTime());
                    ndb.addRecord(nb);

                    forwardBooking(request, response, user.getUserId());
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
                        TimeslotBean tb = tdb.queryTimeslotByTsId(targetId);
                        System.out.println("TimeslotBean: " + tb);
                        nb.setUserId(user.getUserId());
                        nb.setMessage("You have successfully cancel a booking.  "
                                + "<br/>Clinic: " + tb.getClinicName()
                                + "<br/>Service: " + tb.getServiceName()
                                + "<br/>Date: " + tb.getDate()
                                + "<br/>Time: " + tb.getOpenTime()
                        );
                        ndb.addRecord(nb); // Save to notification table

                        ab.setStatus("Cancelled");
                        adb.editRecord(ab);
                        break;
                    }
                }
                forwardBooking(request, response, user.getUserId());
            }
        } else if ("reschedule".equalsIgnoreCase(action)) {
            String appIdStr = request.getParameter("appId");

            if (appIdStr != null) {
                int appId = Integer.parseInt(appIdStr);
                AppointmentBean targetApp = adb.queryAppByAppID(appId);

                // --- CUTOFF TIME LOGIC START ---
                if (targetApp != null) {
                    // 1. Get the appointment date and time from the targetApp
                    // Assuming targetApp contains a TimeslotBean or has access to the date/time strings
                    java.time.LocalDate appDate = java.time.LocalDate.parse(targetApp.getTimeslotBean().getDate().toString());
                    java.time.LocalTime appTime = java.time.LocalTime.parse(targetApp.getTimeslotBean().getOpenTime().toString());

                    // 2. Combine into a LocalDateTime object
                    java.time.LocalDateTime appointmentDateTime = java.time.LocalDateTime.of(appDate, appTime);
                    java.time.LocalDateTime now = java.time.LocalDateTime.now();

                    // 3. Check if 'now' is within 1 hour of the appointment
                    // Duration.between(start, end) -> positive if end is after start
                    long minutesUntilAppointment = java.time.Duration.between(now, appointmentDateTime).toMinutes();

                    if (minutesUntilAppointment < 60) {
                        // If it's less than 60 mins away (or already passed), block rescheduling
                        request.setAttribute("errorMessage", "You cannot reschedule an appointment within 1 hour of its start time.");
                        // Redirect back to the appointment list or dashboard
                        forwardBooking(request, response, user.getUserId());
                        return; // Stop execution here
                    }
                }
                // --- CUTOFF TIME LOGIC END ---

                // Fetch options for the JSP
                ArrayList<ServiceBean> services = sdb.queryService();
                ArrayList<ClinicBean> clinics = cdb.queryClinic();
                ArrayList<TimeslotBean> availableSlots = tdb.queryAllAvailableTimeslots();

                request.setAttribute("targetApp", targetApp);
                request.setAttribute("services", services);
                request.setAttribute("clinics", clinics);
                request.setAttribute("availableSlots", availableSlots);

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
                    TimeslotBean tb = tdb.queryTimeslotByTsId(appId);
                    System.out.println("TimeslotBean: " + tb);
                    nb.setUserId(user.getUserId());
                    nb.setMessage("You have successfully cancel a booking.  "
                            + "<br/>Clinic: " + tb.getClinicName()
                            + "<br/>Service: " + tb.getServiceName()
                            + "<br/>Date: " + tb.getDate()
                            + "<br/>Time: " + tb.getOpenTime()
                    );
                    ndb.addRecord(nb); // Save to notification table

                    // 4. Reload all appointments and forward to booking.jsp
                    forwardBooking(request, response, user.getUserId());
                } else {
                    response.sendRedirect("error.jsp");
                }
            }
        } else if ("joinQueue".equalsIgnoreCase(action)) {
            int clinicId = Integer.parseInt(request.getParameter("clinicId"));
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));

            ArrayList<ServiceBean> services = sdb.queryService();
            request.setAttribute("services", services);
            ArrayList<ClinicBean> clinics = cdb.queryClinic();
            request.setAttribute("clinics", clinics);

            QueueBean qb = new QueueBean();
            qb.setPatientId(user.getUserId());
            qb.setClinicId(clinicId);
            qb.setServiceId(serviceId);
            qb.setStatus("Waiting");

            // Logic to calculate the next number using the updated QueueDB
            int nextNum = qdb.getNextQueueNumber(clinicId, serviceId);
            qb.setQueueNumber(nextNum);

            if (qdb.addRecord(qb)) {
                // Store message in session so it survives the redirect
                nb.setUserId(user.getUserId());
                nb.setMessage("You have successfully joined the walk-in queue. <br/>Your number is: " + nextNum);
                ndb.addRecord(nb); // Save to notification table
                session.setAttribute("msg", "Successfully joined! Your number is: " + nextNum);
            } else {
                session.setAttribute("msg", "Failed to join queue. Please try again.");
            }

            // Redirect back to the walk-in page (PRG Pattern)
            response.sendRedirect("handlePatient?action=walkin");
            return; // Ensure no further code is executed after redirect
        } else if ("editProfile".equalsIgnoreCase(action)) {
            // 1. Get parameters from request
            String fullName = request.getParameter("fullName");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String cpw = request.getParameter("cpw");
            String gender = request.getParameter("gender");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");

            // 2. Simple Validation
            if (!password.equals(cpw)) {
                request.setAttribute("errorMsg", "Passwords do not match!");
                request.getRequestDispatcher("/patient/profile.jsp").forward(request, response);
                return;
            }

            // 3. Update the bean and Database
            user.setFullName(fullName);
            user.setUsername(username);
            user.setPassword(password);
            user.setGender(gender);
            user.setEmail(email);
            user.setPhone(phone);

            int result = udb.editRecord(user);

            if (result > 0) {
                request.setAttribute("successMsg", "Profile updated successfully!");
                // Update session bean to reflect changes immediately
                session.setAttribute("userBean", user);
            } else {
                request.setAttribute("errorMsg", "Failed to update profile. Please try again.");
            }
            String targetUrl = "/" + user.getRole().toLowerCase() + "/profile.jsp";
            request.getRequestDispatcher(targetUrl).forward(request, response);
        } else {
            response.setContentType("text/html;charset=UTF-8");
            response.getWriter().println("No such action!!!");
        }
    }

    // Helper method to reload list and forward to booking page
    private void forwardBooking(HttpServletRequest request, HttpServletResponse response, int userId)
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
