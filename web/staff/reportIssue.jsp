<%-- Updated reportIssue.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Report Operational Issue</title>
        <link rel="stylesheet" href="css/style.css">
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <div class="container">
            <h2>Report Operational Issue</h2>

            <% if ("error".equals(request.getParameter("status"))) { %>
            <p style="color:red;">Failed to submit report. Please check the Appointment ID.</p>
            <% } else if ("reported".equals(request.getParameter("status"))) { %>
            <p style="color:green;">Reported</p>
            <% }%>
            <form action="${pageContext.request.contextPath}/handleStaffProcess" method="POST">
                <input type="hidden" name="action" value="submitIssue">

                <label for="appId">Appointment ID (Optional):</label><br>
                <input type="number" name="appId" id="appId" placeholder="e.g. 101">
                <br><br>

                <label for="eventType">Issue Type:</label><br>
                <select name="eventType" id="eventType" required>
                    <option value="Doctor Unavailable">Doctor Unavailable</option>
                    <option value="Service Suspension">Service Suspension</option>
                    <option value="Equipment Failure">Equipment Failure</option>
                    <option value="Other">Other</option>
                </select>
                <br><br>

                <label for="description">Details:</label><br>
                <textarea name="description" rows="5" cols="40" required><%=session.getAttribute("description")%></textarea>
                <br><br>

                <button type="submit">Submit Report</button>
            </form>
        </div>
    </body>
</html>