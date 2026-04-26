<%@page import="java.util.ArrayList, ict.bean.IncidentLogBean, ict.bean.UserBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Incident Logs</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/styles.css">
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>

        <div style="padding: 20px;">
            <h2>Incident Logs Review</h2>

            <form action="${pageContext.request.contextPath}/handleAdmin" method="get">
                <input type="hidden" name="action" value="viewLogs">
                <div class="form-row">
                    <label for="category">Category:</label>
                    <select name="category" id="category">
                        <option value="incidentLogs" <%= "incidentLogs".equals(request.getAttribute("category")) ? "selected" : "" %>>All Incident Logs</option>
                        <option value="noShows" <%= "noShows".equals(request.getAttribute("category")) ? "selected" : "" %>>Repeated No-Shows</option>
                        <option value="cancellations" <%= "cancellations".equals(request.getAttribute("category")) ? "selected" : "" %>>Frequent Cancellations</option>
                    </select>
                </div>
                <div class="form-row" id="thresholdRow" style="<%= "noShows".equals(request.getAttribute("category")) || "cancellations".equals(request.getAttribute("category")) ? "" : "display:none;" %>">
                    <label for="threshold">Threshold (times):</label>
                    <input type="number" name="threshold" id="threshold" value="<%= request.getAttribute("threshold") != null ? request.getAttribute("threshold") : "3" %>" min="1">
                </div>
                <div class="form-row">
                    <input type="submit" value="Filter">
                </div>
            </form>

            <%
                String category = (String) request.getAttribute("category");
                if ("incidentLogs".equals(category) || category == null) {
                    ArrayList<IncidentLogBean> logs = (ArrayList<IncidentLogBean>) request.getAttribute("logs");
            %>
            <h3>All Incident Logs</h3>
            <table border="1" style="width:100%; border-collapse: collapse;">
                <tr>
                    <th>Log ID</th><th>User</th><th>Event Type</th><th>Description</th><th>Created At</th>
                </tr>
                <%
                    if (logs != null) {
                        for (IncidentLogBean log : logs) {
                %>
                <tr>
                    <td><%= log.getLogId() %></td>
                    <td><%= log.getUserBean() != null ? log.getUserBean().getFullName() : "N/A" %></td>
                    <td><%= log.getEventType() %></td>
                    <td><%= log.getDescription() %></td>
                    <td><%= log.getCreatedAt() %></td>
                </tr>
                <%
                        }
                    }
                %>
            </table>
            <%
                } else if ("noShows".equals(category) || "cancellations".equals(category)) {
                    ArrayList<UserBean> users = (ArrayList<UserBean>) request.getAttribute("users");
                    String catName = "noShows".equals(category) ? "No-Shows" : "Cancellations";
            %>
            <h3>Users with Repeated <%= catName %> (>= <%= request.getAttribute("threshold") %> times)</h3>

            <form action="${pageContext.request.contextPath}/handleAdminProcess" method="post">
                <input type="hidden" name="action" value="sendNotification">
                <input type="hidden" name="category" value="<%= category %>">
                <input type="hidden" name="threshold" value="<%= request.getAttribute("threshold") %>">
                <%
                    if (users != null) {
                        for (UserBean u : users) {
                %>
                <input type="hidden" name="userIds" value="<%= u.getUserId() %>">
                <%
                        }
                    }
                %>
                <div class="form-row">
                    <label for="message">Notification Message:</label>
                    <textarea name="message" id="message" rows="4" cols="50">Dear user, we have noticed repeated <%= catName.toLowerCase() %> in your appointment history. Please contact us if you need assistance.</textarea>
                </div>
                <div class="form-row">
                    <input type="submit" value="Send Notification to All Listed Users">
                </div>
            </form>

            <table border="1" style="width:100%; border-collapse: collapse;">
                <tr>
                    <th>User ID</th><th>Full Name</th><th>Email</th><th>Phone</th>
                </tr>
                <%
                    if (users != null) {
                        for (UserBean u : users) {
                %>
                <tr>
                    <td><%= u.getUserId() %></td>
                    <td><%= u.getFullName() %></td>
                    <td><%= u.getEmail() %></td>
                    <td><%= u.getPhone() %></td>
                </tr>
                <%
                        }
                    }
                %>
            </table>
            <%
                }
            %>

            <%
                Boolean sent = (Boolean) request.getAttribute("notificationSent");
                String sentParam = request.getParameter("notificationSent");
                if (sent != null || sentParam != null) {
                    boolean success = (sent != null && sent) || "success".equals(sentParam);
            %>
            <%
                if (success) {
            %>
            <p style="color: green;">Notification sent successfully.</p>
            <%
                    } else {
            %>
            <p style="color: red;">Failed to send notification.</p>
            <%
                    }
                }
            %>
        </div>

        <script>
            document.getElementById('category').addEventListener('change', function() {
                var thresholdRow = document.getElementById('thresholdRow');
                if (this.value === 'noShows' || this.value === 'cancellations') {
                    thresholdRow.style.display = '';
                } else {
                    thresholdRow.style.display = 'none';
                }
            });
        </script>
    </body>
</html>
