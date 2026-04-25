<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ict.bean.UserBean"%>
<%
    // Check if we are in Edit mode (bean exists) or Add mode (attributes might exist from previous failed submit)
    UserBean editUser = (UserBean) request.getAttribute("editUser");
    boolean isEdit = (editUser != null);

    // Helper logic to get values: priority to Bean (Edit mode), then Request (validation), then Empty
    String fullName = isEdit ? editUser.getFullName() : (request.getAttribute("fullName") != null ? (String) request.getAttribute("fullName") : "");
    String username = isEdit ? editUser.getUsername() : (request.getAttribute("username") != null ? (String) request.getAttribute("username") : "");
    String email = isEdit ? editUser.getEmail() : (request.getAttribute("email") != null ? (String) request.getAttribute("email") : "");
    String phone = isEdit ? editUser.getPhone() : (request.getAttribute("phone") != null ? (String) request.getAttribute("phone") : "");
    String role = isEdit ? editUser.getRole() : (request.getAttribute("role") != null ? (String) request.getAttribute("role") : "");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><%= isEdit ? "Edit User" : "Add New User"%></title>
        <link rel="stylesheet" type="text/css" href="css/style.css">
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <div class="container">
            <h2><%= isEdit ? "Update User Account" : "Create New Account"%></h2>

            <form action="${pageContext.request.contextPath}/handleAdminProcess" method="POST">
                <input type="hidden" name="action" value="saveUser">
                <% if (isEdit) {%>
                <input type="hidden" name="id" value="<%= editUser.getUserId()%>">
                <% }%>

                <div class="form-row">
                    <label for="fullName">Full Name:</label>
                    <input type="text" id="fullName" name="fullName" value="<%= fullName%>" required />
                </div>

                <div class="form-row">
                    <label for="username">Username:</label>
                    <input id="username" type="text" name="username" value="<%= username%>" required <%= isEdit ? "readonly style='background-color:#eee;'" : ""%>/>
                    <span id="username-feedback" style="margin-left: 10px;"></span>
                </div>

                <div class="form-row">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required />
                </div>

                <div class="form-row">
                    <label for="role">Role:</label>
                    <select name="role" id="role" required>
                        <option value="Patient" <%= "Patient".equals(role) ? "selected" : ""%>>Patient</option>
                        <option value="Staff" <%= "Staff".equals(role) ? "selected" : ""%>>Staff</option>
                        <option value="Admin" <%= "Admin".equals(role) ? "selected" : ""%>>Admin</option>
                    </select>
                </div>

                <div class="form-row">
                    <label>Gender:</label>
                    <div class="radio-group">
                        <%
                            // Determine current gender value from Bean or Request
                            String currentGender = isEdit ? editUser.getGender() : (request.getAttribute("gender") != null ? (String) request.getAttribute("gender") : "");
                        %>
                        <label>
                            <input type="radio" name="gender" value="M" <%= "M".equals(currentGender) ? "checked" : ""%> required /> Male
                        </label> 
                        <label>
                            <input type="radio" name="gender" value="F" <%= "F".equals(currentGender) ? "checked" : ""%> required /> Female
                        </label>
                        <label>
                            <input type="radio" name="gender" value="O" <%= "O".equals(currentGender) ? "checked" : ""%> required /> Other
                        </label>
                    </div>
                </div>

                <div class="form-row">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="<%= email%>" required/>
                </div>

                <div class="form-row">
                    <label for="phone">Phone/Contact:</label>
                    <input type="text" id="phone" name="phone" value="<%= phone%>" required/>
                </div>

                <div class="form-row">
                    <label></label>
                    <div class="button-group">
                        <button type="submit" class="btn-primary"><%= isEdit ? "Update User" : "Register User"%></button>
                        <a href="${pageContext.request.contextPath}/handleAdmin?action=listUsers" class="btn-secondary">Cancel</a>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>