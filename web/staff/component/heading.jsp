<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/styles.css">
</head>

<form class="heading" method="post" action="${pageContext.request.contextPath}/loginController">
    <input type="hidden" name="action" value="logout"/>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/staff/staffHome.jsp">Home</a>
        <a href="${pageContext.request.contextPath}/handleStaff?action=queue">Current queue</a>
        <a href="${pageContext.request.contextPath}/handleStaff?action=process">Process queue</a>
        <a href="${pageContext.request.contextPath}/handleStaff?action=outcome">Outcome</a>
        <a href="${pageContext.request.contextPath}/handleStaff?action=issue">Issue</a>
        <a href="${pageContext.request.contextPath}/handleStaff?action=profile">Profile</a>
        <a href="${pageContext.request.contextPath}/handleStaff?action=analysis">Analysis</a>
    </div>

    <input type="submit" class="logout-btn" name="logoutButton" value="Logout"/>
</form>
<%@ taglib uri="/WEB-INF/tlds/ict-taglib.tld" prefix="ict" %> 