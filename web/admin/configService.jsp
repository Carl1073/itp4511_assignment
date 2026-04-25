<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ict.bean.ClinicBean, ict.bean.ServiceBean, ict.bean.UserBean, java.util.ArrayList"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Configure Clinic</title>
        <link rel="stylesheet" type="text/css" href="css/style.css">
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>
        
        <%
            // Get the clinic and service list from the request attribute (passed from Servlet)
            ClinicBean clinic = (ClinicBean) request.getAttribute("clinic");
            ArrayList<ServiceBean> services = (ArrayList<ServiceBean>) request.getAttribute("services");
            
            if (clinic != null) {
        %>
        <div class="container">
            <h2>Configure Clinic: <%= clinic.getClinicName() %></h2>
            
            <form action="${pageContext.request.contextPath}/handleAdminProcess" method="POST">
                <input type="hidden" name="action" value="saveClinicConfig">
                <input type="hidden" name="clinicId" value="<%= clinic.getClinicId() %>">

                <h3>General Information</h3>
                <div class="form-row">
                    <label for="address">Address:</label>
                    <input type="text" id="address" name="address" value="<%= clinic.getAddress() %>" required />
                </div>

                <div class="form-row">
                    <label>Opening Hours:</label>
                    <input type="time" name="openTime" value="<%= clinic.getOpenTime() %>" required />
                    to
                    <input type="time" name="closeTime" value="<%= clinic.getCloseTime() %>" required />
                </div>

                <div class="form-row">
                    <label>Walk-in Support:</label>
                    <input type="checkbox" name="isWalkinEnabled" value="true" <%= clinic.getIsWalkinEnabled() ? "checked" : "" %> />
                    Enable walk-ins for this clinic
                </div>

                <hr>
                <h3>Offered Services</h3>
                <table border="1" style="width:100%; border-collapse: collapse;">
                    <tr>
                        <th>Service Name</th>
                        <th>Description</th>
                        <th>Duration (Min)</th>
                        <th>Base Price</th>
                    </tr>
                    <%
                        if (services != null) {
                            for (ServiceBean s : services) {
                    %>
                    <tr>
                        <td><%= s.getServiceName() %></td>
                        <td><%= s.getDescription() %></td>
                        <td><%= s.getDuration() %></td>
                        <td>$<%= s.getPrice() %></td>
                    </tr>
                    <%      }
                        } %>
                </table>

                <div class="button-group" style="margin-top: 20px;">
                    <button type="submit" class="btn-primary">Save Changes</button>
                    <a href="handleAdmin?action=listClinics" class="btn-secondary">Back to List</a>
                </div>
            </form>
        </div>
        <% } else { %>
            <p>Error: Clinic data not found.</p>
        <% } %>
    </body>
</html>