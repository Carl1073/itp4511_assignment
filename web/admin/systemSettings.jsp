<%--
    Document   : systemSettings
    Created on : 2026年4月26日
    Author     : Tong
--%>

<%@page import="java.util.ArrayList, ict.bean.SystemSettingBean"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>System Settings</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/styles.css">
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>

        <div style="padding: 20px;">
            <h2>Policy Settings</h2>

            <%
                String successMsg = request.getParameter("successMsg");
                String errorMsg = request.getParameter("errorMsg");
                if (successMsg != null) {
            %>
            <p style="color: green;"><%= successMsg %></p>
            <%
                }
                if (errorMsg != null) {
            %>
            <p style="color: red;"><%= errorMsg %></p>
            <%
                }
            %>

            <%
                ArrayList<SystemSettingBean> settings = (ArrayList<SystemSettingBean>) request.getAttribute("settings");
                if (settings != null && !settings.isEmpty()) {
                    SystemSettingBean setting = settings.get(0);
            %>
            <h3>Maximum Active Bookings per Patient</h3>
            <p><%= setting.getDescription() %></p>

            <form action="${pageContext.request.contextPath}/handleAdminProcess" method="post">
                <input type="hidden" name="action" value="updateSetting">
                <input type="hidden" name="settingKey" value="<%= setting.getSettingKey() %>">
                <div class="form-row">
                    <label for="settingValue">Maximum Active Bookings:</label>
                    <input type="number" name="settingValue" id="settingValue" value="<%= setting.getSettingValue() %>" min="1" required>
                </div>
                <div class="form-row">
                    <input type="submit" value="Update Setting">
                </div>
            </form>

            <p><strong>Current Value:</strong> <%= setting.getSettingValue() %></p>
            <p><strong>Last Updated:</strong> <%= setting.getUpdatedAt() %></p>
            <%
                } else {
            %>
            <p>Setting not found. Please contact administrator.</p>
            <%
                }
            %>
        </div>
    </body>
</html>
