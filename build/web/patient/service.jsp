<%-- 
    Document   : service
    Created on : 2026年4月23日, 下午09:20:38
    Author     : Tong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ict.bean.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Service</title>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>

        <form action="handlePatient" method="post" id="searchForm">
            <input type="hidden" name="action" value="search">
            <div class="search">
                <div class="form-row">
                    <label for="Service">Service</label>
                    <select id="serviceId"  name="serviceId" required>
                        <ict:dropdown items="${services}" name="serviceId" option="serviceName" />
                    </select>
                </div>
                <div class="form-row">
                    <label for="Clinic">Clinic</label>
                    <select  id="clinicId" name="clinicId" required>
                        <ict:dropdown items="${clinics}" name="clinicId" option="clinicName" />
                    </select>
                </div>
                <div class="form-row">
                    <label for="date">Date</label>
                    <input name="date"  id="date" type="date" required/>
                </div>
            </div>
            <input type="submit" value="Search"/>
        </form>
        <%
            ArrayList<TimeslotBean> timeslots = (ArrayList<TimeslotBean>) request.getAttribute("timeslots");
            if (timeslots != null && !timeslots.isEmpty()) {
        %>
        <div class="form-row">
            <label for="date">Service</label>
            ${service}
        </div>
        <div class="form-row">
            <label for="date">Clinic</label>
            ${clinic}
        </div>
        <table>
            <thead>
                <tr>
                    <th>Time</th>
                    <th>Slot</th>
                    <th>Action</th>
                </tr>
            </thead>
            <tbody>
                <%
                        for (int i = 0; i < timeslots.size(); i++) {
                            TimeslotBean t = timeslots.get(i);
                            out.println("<tr>");
                            out.println("<td>" + t.getOpenTime() + "</td>");
                            out.println("<td>" + t.getRemaining() + "/" + t.getQuotaPerSlot() + "</td>");
                            if (t.getRemaining() > 0) {
                                out.println("<td><a href='handlePatientService?action=booking&tsId=" + t.getTimeslotId() + "' class='btn'>Book</a></td>");
                            } else {
                                out.println("<td><span class='btn-disabled'>Full</span></td>");
                            }
                            out.println("</tr>");
                        }
                        out.println("</table>");
                    }
                %>

                </body>
            <script>
                const today = new Date().toISOString().split('T')[0];
                document.getElementById('date').value = today;

            </script>
</html>
