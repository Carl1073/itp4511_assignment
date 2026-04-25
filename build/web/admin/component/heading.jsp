<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/styles.css">
</head>

<form class="heading" method="post" action="${pageContext.request.contextPath}/loginController">
    <input type="hidden" name="action" value="logout"/>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/admin/adminHome.jsp">Home</a>
        <a href="${pageContext.request.contextPath}/handleAdmin?action=manageUsers">Users</a>
        <a href="${pageContext.request.contextPath}/handleAdmin?action=configClinic">Clinics</a>
        <a href="${pageContext.request.contextPath}/handleAdmin?action=configService">Services</a>
        <a href="${pageContext.request.contextPath}/handleAdmin?action=manageQuota">Quotas</a>
        <a href="${pageContext.request.contextPath}/handleAdmin?action=reports">Reports</a>
        <a href="${pageContext.request.contextPath}/handleAdmin?action=viewLogs">Incident Logs</a>
        <a href="${pageContext.request.contextPath}/handleAdmin?action=settings">Policy Settings</a>
        <a href="${pageContext.request.contextPath}/handleAdmin?action=profile">Profile</a>

    </div>

    <input type="submit" class="logout-btn" name="logoutButton" value="Logout"/>
</form>
<%@ taglib uri="/WEB-INF/tlds/ict-taglib.tld" prefix="ict" %> 