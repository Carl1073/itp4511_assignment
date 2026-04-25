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
                <%-- feedback messages --%>
        <div class="message-container">
            <%
                String error = request.getParameter("error");
                if ("full".equals(error)) {
            %>
            <div class="red">
                <strong>Booking Failed:</strong> This timeslot is currently full. Please select another time.
            </div>
            <%
            } else if ("alreadyBooked".equals(error)) {
            %>
            <div class="yellow">
                <strong>Duplicate Booking:</strong> You already have an active appointment for this timeslot.
            </div>
            <%
                }
            %>

        </div>
        <ict:hello name="${userBean.fullName}"/>
        <h1>This is home page.</h1>
        
    </body>
</html>
