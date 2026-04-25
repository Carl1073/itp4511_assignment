<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ict.bean.ServiceBean, ict.bean.UserBean, java.util.ArrayList"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Configure Service</title>
        <link rel="stylesheet" type="text/css" href="../CSS/styles.css">
        <style>
            .button-group {
                margin-top: 20px;
                display: flex;
                gap: 10px;
                flex-wrap: wrap;
            }

            button, input[type="submit"], .btn-back {
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

            .form-row input[type="number"] {
                padding: 5px 8px;
                border: 1px solid #ccc;
                border-radius: 4px;
                width: 120px;
            }

            .form-row textarea {
                padding: 5px 8px;
                border: 1px solid #ccc;
                border-radius: 4px;
                width: 100%;
                min-height: 80px;
                resize: vertical;
            }
        </style>
        <script>
            function handleServiceSelect(id) {
                if (id !== "") {
                    window.location.href = "handleAdmin?action=editService&serviceId=" + id;
                } else {
                    window.location.href = "handleAdmin?action=configService";
                }
            }
        </script>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>

        <div class="container">
            <h1>Service Management</h1>

            <%-- Success/Error Messages from URL parameters --%>
            <% String successMsg = request.getParameter("successMsg");
               String errorMsg = request.getParameter("errorMsg"); %>

            <% if (successMsg != null && !successMsg.isEmpty()) { %>
                <div class="success-msg">✓ <%= successMsg %></div>
            <% } %>
            <% if (errorMsg != null && !errorMsg.isEmpty()) { %>
                <div class="error-msg">✗ <%= errorMsg %></div>
            <% } %>

            <%-- Service Selection Dropdown --%>
            <div class="info-section">
                <div class="form-row">
                    <label for="selectService"><strong>Select Service to Edit:</strong></label>
                    <select id="selectService" onchange="handleServiceSelect(this.value)" style="flex: 1;">
                        <option value="">--- Select or Create New Service ---</option>
                        <%
                            ArrayList<ServiceBean> serviceList = (ArrayList<ServiceBean>) request.getAttribute("serviceList");
                            ServiceBean selectedService = (ServiceBean) request.getAttribute("service");
                            if (serviceList != null) {
                                for (ServiceBean s : serviceList) {
                                    String selected = (selectedService != null && s.getServiceId() == selectedService.getServiceId()) ? "selected" : "";
                        %>
                            <option value="<%= s.getServiceId() %>" <%= selected %>><%= s.getServiceName() %></option>
                        <%      }
                            } %>
                    </select>
                </div>
            </div>

            <%-- Main Form --%>
            <form action="handleAdminProcess" method="POST" name="serviceForm">
                <%-- No hidden action input - buttons will set the action --%>
                <% if (selectedService != null) {%>
                <input type="hidden" name="serviceId" value="<%= selectedService.getServiceId()%>">
                <% }%>

                <h2 style="color: #009879; margin-top: 30px;">
                    <%= (selectedService != null) ? "Edit Service Details" : "Add New Service" %>
                </h2>

                <div class="form-row">
                    <label for="serviceName">Service Name:</label>
                    <input type="text" id="serviceName" name="serviceName"
                           value="<%= selectedService != null ? selectedService.getServiceName() : "" %>"
                           required placeholder="Enter service name"/>
                </div>

                <div class="form-row">
                    <label for="description">Description:</label>
                    <textarea id="description" name="description"
                              placeholder="Enter service description"><%= selectedService != null ? selectedService.getDescription() : "" %></textarea>
                </div>

                <div class="form-row">
                    <label for="price">Price ($):</label>
                    <input type="number" id="price" name="price" step="0.01" min="0"
                           value="<%= selectedService != null ? selectedService.getPrice() : "" %>"
                           required placeholder="0.00"/>
                </div>

                <div class="form-row">
                    <label for="duration">Duration (minutes):</label>
                    <input type="number" id="duration" name="duration" min="1" max="480"
                           value="<%= selectedService != null ? selectedService.getDuration() : "60" %>"
                           required placeholder="60"/>
                </div>

                <div class="button-group">
                    <% if (selectedService != null) { %>
                        <!-- Edit Mode: Show Save Changes and Delete buttons -->
                        <button type="submit" name="action" value="updateService" class="btn-primary">💾 Save Changes</button>
                        <button type="submit" name="action" value="deleteService" class="btn-secondary"
                                onclick="return confirm('⚠️ Are you sure you want to delete this service? This action cannot be undone.');">
                            🗑️ Delete Service
                        </button>
                    <% } else { %>
                        <!-- Add Mode: Show Create Service button only -->
                        <button type="submit" name="action" value="addService" class="btn-primary">
                            ➕ Create Service
                        </button>
                    <% } %>
                    <a href="handleAdmin?action=configService" class="btn-back" style="text-decoration: none; display: inline-flex; align-items: center;">
                        ← Back to Service List
                    </a>
                </div>
            </form>
        </div>
    </body>
</html>