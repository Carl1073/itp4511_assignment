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
        <form action="handlePatient" method="post" id="searchForm">
            <input type="hidden" name="action" value="search">
            <div class="search">
                <div class="form-row">
                    <label for="Service">Service</label>
                    <select id="serviceId"  name="serviceId">
                        <ict:dropdown items="${services}" name="serviceId" option="serviceName" />
                    </select>
                </div>
                <div class="form-row">
                    <label for="Clinic">Clinic</label>
                    <select  id="clinicId" name="clinicId">
                        <ict:dropdown items="${clinics}" name="clinicId" option="clinicName" />
                    </select>
                </div>
                <div class="form-row">
                    <label for="date">Date</label>
                    <input name="date"  id="date" type="date"/>
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
        <table><tr><th>Time</th> <th>Slot</th><th>Action</th></tr>
                    <%
                            for (int i = 0; i < timeslots.size(); i++) {
                                TimeslotBean t = timeslots.get(i);
                                out.println("<tr>");
                                out.println("<th>" + t.getOpenTime()  +  "</th>");
                                out.println("<th>" + t.getQuotaPerSlot() +  "</th>");
                                out.println("</tr>");
                            }
                            out.println("</table>");
                        }
                    %>

    </body>
    <script>
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('date').value = today;

        document.getElementById('searchForm').addEventListener('submit', function (e) {
            const serviceSelect = document.getElementById('serviceId');
            const clinicSelect = document.getElementById('clinicId');

            // Check Service
            if (!serviceSelect.value) {
                e.preventDefault(); // This stops the redirect/reload
                alert('Please select a service.');
                return false; // Exit the function so we don't alert twice
            }

            // Check Clinic
            if (!clinicSelect.value) {
                e.preventDefault();
                alert('Please select a clinic.');
                return false;
            }
        });
    </script>
</html>
