<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ict.bean.*, java.util.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>My Appointment Records</title>
        <style>
            table { width: 100%; border-collapse: collapse; margin-top: 20px; font-family: Arial, sans-serif; }
            th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
            th { background-color: #0056b3; color: white; }
            tr:nth-child(even) { background-color: #f9f9f9; }
            .status-tag { padding: 4px 8px; border-radius: 4px; font-size: 0.9em; font-weight: bold; }
            .status-Confirmed { background-color: #d4edda; color: #155724; }
            .status-Cancelled { background-color: #f8d7da; color: #721c24; }
            .btn { padding: 6px 12px; text-decoration: none; border-radius: 4px; color: white; margin-right: 5px; font-size: 14px; }
            .btn-reschedule { background-color: #ffc107; color: #000; }
            .btn-cancel { background-color: #dc3545; }
        </style>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>

        <div style="padding: 30px;">
            <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>

            <%-- 1. Show Confirmation for the most recent booking / reschedule --%>
            <%
                AppointmentBean appBean = (AppointmentBean) request.getAttribute("appBean");
                if (appBean != null) {
                    // Determine if this was a reschedule or a brand new booking
                    boolean isReschedule = request.getParameter("action") != null
                            && request.getParameter("action").contains("Reschedule");
            %>
            <div style="background: #e8f5e9; padding: 15px; border: 1px solid #c8e6c9; margin-bottom: 20px; border-radius: 8px;">
                <h2 style="margin-top: 0; color: #2e7d32;">
                    <%= isReschedule ? "Appointment Updated!" : "Booking Successful!"%>
                </h2>
                <p>Your appointment <strong>#<%= appBean.getAppId()%></strong> is confirmed.</p>
                <p>
                    Location: <%= appBean.getTimeslotBean().getClinicName()%><br>
                    Time: <%= appBean.getTimeslotBean().getDate()%> at <%= appBean.getTimeslotBean().getOpenTime()%>
                </p>
            </div>
            <% } %>
            <h2>Your Appointment History</h2>

            <table>
                <thead>
                    <tr>
                        <th>Date</th>
                        <th>Time</th>
                        <th>Clinic Location</th>
                        <th>Service</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        // Get the list of appointments from the request attribute
                        ArrayList<AppointmentBean> appointments = (ArrayList<AppointmentBean>) request.getAttribute("allAppointments");

                        if (appointments != null && !appointments.isEmpty()) {
                            for (AppointmentBean ab : appointments) {
                                // Access the nested TimeslotBean for descriptive info
                                TimeslotBean ts = ab.getTimeslotBean();
                    %>
                    <tr>
                        <td><%= (ts != null) ? ts.getDate() : "N/A"%></td>
                        <td><%= (ts != null) ? ts.getOpenTime() : "N/A"%></td>
                        <td>
                            <%-- Assuming TimeslotBean or a Join provides the name --%>
                            <%= (ts != null) ? ts.getClinicName() : "N/A"%>
                        </td>
                        <td>
                            <%= (ts != null) ? ts.getServiceName() : "N/A"%>
                        </td>
                        <td>
                            <span class="status-tag status-<%= ab.getStatus()%>">
                                <%= ab.getStatus()%>
                            </span>
                        </td>
                        <td>
                            <a href="handlePatientService?action=reschedule&appId=<%= ab.getAppId()%>" class="btn btn-reschedule">Reschedule</a>
                            <a href="handlePatientService?action=cancel&appId=<%= ab.getAppId()%>" 
                               class="btn btn-cancel" 
                               onclick="return confirm('Confirm cancellation?')">Cancel</a>
                        </td>
                    </tr>
                    <%
                        }
                    } else {
                    %>
                    <tr>
                        <td colspan="6" style="text-align:center;">No appointments found.</td>
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div>
    </body>
</html>