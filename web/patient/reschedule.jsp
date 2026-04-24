<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ict.bean.*, java.util.*" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reschedule Appointment</title>
        <style>
            .reschedule-container { max-width: 600px; margin: 30px auto; font-family: sans-serif; }
            .info-card { background: #f8f9fa; border-left: 5px solid #ffc107; padding: 15px; margin-bottom: 20px; }
            .form-group { margin-bottom: 20px; }
            label { display: block; margin-bottom: 8px; font-weight: bold; }

            /* Segmented Control from your CSS */
            .segmented-control { display: flex; background-color: #f1f1f1; border-radius: 8px; padding: 4px; }
            .segmented-control input[type="radio"] { display: none; }
            .segmented-control label { 
                flex: 1; text-align: center; padding: 10px 0; cursor: pointer; 
                border-radius: 6px; transition: all 0.3s ease; color: #666; 
            }
            .segmented-control input[type="radio"]:checked + label {
                background-color: #fff; color: #007bff; box-shadow: 0 2px 4px rgba(0,0,0,0.1);
            }

            .submit-btn { 
                width: 100%; padding: 12px; background: #28a745; color: white; 
                border: none; border-radius: 6px; font-size: 16px; cursor: pointer; 
            }
        </style>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>

        <div class="reschedule-container">
            <%
                // Get the existing appointment details forwarded from servlet
                AppointmentBean currentApp = (AppointmentBean) request.getAttribute("targetApp");
                ArrayList<ClinicBean> clinics = (ArrayList<ClinicBean>) request.getAttribute("clinics");
                ArrayList<ServiceBean> services = (ArrayList<ServiceBean>) request.getAttribute("services");
            %>

            <h1>Reschedule Appointment</h1>

            <div class="info-card">
                <strong>Current Selection:</strong><br>
                <%= currentApp.getTimeslotBean().getClinicName()%> - 
                <%= currentApp.getTimeslotBean().getServiceName()%><br>
                Date: <%= currentApp.getTimeslotBean().getDate()%> | 
                Time: <%= currentApp.getTimeslotBean().getOpenTime()%>
            </div>

            <form action="handlePatientService" method="post">
                <input type="hidden" name="action" value="confirmReschedule">
                <input type="hidden" name="appId" value="<%= currentApp.getAppId()%>">

                <%-- 1. Clinic Dropdown --%>
                <div class="form-group">
                    <label>New Clinic Location</label>
                    <select name="newClinicId" id="clinicSelect" class="submit-btn" style="background:white; color:black; border:1px solid #ccc;">
                        <option value="">-- Select Clinic --</option>
                        <%
                            AppointmentBean target = (AppointmentBean) request.getAttribute("targetApp");
                            int originalClinicId = target.getTimeslotBean().getClinicId();

                            for (ClinicBean c : clinics) {
                                String selected = (c.getClinicId() == originalClinicId) ? "selected" : "";
                        %>
                        <option value="<%= c.getClinicId()%>" <%= selected%>>
                            <%= c.getClinicName()%>
                        </option>
                        <% } %>
                    </select>
                </div>

                <%-- 2. Service Dropdown --%>
                <div class="form-group">
                    <label>New Service Type</label>
                    <select name="newServiceId" id="serviceSelect" class="submit-btn" style="background:white; color:black; border:1px solid #ccc;">
                        <option value="">-- Select Service --</option>
                        <%
                            int originalServiceId = target.getTimeslotBean().getServiceId();

                            for (ServiceBean s : services) {
                                String selected = (s.getServiceId() == originalServiceId) ? "selected" : "";
                        %>
                        <option value="<%= s.getServiceId()%>" <%= selected%>>
                            <%= s.getServiceName()%>
                        </option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label>Select New Date & Time</label>
                    <select name="newTsId" id="timeslotSelect" class="submit-btn" style="background: white; color: black; border: 1px solid #ccc;" required>
                        <option value="">-- Choose a new timeslot --</option>
                        <%
                            ArrayList<TimeslotBean> slots = (ArrayList<TimeslotBean>) request.getAttribute("availableSlots");
                            if (slots != null) {
                                for (TimeslotBean ts : slots) {
                        %>
                        <option value="<%= ts.getTimeslotId()%>" 
                                data-clinic="<%= ts.getClinicId()%>" 
                                data-service="<%= ts.getServiceId()%>" 
                                style="display:none;"> <%= ts.getDate()%> at <%= ts.getOpenTime()%>, Remaining: <%= ts.getRemaining()%>
                        </option>
                        <%
                                }
                            }
                        %>
                    </select>
                </div>

                <button type="submit" class="submit-btn">Confirm Change</button>
                <p style="text-align: center;"><a href="${pageContext.request.contextPath}/handlePatient?action=booking"">Cancel and Go Back</a></p>
            </form>
        </div>
    </body>
    <script>
        function filterTimeslots() {
            // Updated to get values from select elements instead of radio inputs
            const selectedClinic = document.getElementById('clinicSelect').value;
            const selectedService = document.getElementById('serviceSelect').value;

            const selectBox = document.getElementById('timeslotSelect');
            const options = selectBox.querySelectorAll('option');

            // Reset select box value only if the current selection is no longer valid
            // selectBox.value = ""; 

            options.forEach(option => {
                if (option.value === "")
                    return;

                const optClinic = option.getAttribute('data-clinic');
                const optService = option.getAttribute('data-service');

                if (optClinic === selectedClinic && optService === selectedService) {
                    option.style.display = "block";
                    option.disabled = false;
                } else {
                    option.style.display = "none";
                    option.disabled = true;
                }
            });
        }

        // Update listeners to "change" on the select elements
        document.getElementById('clinicSelect').addEventListener('change', filterTimeslots);
        document.getElementById('serviceSelect').addEventListener('change', filterTimeslots);

        // Run once on page load to set the initial valid timeslots
        window.onload = filterTimeslots;
    </script>
</html>