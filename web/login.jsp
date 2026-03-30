<%-- Document : index Created on : Mar 16, 2026, 3:29:02 PM Author : 240708635 --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS/styles.css">
        <title>Login Page</title>
    </head>

    <jsp:useBean id="customerBean" class="ict.bean.CustomerBean" scope="session"/>
    <%if(customerBean.getRole().equalsIgnoreCase("customer")){ %>
        <jsp:forward page="client/clientHome.jsp" />
    <%}%>
    <body>
        <h1>Community Care Health Consortium </h1>
        <h2>Login Page</h2>
        <% if (request.getAttribute("errorMsg") != null) {%>
        <p class="error"><%= request.getAttribute("errorMsg")%></p>
        <% }%>
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
            <label>Password <input name="password" type="text" required="required" /></label><br />

            <br />
            <input type="submit" value="Login" id="login">
            <a href="register.jsp">No account? Register now!</a>
        </form>
    </body>

</html>