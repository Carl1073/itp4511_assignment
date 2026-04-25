<%--
    Document   : clientHome
    Created on : 2026年3月24日, 下午08:54:57
    Author     : Tong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home page</title>

    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>
        <ict:hello name="${userBean.fullName}"/>
        <h1>This is home page.</h1>
        <div class="message-container">
            <%
                String error = request.getParameter("error");
                String status = request.getParameter("status");
                String deleted = request.getParameter("deleted");
                if ("isTaken".equals(error)) {
            %>
            <div style="    color: #721c24; background-color: #f8d7da; border: 1px solid #f5c6cb; padding: 10px; margin-bottom: 20px; border-radius: 4px;">
                <strong>Added/Edited Failed:</strong> <%=request.getParameter("username")%> is already taken. Please select another username.
            </div>
            <%
                }
                if ("doneUser".equals(status)) {
            %>
            <div style="background: #e8f5e9; padding: 15px; border: 1px solid #c8e6c9; margin-bottom: 20px; border-radius: 8px;">
                Added/Edited Success
            </div>
            <%
                }
                if ("true".equals(deleted)) {
            %>
            <div style="background: #e8f5e9; padding: 15px; border: 1px solid #c8e6c9; margin-bottom: 20px; border-radius: 8px;">
                Deleted Success
            </div>
            <%
                }
            %>
        </div>

    </body>
</html>
