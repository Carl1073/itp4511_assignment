<%-- 
    Document   : register
    Created on : Mar 16, 2026, 3:44:18 PM
    Author     : 240708635
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    java.sql.Date sqlDate = (java.sql.Date) request.getAttribute("dob");
    String formattedDate = "";
    if (sqlDate != null) {
        formattedDate = sqlDate.toString();   // java.sql.Date.toString() already returns "yyyy-MM-dd"
    }
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="CSS/styles.css">
        <title>Client Register</title>
    </head>

    <body>
        <jsp:useBean id="userInfo" class="ict.bean.UserBean" scope="session"/>
        <h1>Client Register</h1>
        <form action="registerController" method="post">
            <% if (request.getAttribute("errorMsg") != null) {%>
            <p class="error"><%= request.getAttribute("errorMsg")%></p>
            <% }%>

            <input type="hidden" name="action" value="register"/>

            <div class="form-row">
                <label for="fullName">Full Name:</label>
                <input type="text" id="fullName" name="fullName" 
                       value="<%= request.getAttribute("fullName") != null ? request.getAttribute("fullName") : ""%>" required />
            </div>

            <div class="form-row">
                <label for="username">Username:</label>
                <input id="username" type="text" name="username" 
                       value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : ""%>" required/>
                <span id="username-feedback" style="margin-left: 10px;"></span>
            </div>

            <div class="form-row">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password" required />
            </div>

            <div class="form-row">
                <label for="cpw">Confirm Password:</label>
                <input type="password" id="cpw" name="cpw" required />
                <span id="pw-feedback" style="margin-left: 10px;"></span>
            </div>

            <div class="form-row">
                <label>Gender:</label>
                <div class="radio-group">
                    <% String gender = (String) request.getAttribute("gender");%>
                    <label><input type="radio" name="gender" value="M" <%= "M".equals(gender) ? "checked" : ""%> required/> Male</label> 
                    <label><input type="radio" name="gender" value="F" <%= "F".equals(gender) ? "checked" : ""%> required/> Female</label>
                    <label><input type="radio" name="gender" value="O" <%= "O".equals(gender) ? "checked" : ""%> required/> Other</label>
                </div>
            </div>

            <div class="form-row">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" 
                       value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : ""%>" required/>
            </div>

            <div class="form-row">
                <label for="phone">Phone/Contact:</label>
                <input type="text" id="phone" name="phone" 
                       value="<%= request.getAttribute("phone") != null ? request.getAttribute("phone") : ""%>" required/>
            </div>

            <input type="hidden" name="clinicId" value="0" />

            <input type="submit" value="Register"/>
        </form>
        <a href="login.jsp">Return</a>
    </body>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script>
        let timeout;
        function checkPwMatch() {
            clearTimeout(timeout);
            const pw = $('input[name="password"]').val();
            const cpw = $('input[name="cpw"]').val();

            timeout = setTimeout(function () {
                console.log(pw);
                console.log(cpw);
                if (pw === cpw) {
                    $('#pw-feedback').text('✓ Password Match').css('color', 'green');
                    console.log();
                } else {
                    $('#pw-feedback').text('✗ Password Not Match').css('color', 'red');
                }
            }, 500);
        }
        $(document).ready(function () {
            // check whether Username is repeated
            $('input[name="username"]').on('input', function () {
                clearTimeout(timeout);
                const username = $(this).val().trim();

                timeout = setTimeout(function () {
                    $.ajax({
                        url: 'checkUsername',
                        type: 'GET',
                        data: {username: username},
                        dataType: 'json',
                        success: function (data) {
                            if (data.available) {
                                $('#username-feedback').text('✓ Available').css('color', 'green');
                            } else {
                                $('#username-feedback').text('✗ ' + data.message).css('color', 'red');
                            }
                        },
                        error: function () {
                            $('#username-feedback').text('Error checking availability').css('color', 'orange');
                        }
                    });
                }, 500);
            });
            $('input[name="password"]').on('input', checkPwMatch);
            $('input[name="cpw"]').on('input', checkPwMatch);
        });
    </script>
</html>
