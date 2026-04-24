<%-- Document : index Created on : Mar 16, 2026, 3:29:02 PM Author : 240708635 --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS/styles.css">
        <title>Login Page</title>
    </head>
    
    <jsp:useBean id="customerBean" class="ict.bean.UserBean" scope="session"/>

    <body>
        <h1>Community Care Health Consortium </h1>
        <h2>Login Page</h2>
        <% if (request.getAttribute("errorMsg") != null) {%>
        <p class="error"><%= request.getAttribute("errorMsg")%></p>
        <% }%>
        <form action="loginController" method="post">
            Login as:
            <div class="segmented-control">
                <input type="radio" name="role" id="patient" value="patient" checked/>
                <label for="patient">Patient</label>

                <input type="radio" name="role" id="staff" value="staff" />
                <label for="staff">Staff</label>

                <input type="radio" name="role" id="admin" value="admin" />
                <label for="admin">Admin</label>
            </div>
            <input type="hidden" name="action" value="authenticate" />
            <div class="form-row">
                <label for="username">Name:</label>
                <input id="username" name="username" type="text" required>
            </div>

            <div class="form-row">
                <label for="password">Password:</label>
                <input id="password" name="password" type="password" required>
            </div>

            <br />
            <input type="submit" value="Login" id="login">
            <a href="register.jsp">No account? Register now!</a>
        </form>
    </body>

</html>