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
        <%
            if (request.getAttribute("timeslots") != null){
            <tr>
                <th>CustId</th> <th> name</th><th> tel</th><th> age</th >
            </tr>
            <%
                ArrayList<CustomerBean> customers = (ArrayList<CustomerBean>) request.getAttribute("customers");
                for (int i = 0; i < customers.size(); i++) {
                    CustomerBean c = customers.get(i);
                    out.println("<tr>");
                    out.println("<td>" + c.getCustid() + "</td>");
                    out.println("<td>" + c.getName()+ "</td>");
                    out.println("<td>" + c.getTel()+ "</td>");
                    out.println("<td>" + c.getAge()+ "</td>");
                    out.println("</tr>");
                }            
            }

            %>
        %>

    </body>
    <script>
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('date').value = today;

    document.querySelector('form').addEventListener('submit', function(e) {
        const serviceSelect = document.getElementById('serviceId');
        if (serviceSelect.value === '') {
            e.preventDefault();
            alert('Please select a service.');
        }
        const clinicSelect = document.getElementById('clinicId');
        if (clinicSelect.value === '') {
            e.preventDefault();
            alert('Please select a clinic.');
        }
    });
</script>
</html>
