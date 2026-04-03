<%-- Document : login Created on : 2026/4/3, 上午 02:29:24 Author : slt8ky --%>

<%@page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>

    <body>
        <form action="main" method="post">
            <input type="hidden" name="action" value="login" required>
            <div>
                <div>Username</div>
                <input type="text" name="username" value="<%= (request.getParameter("username") != null ? request.getParameter("username") : "") %>" required>
            </div>
            <div>
                <div>Password</div>
                <input type="password" name="password" value="<%= (request.getParameter("password") != null ? request.getParameter("password") : "") %>" required>
            </div>
            <div style="color:red;">
                <%= (request.getAttribute("msg") != null ? request.getAttribute("msg") : "")%>
            </div>
            <input type="submit" value="login">
            <a href="register.jsp">create account<a/>
        </form>
    </body>

</html>