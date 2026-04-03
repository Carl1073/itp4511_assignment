<%-- Document : home Created on : 2026/4/3, 上午 04:45:15 Author : slt8ky --%>

    <%@page import="ict.bean.UserBean" %>
        <%@page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>JSP Page</title>
            </head>

            <body>
                <% UserBean user=(UserBean) session.getAttribute("current_user"); if (user==null) {
                    response.sendRedirect("login.jsp"); return; } %>

                    Welcome <%= user.getUsername()%>
            </body>

            </html>