<%--
    Document   : reports
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
        <title>Reports - Admin Panel</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/styles.css">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <style>
            .reports-container { max-width: 1200px; margin: 0 auto; padding: 20px; }
            .filters { background: #f8f9fa; padding: 20px; border-radius: 8px; margin-bottom: 20px; }
            .filters h3 { margin-top: 0; }
            .filter-row { display: flex; gap: 15px; align-items: end; flex-wrap: wrap; }
            .filter-group { display: flex; flex-direction: column; }
            .filter-group label { font-weight: bold; margin-bottom: 5px; }
            .filter-group select, .filter-group input { padding: 8px; border: 1px solid #ddd; border-radius: 4px; }
            .report-tabs { display: flex; margin-bottom: 20px; }
            .tab-button { padding: 10px 20px; border: none; background: #e9ecef; cursor: pointer; margin-right: 5px; border-radius: 4px 4px 0 0; }
            .tab-button.active { background: #007bff; color: white; }
            .report-content { display: none; }
            .report-content.active { display: block; }
            .table-container { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin-bottom: 20px; }
            table { width: 100%; border-collapse: collapse; }
            th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }
            th { background: #484848; font-weight: bold; }
            .chart-container { background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); margin-bottom: 20px; }
            .chart-row { display: flex; gap: 20px; }
            .chart-item { flex: 1; }
            .utilisation-rate { font-weight: bold; color: #28a745; }
        </style>
    </head>
    <body>
        <%@ include file="component/heading.jsp" %>
        <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session"/>
        <ict:hello name="${userBean.fullName}"/>

        <div class="reports-container">
            <h1>Reports & Analytics</h1>

            <!-- Filters -->
            <div class="filters">
                <h3>Filters</h3>
                <form action="${pageContext.request.contextPath}/handleAdmin" method="GET">
                    <input type="hidden" name="action" value="reports">
                    <div class="filter-row">
                        <div class="filter-group">
                            <label for="clinicId">Clinic:</label>
                            <select name="clinicId" id="clinicId">
                                <option value="">All Clinics</option>
                                <c:forEach var="clinic" items="${clinicList}">
                                    <option value="${clinic.clinicId}" ${clinic.clinicId == selectedClinicId ? 'selected' : ''}>${clinic.clinicName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="filter-group">
                            <label for="serviceId">Service:</label>
                            <select name="serviceId" id="serviceId">
                                <option value="">All Services</option>
                                <c:forEach var="service" items="${serviceList}">
                                    <option value="${service.serviceId}" ${service.serviceId == selectedServiceId ? 'selected' : ''}>${service.serviceName}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="filter-group">
                            <label for="monthYear">Month/Year:</label>
                            <input type="month" name="monthYear" id="monthYear" value="${selectedMonthYear}">
                        </div>
                        <div class="filter-group">
                            <label for="status">Status:</label>
                            <select name="status" id="status">
                                <option value="">All Statuses</option>
                                <option value="Confirmed" ${'Confirmed' == selectedStatus ? 'selected' : ''}>Confirmed</option>
                                <option value="Arrived" ${'Arrived' == selectedStatus ? 'selected' : ''}>Arrived</option>
                                <option value="Completed" ${'Completed' == selectedStatus ? 'selected' : ''}>Completed</option>
                                <option value="No-show" ${'No-show' == selectedStatus ? 'selected' : ''}>No-show</option>
                                <option value="Cancelled" ${'Cancelled' == selectedStatus ? 'selected' : ''}>Cancelled</option>
                            </select>
                        </div>
                        <div class="filter-group">
                            <button type="submit" name="reportType" value="appointments" style="padding: 8px 16px; background: #007bff; color: white; border: none; border-radius: 4px; cursor: pointer;">View Appointments</button>
                        </div>
                        <div class="filter-group">
                            <button type="submit" name="reportType" value="utilisation" style="padding: 8px 16px; background: #28a745; color: white; border: none; border-radius: 4px; cursor: pointer;">Utilisation Report</button>
                        </div>
                        <div class="filter-group">
                            <button type="submit" name="reportType" value="noshows" style="padding: 8px 16px; background: #dc3545; color: white; border: none; border-radius: 4px; cursor: pointer;">No-show Report</button>
                        </div>
                    </div>
                </form>
            </div>

            <!-- Appointments Report -->
            <c:if test="${reportType == 'appointments'}">
                <div class="table-container">
                    <h3>Appointment Records</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>Appointment ID</th>
                                <th>Patient</th>
                                <th>Clinic</th>
                                <th>Service</th>
                                <th>Date</th>
                                <th>Time</th>
                                <th>Status</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="appointment" items="${appointments}">
                                <tr>
                                    <td>${appointment.appointmentId}</td>
                                    <td>${appointment.patientName}</td>
                                    <td>${appointment.clinicName}</td>
                                    <td>${appointment.serviceName}</td>
                                    <td><fmt:formatDate value="${appointment.date}" pattern="yyyy-MM-dd"/></td>
                                    <td>${appointment.openTime}</td>
                                    <td>${appointment.status}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty appointments}">
                                <tr>
                                    <td colspan="7" style="text-align: center;">No appointments found for the selected filters.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </c:if>

            <!-- Utilisation Report -->
            <c:if test="${reportType == 'utilisation'}">
                <div class="table-container">
                    <h3>Utilisation Rate Report - ${selectedMonthYear}</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>Clinic</th>
                                <th>Service</th>
                                <th>Total Slots</th>
                                <th>Booked Slots</th>
                                <th>Utilisation Rate</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="data" items="${utilisationData}">
                                <tr>
                                    <td>${data[0]}</td>
                                    <td>${data[1]}</td>
                                    <td>${data[3]}</td>
                                    <td>${data[4]}</td>
                                    <td class="utilisation-rate">
                                        <c:set var="rate" value="${data[3] > 0 ? (data[4] * 100.0 / data[3]) : 0}"/>
                                        <fmt:formatNumber value="${rate}" maxFractionDigits="1"/>%
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty utilisationData}">
                                <tr>
                                    <td colspan="5" style="text-align: center;">No utilisation data found for the selected month.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <!-- Utilisation Chart -->
                <div class="chart-container">
                    <h3>Utilisation Rate Chart</h3>
                    <canvas id="utilisationChart" width="400" height="200"></canvas>
                </div>
            </c:if>

            <!-- No-show Report -->
            <c:if test="${reportType == 'noshows'}">
                <div class="table-container">
                    <h3>No-show Summary - ${selectedMonthYear}</h3>
                    <table>
                        <thead>
                            <tr>
                                <th>Clinic</th>
                                <th>Service</th>
                                <th>No-show Count</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="data" items="${noShowData}">
                                <tr>
                                    <td>${data[0]}</td>
                                    <td>${data[1]}</td>
                                    <td>${data[3]}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty noShowData}">
                                <tr>
                                    <td colspan="3" style="text-align: center;">No no-show data found for the selected month.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>

                <!-- No-show Chart -->
                <div class="chart-container">
                    <h3>No-show Count Chart</h3>
                    <canvas id="noshowChart" width="400" height="200"></canvas>
                </div>
            </c:if>
        </div>

        <script>
            // Utilisation Chart
            <c:if test="${reportType == 'utilisation' && not empty utilisationData}">
                const utilisationCtx = document.getElementById('utilisationChart').getContext('2d');
                const utilisationData = [
                    <c:forEach var="data" items="${utilisationData}" varStatus="status">
                        {
                            label: '${data[0]} - ${data[1]}',
                            rate: ${data[3] > 0 ? (data[4] * 100.0 / data[3]) : 0}
                        }<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ];

                new Chart(utilisationCtx, {
                    type: 'bar',
                    data: {
                        labels: utilisationData.map(item => item.label),
                        datasets: [{
                            label: 'Utilisation Rate (%)',
                            data: utilisationData.map(item => item.rate),
                            backgroundColor: 'rgba(40, 167, 69, 0.6)',
                            borderColor: 'rgba(40, 167, 69, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        scales: {
                            y: {
                                beginAtZero: true,
                                max: 100
                            }
                        }
                    }
                });
            </c:if>

            // No-show Chart
            <c:if test="${reportType == 'noshows' && not empty noShowData}">
                const noshowCtx = document.getElementById('noshowChart').getContext('2d');
                const noshowData = [
                    <c:forEach var="data" items="${noShowData}" varStatus="status">
                        {
                            label: '${data[0]} - ${data[1]}',
                            count: ${data[3]}
                        }<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ];

                new Chart(noshowCtx, {
                    type: 'bar',
                    data: {
                        labels: noshowData.map(item => item.label),
                        datasets: [{
                            label: 'No-show Count',
                            data: noshowData.map(item => item.count),
                            backgroundColor: 'rgba(220, 53, 69, 0.6)',
                            borderColor: 'rgba(220, 53, 69, 1)',
                            borderWidth: 1
                        }]
                    },
                    options: {
                        scales: {
                            y: {
                                beginAtZero: true
                            }
                        }
                    }
                });
            </c:if>
        </script>
    </body>
</html>
