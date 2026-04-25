<%@page import="java.util.HashMap"%>
<%@page import="java.util.Map"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ict.bean.ClinicBean, ict.bean.ServiceBean, ict.bean.TimeslotBean, ict.bean.UserBean, java.util.ArrayList, java.util.Calendar, java.util.Date, java.sql.Time, java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Configure Timeslots</title>
        <link rel="stylesheet" type="text/css" href="../CSS/styles.css">
        <style>
            .button-group {
                margin-top: 20px;
                display: flex;
                gap: 10px;
                flex-wrap: wrap;
            }

            button, input[type="submit"] {
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                font-size: 14px;
                font-weight: 600;
                transition: all 0.3s ease;
            }

            .btn-primary {
                background-color: #009879;
                color: white;
            }

            .btn-primary:hover {
                background-color: #047857;
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            }

            .success-msg {
                color: #28a745;
                background-color: #d4edda;
                border: 1px solid #c3e6cb;
                padding: 12px;
                border-radius: 4px;
                margin-bottom: 20px;
            }

            .error-msg {
                color: #721c24;
                background-color: #f8d7da;
                border: 1px solid #f5c6cb;
                padding: 12px;
                border-radius: 4px;
                margin-bottom: 20px;
            }

            .info-section {
                background-color: #f9f9f9;
                padding: 20px;
                border-radius: 4px;
                margin-bottom: 20px;
                border-left: 4px solid #009879;
            }

            .timeslot-table {
                width: 100%;
                border-collapse: collapse;
                margin: 20px 0;
                font-size: 14px;
                box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
                border-radius: 8px;
                overflow: hidden;
            }

            .timeslot-table th, .timeslot-table td {
                border: 1px solid #ddd;
                padding: 12px;
                text-align: center;
            }

            .timeslot-table th {
                background-color: #009879;
                color: white;
                font-weight: bold;
            }

            .timeslot-table tbody tr:nth-child(even) {
                background-color: #f8f8f8;
            }

            .timeslot-table tbody tr:hover {
                background-color: #e8f4f8;
            }

            .timeslot-table input[type="number"] {
                width: 60px;
                padding: 5px;
                text-align: center;
                border: 1px solid #ccc;
                border-radius: 3px;
            }

            .selection-form {
                display: flex;
                gap: 20px;
                align-items: end;
                margin-bottom: 20px;
            }

            .selection-form .form-row {
                margin-bottom: 0;
            }
        </style>
        <script>
            function handleClinicDateSelection() {
                var clinicSelect = document.getElementById('clinicSelect');
                var dateSelect = document.getElementById('dateSelect');

                if (clinicSelect.value && dateSelect.value) {
                    window.location.href = "handleAdmin?action=manageQuota&clinicId=" + clinicSelect.value + "&date=" + dateSelect.value;
                }
            }

            function validateQuotas() {
                var inputs = document.querySelectorAll('input[type="number"]');
                var valid = true;

                inputs.forEach(function (input) {
                    var value = parseInt(input.value);
                    if (isNaN(value) || value < 0) {
                        input.style.borderColor = 'red';
                        valid = false;
                    } else {
                        input.style.borderColor = '#ccc';
                    }
                });

                if (!valid) {
                    alert('Please enter valid quota values (0 or greater)');
                    return false;
                }

                return confirm('Are you sure you want to update the timeslot quotas?');
            }
        </script>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>

        <div class="container">
            <h1>Timeslot & Capacity Management</h1>

            <%-- Success/Error Messages from URL parameters --%>
            <% String successMsg = request.getParameter("successMsg");
                String errorMsg = request.getParameter("errorMsg"); %>

            <% if (successMsg != null && !successMsg.isEmpty()) {%>
            <div class="success-msg">✓ <%= successMsg%></div>
            <% } %>
            <% if (errorMsg != null && !errorMsg.isEmpty()) {%>
            <div class="error-msg">✗ <%= errorMsg%></div>
            <% } %>

            <%-- Clinic and Date Selection --%>
            <div class="info-section">
                <h3>Select Clinic and Date</h3>
                <form class="selection-form" onsubmit="return false;">
                    <div class="form-row">
                        <label for="clinicSelect"><strong>Clinic:</strong></label>
                        <select id="clinicSelect" onchange="handleClinicDateSelection()">
                            <option value="">--- Select Clinic ---</option>
                            <%
                                ArrayList<ClinicBean> clinicList = (ArrayList<ClinicBean>) request.getAttribute("clinicList");
                                ClinicBean selectedClinic = (ClinicBean) request.getAttribute("selectedClinic");
                                if (clinicList != null) {
                                    for (ClinicBean c : clinicList) {
                                        String selected = (selectedClinic != null && c.getClinicId() == selectedClinic.getClinicId()) ? "selected" : "";
                            %>
                            <option value="<%= c.getClinicId()%>" <%= selected%>><%= c.getClinicName()%></option>
                            <%      }
                                } %>
                        </select>
                    </div>

                    <div class="form-row">
                        <label for="dateSelect"><strong>Date:</strong></label>
                        <select id="dateSelect" onchange="handleClinicDateSelection()">
                            <option value="">--- Select Date ---</option>
                            <%
                                // Get today and tomorrow dates
                                Calendar cal = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                                // Today
                                String today = sdf.format(cal.getTime());
                                cal.add(Calendar.DAY_OF_MONTH, 1);
                                String tomorrow = sdf.format(cal.getTime());

                                String selectedDate = (String) request.getAttribute("selectedDate");
                                System.out.println(selectedDate);
                                if (selectedDate == null) {
                                    selectedDate = "";
                                }
                            %>
                            <option value="<%= today%>" <%= today.equals(selectedDate) ? "selected" : ""%>>Today (<%= today%>)</option>
                            <option value="<%= tomorrow%>" <%= tomorrow.equals(selectedDate) ? "selected" : ""%>>Tomorrow (<%= tomorrow%>)</option>
                        </select>
                    </div>
                </form>
            </div>

            <%-- Timeslot Configuration Table --%>
            <% if (selectedClinic != null && request.getAttribute("selectedDate") != null) { %>
            <%
                String dateStr = (String) request.getAttribute("selectedDate");
                java.sql.Date selectDate = null;
                
                if (dateStr != null && !dateStr.isEmpty()) {
                    selectDate = java.sql.Date.valueOf(dateStr);
                }

                ArrayList<TimeslotBean> existingTimeslots = (ArrayList<TimeslotBean>) request.getAttribute("existingTimeslots");
                ArrayList<ServiceBean> services = (ArrayList<ServiceBean>) request.getAttribute("serviceList");

                // Create a map of existing timeslots for quick lookup
                Map<String, TimeslotBean> timeslotMap = new HashMap<String, TimeslotBean>();
                if (existingTimeslots != null) {
                    for (TimeslotBean ts : existingTimeslots) {
                        String key = ts.getServiceId() + "_" + ts.getOpenTime().toString().substring(0, 5);
                        timeslotMap.put(key, ts);
                    }
                }
            %>

            <form action="handleAdminProcess" method="POST" onsubmit="return validateQuotas();">
                <input type="hidden" name="action" value="updateTimeslotQuotas">
                <input type="hidden" name="clinicId" value="<%= selectedClinic.getClinicId()%>">
                <input type="hidden" name="date" value="<%= dateStr %>">

                <h2>Configure Timeslot Quotas for <%= selectedClinic.getClinicName()%> on <%= selectDate%></h2>

                <table class="timeslot-table">
                    <thead>
                        <tr>
                            <th>Time Slot</th>
                                <%
                                    if (services != null) {
                                        for (ServiceBean service : services) {
                                %>
                            <th><%= service.getServiceName()%></th>
                                <%      }
                                    } %>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            // Generate timeslots every hour from clinic opening to closing time
                            Calendar timeCal = Calendar.getInstance();
                            timeCal.setTime(selectedClinic.getOpenTime());

                            Calendar closeCal = Calendar.getInstance();
                            closeCal.setTime(selectedClinic.getCloseTime());

                            while (timeCal.before(closeCal) || timeCal.equals(closeCal)) {
                                String timeStr = String.format("%02d:%02d", timeCal.get(Calendar.HOUR_OF_DAY), timeCal.get(Calendar.MINUTE));
                                String displayTime = timeStr + " - " + String.format("%02d:%02d",
                                        (timeCal.get(Calendar.HOUR_OF_DAY) + 1) % 24, timeCal.get(Calendar.MINUTE));
                        %>
                        <tr>
                            <td><strong><%= displayTime%></strong></td>
                            <%
                                if (services != null) {
                                    for (ServiceBean service : services) {
                                        String key = service.getServiceId() + "_" + timeStr;
                                        TimeslotBean existing = timeslotMap.get(key);
                                        int currentQuota = (existing != null) ? existing.getQuotaPerSlot() : 0;
                                        String inputName = "quota_" + service.getServiceId() + "_"
                                                + String.format("%02d%02d", timeCal.get(Calendar.HOUR_OF_DAY), timeCal.get(Calendar.MINUTE));
                            %>
                            <td>
                                <input type="number" name="<%= inputName%>" value="<%= currentQuota%>" 
                                       min="0" max="50" placeholder="0">
                            </td>
                            <%      }
                                } %>
                        </tr>
                        <%
                                timeCal.add(Calendar.HOUR_OF_DAY, 1);
                            }
                        %>
                    </tbody>
                </table>

                <div class="button-group">
                    <button type="submit" class="btn-primary">💾 Update Quotas</button>
                    <a href="handleAdmin?action=manageQuota" class="btn-primary" style="text-decoration: none;">← Back to Selection</a>
                </div>
            </form>
            <% } else { %>
            <div class="info-section">
                <p>Please select a clinic and date above to configure timeslot quotas.</p>
                <p>The system will show hourly timeslots from the clinic's opening time to closing time, allowing you to set capacity limits for each service.</p>
            </div>
            <% }%>
        </div>
    </body>
</html>