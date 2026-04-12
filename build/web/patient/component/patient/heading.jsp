<form method="post" action="${pageContext.request.contextPath}/loginController">
    <input type="hidden" name="action" value="logout"/>
    
    <a href="${pageContext.request.contextPath}/patient/patientHome.jsp">Home</a>
    <a href="${pageContext.request.contextPath}/patient/booking.jsp">Booking</a>

    <input type="submit" name="logoutButton" value="Logout"/>
</form>