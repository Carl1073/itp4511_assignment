<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Capacity Management</title>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        
        <div class="container">
            <h2>Timeslot & Capacity Rules</h2>
            <p>Define maximum quota for each service per timeslot.</p>

            <form action="handleAdminProcess" method="POST">
                <input type="hidden" name="action" value="saveCapacity">
                
                <table border="1" style="width:100%; border-collapse: collapse;">
                    <tr>
                        <th>Timeslot Range</th>
                        <th>General Consultation Quota</th>
                        <th>Specialist Quota</th>
                        <th>Vaccination Quota</th>
                    </tr>
                    <tr>
                        <td>09:00 - 10:00</td>
                        <td><input type="number" name="quota_gen_09" value="10"></td>
                        <td><input type="number" name="quota_spec_09" value="2"></td>
                        <td><input type="number" name="quota_vac_09" value="5"></td>
                    </tr>
                    <tr>
                        <td>10:00 - 11:00</td>
                        <td><input type="number" name="quota_gen_10" value="10"></td>
                        <td><input type="number" name="quota_spec_10" value="2"></td>
                        <td><input type="number" name="quota_vac_10" value="5"></td>
                    </tr>
                </table>
                <br>
                <button type="submit" class="btn-primary">Update Quotas</button>
            </form>
        </div>
    </body>
</html>