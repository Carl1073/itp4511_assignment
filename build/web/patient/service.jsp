<%-- 
    Document   : service
    Created on : 2026年4月23日, 下午09:20:38
    Author     : Tong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ict.bean.*"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Service</title>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <form action="handlePatient" method="post">
            <input type="hidden" name="action" value="search">
            <div class="search">
                <div class="form-row">
                    <label for="Service">Service</label>
                    <select id="serviceId"  name="serviceId">
                        <ict:dropdown items="${services}" name="serviceId" option="serviceName" />
                    </select>
                </div>
                <div class="form-row">
                    <label for="Clinic">Clinic</label>
                    <select  id="clinicId" name="clinicId">
                        <ict:dropdown items="${clinics}" name="clinicId" option="clinicName" />
                    </select>
                </div>
                <div class="form-row">
                    <label for="date">Date</label>
                    <input name="date"  id="date" type="date"/>
                </div>
            </div>
            <input type="submit" value="Search"/>
        </form>
        <table>
            
        </table>

    </body>
    <script>
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('date').value = today;
</script>
</html>
