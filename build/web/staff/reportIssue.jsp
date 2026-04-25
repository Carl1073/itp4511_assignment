<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Report Operational Issue</title>
        <link rel="stylesheet" href="css/style.css"> <%-- Add your CSS path --%>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <div class="container">
            <h2>Report Operational Issue</h2>
            <form action="handleStaffProcess" method="POST">
                <input type="hidden" name="action" value="submitIssue">
                
                <label for="eventType">Issue Type:</label>
                <select name="eventType" id="eventType" required>
                    <option value="Doctor Unavailable">Doctor Unavailable</option>
                    <option value="Service Suspension">Service Suspension</option>
                    <option value="Equipment Failure">Equipment Failure</option>
                    <option value="Other">Other</option>
                </select>
                <br><br>

                <label for="description">Details:</label><br>
                <textarea name="description" rows="5" cols="40" placeholder="Describe the issue here..." required></textarea>
                <br><br>

                <button type="submit">Submit Report</button>
                <button type="reset">Clear</button>
            </form>
        </div>
    </body>
</html>