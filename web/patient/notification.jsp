<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Notifications</title>
        <link rel="stylesheet" type="text/css" href="css/style.css">
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>

        <div style="padding: 20px;">
            <h2>System Notifications</h2>


            <table border="1" style="width: 100%; border-collapse: collapse;">
                <thead>
                    <tr>
                        <th>Message</th>
                        <th>Date & Time</th>
                    </tr>
                </thead>
                <c:forEach var="note" items="${notifications}">
                    <tr class="notif-row ${note.isRead ? 'read' : 'unread'}" 
                        data-id="${note.notifId}" 
                        style="cursor: pointer; ${note.isRead ? '' : 'font-weight: bold; background-color: #fff9db;'}">
                        <td style="padding: 10px;">${note.message}</td>
                        <td style="padding: 10px; color: #666;">
                            <fmt:formatDate value="${note.createdAt}" pattern="yyyy-MM-dd HH:mm" />
                        </td>
                    </tr>
                </c:forEach>
            </table>



            <p><a href="handlePatient?action=profile">Back to Profile</a></p>
        </div>
    </body>
    <script>
        $(document).ready(function () {
            $(".notif-row.unread").click(function () {
                var row = $(this);
                var id = row.data("id");

                $.ajax({
                    url: "handleNotification",
                    type: "GET",
                    data: {action: "read", notifId: id},
                    success: function (response) {
                        if (response === "success") {
                            // Update UI without reload
                            row.css({
                                "font-weight": "normal",
                                "background-color": "transparent"
                            });
                            row.removeClass("unread").addClass("read");
                        }
                    }
                });
            });
        });
    </script>
</html>