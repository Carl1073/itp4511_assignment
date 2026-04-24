<head>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/CSS/styles.css">
</head>

<form class="heading" method="post" action="${pageContext.request.contextPath}/loginController">
    <input type="hidden" name="action" value="logout"/>

    <div class="nav-links">
        <a href="${pageContext.request.contextPath}/patient/patientHome.jsp">Home</a>
        <a href="${pageContext.request.contextPath}/handlePatient?action=booking">Booking</a>
        <a href="${pageContext.request.contextPath}/handlePatient?action=walkin">Walk In</a>
        <a href="${pageContext.request.contextPath}/handlePatient?action=service">Service</a>
        <a href="${pageContext.request.contextPath}/handlePatient?action=notification">Notification</a>
        <a href="${pageContext.request.contextPath}/handlePatient?action=profile">Profile</a>
    </div>

    <input type="submit" class="logout-btn" name="logoutButton" value="Logout"/>
</form>
<%@ taglib uri="/WEB-INF/tlds/ict-taglib.tld" prefix="ict" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>