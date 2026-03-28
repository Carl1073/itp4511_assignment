<%-- 
    Document   : loginError
    Created on : Mar 19, 2026, 10:05:24 AM
    Author     : 240708635
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Login Error</title>
    </head>
    <body>
        <p>Incorrect Password</p>
        <p>
            <% out.println("<a href=\"" + request.getContextPath() + "/main\">Login again</a>"); %>
        </p>
    </body>
</html>
