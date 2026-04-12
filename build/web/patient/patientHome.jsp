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
        <%@ include file="component/patient/heading.jsp" %>
        <jsp:useBean id="patientBean" class="ict.bean.PatientBean" scope="session"/>
        <b> Hello, <jsp:getProperty name="patientBean" property="name"/></b>
        <h1>Thsi is home page.</h1>
    </body>
</html>
