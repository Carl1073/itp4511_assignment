<%-- 
    Document   : visitOutcome
    Created on : 2026年3月24日, 下午08:54:57
    Author     : Tong
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Visit Outcomes - Staff Panel</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/styles.css">
        <style>
            .outcomes-container { max-width: 1200px; margin: 0 auto; padding: 20px; }
            .appointment-table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
            .appointment-table th, .appointment-table td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
            .appointment-table th { background: #00509f; font-weight: bold; }
            .status-form { display: flex; align-items: center; gap: 10px; }
            .status-select { padding: 6px; border: 1px solid #ddd; border-radius: 4px; }
            .cancel-reason-container { display: none; margin-top: 10px; }
            .cancel-reason-container.show { display: block; }
            .reason-dropdown { padding: 6px; border: 1px solid #ddd; border-radius: 4px; margin-bottom: 5px; }
            .reason-textarea { width: 100%; padding: 6px; border: 1px solid #ddd; border-radius: 4px; resize: vertical; }
            .update-btn { padding: 6px 12px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer; }
            .update-btn:hover { background: #0056b3; }
            .status-badge { padding: 4px 8px; border-radius: 4px; font-size: 0.9em; font-weight: bold; }
            .status-confirmed { background: #d4edda; color: #155724; }
            .status-arrived { background: #fff3cd; color: #856404; }
            .status-completed { background: #d1ecf1; color: #0c5460; }
            .status-noshow { background: #f8d7da; color: #721c24; }
            .status-cancelled { background: #e2e3e5; color: #383d41; }
        </style>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>
        <ict:hello name="${userBean.fullName}"/>

        <div class="outcomes-container">
            <h1>Visit Outcomes - ${clinicName}</h1>
            <p>Today's Date: <fmt:formatDate value="<%= new java.util.Date() %>" pattern="yyyy-MM-dd"/></p>

            <c:if test="${param.status == 'success'}">
                <div style="background: #d4edda; color: #155724; padding: 10px; border-radius: 4px; margin-bottom: 20px;">
                    Visit outcome updated successfully!
                </div>
            </c:if>
            <c:if test="${param.status == 'error'}">
                <div style="background: #f8d7da; color: #721c24; padding: 10px; border-radius: 4px; margin-bottom: 20px;">
                    Error updating visit outcome. Please try again.
                </div>
            </c:if>

            <c:if test="${not empty todaysAppointments}">
                <table class="appointment-table">
                    <thead>
                        <tr>
                            <th>Appointment ID</th>
                            <th>Patient Name</th>
                            <th>Service</th>
                            <th>Time</th>
                            <th>Current Status</th>
                            <th>Update Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="appointment" items="${todaysAppointments}">
                            <tr>
                                <td>${appointment.appointmentId}</td>
                                <td>${appointment.patientName}</td>
                                <td>${appointment.serviceName}</td>
                                <td><fmt:formatDate value="${appointment.date}" pattern="HH:mm"/></td>
                                <td>
                                    <span class="status-badge status-${appointment.status.toLowerCase().replace(' ', '').replace('-', '')}">
                                        <c:choose>
                                            <c:when test="${appointment.status == 'Cancelled' && not empty appointment.cancelReason && appointment.cancelReason == 'Cancelled by clinic'}">
                                                Cancelled by clinic
                                            </c:when>
                                            <c:otherwise>
                                                ${appointment.status}
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </td>
                                <td>
                                    <form action="${pageContext.request.contextPath}/handleStaffProcess" method="POST" class="status-form">
                                        <input type="hidden" name="action" value="updateVisitOutcome">
                                        <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">

                                        <select name="status" class="status-select" onchange="toggleCancelReason(this)">
                                            <option value="">Select Status</option>
                                            <option value="Completed" ${appointment.status == 'Completed' ? 'selected' : ''}>Completed</option>
                                            <option value="No-show" ${appointment.status == 'No-show' ? 'selected' : ''}>No-show</option>
                                            <option value="Cancelled by clinic" ${appointment.status == 'Cancelled' && appointment.cancelReason == 'Cancelled by clinic' ? 'selected' : ''}>Cancelled by clinic</option>
                                        </select>

                                        <div class="cancel-reason-container" id="cancel-reason-${appointment.appointmentId}">
                                            <select name="cancelReasonType" class="reason-dropdown" onchange="toggleCustomReason(this, ${appointment.appointmentId})">
                                                <option value="">Select Reason</option>
                                                <option value="Staff unavailable">Staff unavailable</option>
                                                <option value="Equipment malfunction">Equipment malfunction</option>
                                                <option value="Emergency closure">Emergency closure</option>
                                                <option value="Patient no-show">Patient no-show</option>
                                                <option value="Other">Other (specify below)</option>
                                            </select>
                                            <textarea name="customCancelReason" class="reason-textarea" id="custom-reason-${appointment.appointmentId}" placeholder="Enter custom cancellation reason..." rows="2"></textarea>
                                        </div>

                                        <button type="submit" class="update-btn">Update</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:if>

            <c:if test="${empty todaysAppointments}">
                <p>No appointments scheduled for today at your clinic.</p>
            </c:if>
        </div>

        <script>
            function toggleCancelReason(selectElement) {
                const appointmentId = selectElement.form.appointmentId.value;
                const cancelReasonContainer = document.getElementById('cancel-reason-' + appointmentId);

                if (selectElement.value === 'Cancelled by clinic') {
                    cancelReasonContainer.classList.add('show');
                } else {
                    cancelReasonContainer.classList.remove('show');
                }
            }

            function toggleCustomReason(selectElement, appointmentId) {
                const customReasonTextarea = document.getElementById('custom-reason-' + appointmentId);

                if (selectElement.value === 'Other') {
                    customReasonTextarea.style.display = 'block';
                    customReasonTextarea.required = true;
                } else {
                    customReasonTextarea.style.display = 'none';
                    customReasonTextarea.required = false;
                    customReasonTextarea.value = '';
                }
            }

            // Initialize on page load
            document.addEventListener('DOMContentLoaded', function() {
                const statusSelects = document.querySelectorAll('.status-select');
                statusSelects.forEach(function(select) {
                    if (select.value === 'Cancelled by clinic') {
                        toggleCancelReason(select);
                    }
                });
            });
        </script>
</html>
