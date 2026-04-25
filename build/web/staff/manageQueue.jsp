<%@page import="java.util.ArrayList, ict.bean.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Manage Daily Queue & Appointments</title>
        <style>
            table { width: 100%; border-collapse: collapse; margin-bottom: 30px; }
            th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }
            th { background-color: #f2f2f2; }
            .filter-section { background: #f9f9f9; padding: 15px; border-radius: 5px; margin-bottom: 20px; }
        </style>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <h1>Daily Operations Dashboard</h1>

        <div class="filter-section">
            <form action="handleStaff" method="get">
                <input type="hidden" name="action" value="queue">
                <label>Clinic:</label>
                <select name="clinicId">
                    <c:forEach var="clinic" items="${clinics}">
                        <option value="${clinic.clinicId}" ${param.clinicId == clinic.clinicId ? 'selected' : ''}>
                            ${clinic.clinicName}
                        </option>
                    </c:forEach>
                </select>

                <label>Service:</label>
                <select name="serviceId">
                    <c:forEach var="service" items="${services}">
                        <option value="${service.serviceId}" ${param.serviceId == service.serviceId ? 'selected' : ''}>
                            ${service.serviceName}
                        </option>
                    </c:forEach>
                </select>
                <input type="submit" value="Filter List">
            </form>
        </div>

<h2>Scheduled Appointments (Today)</h2>
        <table>
            <tr>
                <th>App ID</th>
                <th>Patient Name</th>
                <th>Time</th>
                <th>Status</th>
            </tr>
            <% 
                ArrayList<AppointmentBean> apps = (ArrayList<AppointmentBean>) request.getAttribute("dailyAppointments");
                if (apps != null) {
                    for (AppointmentBean app : apps) {
            %>
            <tr>
                <td><%= app.getAppId() %></td>
                <td><%= app.getUserBean().getFullName() %></td>
                <td><%= app.getTimeslotBean().getOpenTime() %></td>
                <td><%= app.getStatus() %></td>
            </tr>
            <% 
                    }
                } 
            %>
        </table>

        <h2>Walk-in Queue</h2>
        <table>
            <tr>
                <th>Queue No.</th>
                <th>Patient Name</th>
                <th>Status</th>
            </tr>
            <% 
                ArrayList<QueueBean> queues = (ArrayList<QueueBean>) request.getAttribute("walkinQueues");
                if (queues != null) {
                    for (QueueBean q : queues) {
            %>
            <tr>
                <td><%= q.getQueueNumber() %></td>
                <td><%= q.getUserBean().getFullName() %></td>
                <td><%= q.getStatus() %></td>
            </tr>
            <% 
                    }
                } 
            %>
        </table>
    </body>
</html>