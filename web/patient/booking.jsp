<%-- 
    Document   : clientBooking
    Created on : 2026年4月7日, 上午11:56:23
    Author     : Tong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Booking</title>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>
        <ict:hello name="${userBean.fullName}"/>

        <h1>This is booking page</h1>
    </body>
</html>
