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
        <%@ include file="component/patient/heading.jsp" %>
        <jsp:useBean id="patientBean" class="ict.bean.PatientBean" scope="session"/>
        <h1>This is booking page</h1>
    </body>
</html>
