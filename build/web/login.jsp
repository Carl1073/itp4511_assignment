<%-- Document : index Created on : Mar 16, 2026, 3:29:02 PM Author : 240708635 --%>

<%@page import="ict.java.StartUp" %>
<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS/styles.css">
        <title>Login Page</title>
    </head>

    <body>
        <h1>Community Care Health Consortium </h1>
        <h2>Login Page</h2>
        <form action="loginController" method="post">
            Login as:
            <div class="segmented-control">
                <input type="radio" name="role" id="user" value="user" checked />
                <label for="user">customer</label>

                <input type="radio" name="role" id="staff" value="staff" />
                <label for="staff">staff</label>

                <input type="radio" name="role" id="admin" value="admin" />
                <label for="admin">admin</label>
            </div>
            <input type="hidden" name="action" value="authenticate" />
            <label>Name: <input name="username" type="text" required="required" /></label><br />
            <label>Password <input name="username" type="text" required="required" /></label><br />

            <br />
            <input type="submit" value="Login" id="login">
            <a href="register.jsp">No account? Register now!</a>
        </form>
    </body>

</html>