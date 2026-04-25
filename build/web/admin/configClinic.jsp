<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ict.bean.ClinicBean, ict.bean.UserBean, java.util.ArrayList"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Configure Clinic</title>
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

            .btn-secondary {
                background-color: #ff4444;
                color: white;
            }

            .btn-secondary:hover {
                background-color: #ff6b6b;
                transform: translateY(-2px);
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            }

            .btn-back {
                background-color: #666;
                color: white;
            }

            .btn-back:hover {
                background-color: #888;
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

            .form-row input[type="time"] {
                padding: 5px 8px;
                border: 1px solid #ccc;
                border-radius: 4px;
                width: 120px;
            }

            .form-row input[type="checkbox"] {
                margin-right: 8px;
                width: auto;
            }

            .time-label {
                margin: 0 10px;
                font-weight: normal;
            }
        </style>
        <script>
            function handleClinicSelect(id) {
                if (id !== "") {
                    window.location.href = "handleAdmin?action=editClinic&clinicId=" + id;
                } else {
                    window.location.href = "handleAdmin?action=configClinic";
                }
            }
        </script>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>

        <div class="container">
            <h1>Clinic Management</h1>

            <%-- Success/Error Messages from URL parameters --%>
            <% String successMsg = request.getParameter("successMsg");
                String errorMsg = request.getParameter("errorMsg"); %>

            <% if (successMsg != null && !successMsg.isEmpty()) {%>
            <div class="success-msg">✓ <%= successMsg%></div>
            <% } %>
            <% if (errorMsg != null && !errorMsg.isEmpty()) {%>
            <div class="error-msg">✗ <%= errorMsg%></div>
            <% } %>

            <%-- Clinic Selection Dropdown --%>
            <div class="info-section">
                <div class="form-row">
                    <label for="selectClinic"><strong>Select Clinic to Edit:</strong></label>
                    <select id="selectClinic" onchange="handleClinicSelect(this.value)" style="flex: 1;">
                        <option value="">--- Select or Create New Clinic ---</option>
                        <%
                            ArrayList<ClinicBean> clinicList = (ArrayList<ClinicBean>) request.getAttribute("clinicList");
                            ClinicBean selectedClinic = (ClinicBean) request.getAttribute("clinic");
                            if (clinicList != null) {
                                for (ClinicBean c : clinicList) {
                                    String selected = (selectedClinic != null && c.getClinicId() == selectedClinic.getClinicId()) ? "selected" : "";
                        %>
                        <option value="<%= c.getClinicId()%>" <%= selected%>><%= c.getClinicName()%></option>
                        <%      }
                            }%>
                    </select>
                </div>
            </div>

            <%-- Main Form --%>
            <form action="handleAdminProcess" method="POST" name="clinicForm">
                <input type="hidden" name="action" value="<%= (selectedClinic != null) ? "updateClinic" : "addClinic"%>">
                <% if (selectedClinic != null) {%>
                <input type="hidden" name="clinicId" value="<%= selectedClinic.getClinicId()%>">
                <% }%>

                <h2 style="color: #009879; margin-top: 30px;">
                    <%= (selectedClinic != null) ? "Edit Clinic Details" : "Add New Clinic"%>
                </h2>

                <div class="form-row">
                    <label for="clinicName">Clinic Name:</label>
                    <input type="text" id="clinicName" name="clinicName" 
                           value="<%= selectedClinic != null ? selectedClinic.getClinicName() : ""%>" 
                           required placeholder="Enter clinic name"/>
                </div>

                <div class="form-row">
                    <label for="address">Address:</label>
                    <input type="text" id="address" name="address" 
                           value="<%= selectedClinic != null ? selectedClinic.getAddress() : ""%>" 
                           required placeholder="Enter clinic address"/>
                </div>

                <div class="form-row">
                    <label for="openTime">Opening Hours:</label>
                    <input type="time" id="openTime" name="openTime" 
                           value="<%= selectedClinic != null ? selectedClinic.getOpenTime() : "09:00"%>" 
                           min="08:00" max="12:00" required />
                    <span class="time-label">to</span>
                    <input type="time" id="closeTime" name="closeTime" 
                           value="<%= selectedClinic != null ? selectedClinic.getCloseTime() : "18:00"%>" 
                           min="13:00" max="20:00" required />
                </div>

                <%-- 1. Check if we are in Edit or Add mode --%>
                <div class="container">
                    <%-- ... Dropdown Selection Code ... --%>

                    <form action="handleAdminProcess" method="POST">
                        <%-- Set the action based on whether a clinic is selected--%>
                        <input type="hidden" name="action" value="<%= (selectedClinic != null) ? "updateClinic" : "addClinic"%>">

                        <% if (selectedClinic != null) {%>
                        <input type="hidden" name="clinicId" value="<%= selectedClinic.getClinicId()%>">
                        <% }%>

                        <%-- Form Rows for Name, Address, etc. --%>

                        <div class="form-row">
                            <label for="isWalkinEnabled">Walk-in Support:</label>
                            <div class="radio-group" style="margin-left: 150px;">
                                <label>
                                    <input type="checkbox" id="isWalkinEnabled" name="isWalkinEnabled" value="true" 
                                           <%= (selectedClinic != null && selectedClinic.getIsWalkinEnabled()) ? "checked" : ""%> />
                                    Enable walk-ins for this clinic
                                </label>
                            </div>
                        </div>

                        <div class="button-group">
                            <% if (selectedClinic != null) { %>
                            <button type="submit" class="btn-primary">💾 Save Changes</button>
                            <button type="submit" name="action" value="deleteClinic" class="btn-secondary" 
                                    onclick="return confirm('⚠️ Are you sure?');">🗑️ Delete Clinic</button>
                            <% } else { %>
                            <button type="submit" class="btn-primary">➕ Create Clinic</button>
                            <% }%>
                            <a href="handleAdmin?action=configClinic" class="btn-back">← Back to Clinic List</a>
                        </div>
                    </form>
                </div>
            </form>
        </div>
    </body>
</html>