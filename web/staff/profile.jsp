<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>User Profile</title>
        <style>
            .form-row { margin-bottom: 15px; }
            label { display: inline-block; width: 120px; }
            .error { color: red; }
            .success { color: green; }
        </style>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>

        <h1>My Profile</h1>

        <form action="handlePatientService" method="post">
            <% if (request.getAttribute("errorMsg") != null) {%>
                <p class="error"><%= request.getAttribute("errorMsg")%></p>
            <% } %>
            <% if (request.getAttribute("successMsg") != null) {%>
                <p class="success"><%= request.getAttribute("successMsg")%></p>
            <% } %>

            <input type="hidden" name="action" value="editProfile"/>

            <div class="form-row">
                <label for="fullName">Full Name:</label>
                <input type="text" id="fullName" name="fullName" 
                       value="<%= userBean.getFullName() %>" required />
            </div>

            <div class="form-row">
                <label for="username">Username:</label>
                <input id="username" type="text" name="username" 
                       value="<%= userBean.getUsername() %>" required/>
            </div>

            <div class="form-row">
                <label for="password">New Password:</label>
                <input type="password" id="password" name="password"
                       value="<%= userBean.getPassword() %>" required />
            </div>

            <div class="form-row">
                <label for="cpw">Confirm Password:</label>
                <input type="password" id="cpw" name="cpw" 
                       value="<%= userBean.getPassword() %>" required />
            </div>

            <div class="form-row">
                <label>Gender:</label>
                <div class="radio-group" style="display: inline-block;">
                    <% String gender = userBean.getGender(); %>
                    <label><input type="radio" name="gender" value="M" <%= "M".equals(gender) ? "checked" : ""%> required/> Male</label> 
                    <label><input type="radio" name="gender" value="F" <%= "F".equals(gender) ? "checked" : ""%> required/> Female</label>
                    <label><input type="radio" name="gender" value="O" <%= "O".equals(gender) ? "checked" : ""%> required/> Other</label>
                </div>
            </div>

            <div class="form-row">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" 
                       value="<%= userBean.getEmail() %>" required/>
            </div>

            <div class="form-row">
                <label for="phone">Phone/Contact:</label>
                <input type="text" id="phone" name="phone" 
                       value="<%= userBean.getPhone() %>" required/>
            </div>
            
            <input type="submit" value="Update Profile"/>
        </form>
    </body>
</html>