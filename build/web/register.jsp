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
        <jsp:useBean id="userInfo" class="ict.bean.PatientBean" scope="session"/>
        <h1>Client Register</h1>
        <form action="registerController" method="post">
            <% if (request.getAttribute("errorMsg") != null) {%>
            <p class="error"><%= request.getAttribute("errorMsg")%></p>
            <% }%>

            <input type="hidden" name="action" value="register"/>
            Name: <input type="text" name="name" value="<%= request.getAttribute("name") != null ? request.getAttribute("name") : ""%>"  required /><br/>
            Username: <input id="username" type="text" name="username" value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : ""%>"  required/> <span id="username-feedback" style="margin-left: 10px;"></span><br/>
            Password: <input type="password" name="pw" value="<%= request.getAttribute("pw") != null ? request.getAttribute("pw") : ""%>" required /><br/>
            Confirm Password: <input type="password" name="cpw" value="<%= request.getAttribute("cpw") != null ? request.getAttribute("cpw") : ""%>" required /><span id="pw-feedback" style="margin-left: 10px;"></span><br/><br/>
            Gender: <label><input type="radio" name="gender" value="M" <c:if test="${gender == 'M'}">checked="checked"</c:if>  required/>Male </label> 
            <label><input type="radio" name="gender" value="F" <c:if test="${gender == 'F'}">checked="checked"</c:if>  required/>Female </label><br/>
            Address: <input type="text" name="address" size="100" value="<%= request.getAttribute("address") != null ? request.getAttribute("address") : ""%>" required/><br/>
            Date of Birth: <input type="date" name="dob" value="<%= formattedDate %>"  required/><br/>
            Contact number: <input type="text" name="tel" value="<%= request.getAttribute("tel") != null ? request.getAttribute("tel") : ""%>" required/><br/>
            Email: <input type="email" name="email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : ""%>" required/><br/>
            <input type="submit" value="Register"/>
        </form>
        <a href="login.jsp">Return</a>
    </body>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script>
        let timeout;
        function checkPwMatch() {
            clearTimeout(timeout);
            const pw = $('input[name="pw"]').val();
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
            $('input[name="username"]').on('input', function () {
                clearTimeout(timeout);
                const username = $(this).val().trim();

                if (username.length < 4) {
                    $('#username-feedback').text('').css('color', '');
                    return;
                }

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
            $('input[name="pw"]').on('input', checkPwMatch);
            $('input[name="cpw"]').on('input', checkPwMatch);
        });
    </script>
</html>
