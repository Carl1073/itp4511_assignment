<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="ict.bean.UserBean, java.util.ArrayList"%>
<!DOCTYPE html>
<html>
    <head>
        <title>User Management</title>
        <script type="text/javascript">
            function confirmDelete(id) {
                if (confirm("Are you sure you want to delete user ID: " + id + "?")) {
                    window.location.href = "${pageContext.request.contextPath}/handleAdminProcess?action=deleteUser&id=" + id;
                }
            }
        </script>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>


        <h2>User Management</h2>
        <a href="${pageContext.request.contextPath}/handleAdmin?action=editUserPage">Add New User</a>
        <br/><br/>
        <table border="1" style="width:100%; border-collapse: collapse;">
            <tr>
                <th>ID</th><th>Username</th><th>Full Name</th><th>Role</th><th>Clinic</th><th>Actions</th>
            </tr>
            <%
                ArrayList<UserBean> users = (ArrayList<UserBean>) request.getAttribute("users");
                if (users != null) {
                    for (UserBean u : users) {
            %>
            <tr>
                <td><%= u.getUserId()%></td>
                <td><%= u.getUsername()%></td>
                <td><%= u.getFullName()%></td>
                <td><%= u.getRole()%></td>
                <td>
                    <%-- Check if the role is staff to display clinic name --%>
                    <% if ("staff".equalsIgnoreCase(u.getRole()) || "admin".equalsIgnoreCase(u.getRole())) {%>
                    <%= u.getClinicName()%>
                    <% } else { %>
                    N/A
                    <% }%>
                </td>
                <td>
                    <a href="${pageContext.request.contextPath}/handleAdmin?action=editUserPage&id=<%= u.getUserId()%>">Edit</a> | 
                    <a href="javascript:confirmDelete('<%= u.getUserId()%>')">Delete</a>
                </td>
            </tr>
            <% }
                }%>
        </table>
    </div>
</body>
</html>