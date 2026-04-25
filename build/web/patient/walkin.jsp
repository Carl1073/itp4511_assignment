<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Walk-in Queue</title>
            <link rel="stylesheet" type="text/css" href="css/style.css">
            <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
            <script>
                $(window).on('load', function () {
                    let isAlerted = false;

                    async function updateQueueStats() {
                        var clinicId = $('#clinicId').val();
                        var serviceId = $('#serviceId').val();
                        var clinicName = $("#clinicId option:selected").text();
                        var serviceName = $("#serviceId option:selected").text();
                        if (clinicId && serviceId) {
                            const { current, latest, currentQueueNumber } = await $.post('handlePatient', {
                                action: 'getQueueStatus',
                                clinicId,
                                serviceId
                            });

                            $('#currentProgressDisplay').text(current);
                            $('#latestJoinedDisplay').text(latest);
                            $('#currentQueueNumber').text(currentQueueNumber);
                            $('#statusDetail').text("* Showing status for " + serviceName + " in " + clinicName);

                            if (!isAlerted && current === currentQueueNumber) {
                                alert("Dear patient, your ticket #" + currentQueueNumber + " is now being called. Please make your way to the clinic.");
                                isAlerted = true;
                            }
                        }
                    }

                    setInterval(async () => {
                        await updateQueueStats();
                    }, 500);

                    // Update when either dropdown changes
                    $('#clinicId, #serviceId').on('change', updateQueueStats);
                });
            </script>
        </head>

        <body>
            <%@ include file="component/heading.jsp" %>
                <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session" />

                <div style="padding: 20px;">
                    <h2>Join Walk-in Queue</h2>

                    <%-- Success/Error Message Display --%>
                        <c:if test="${not empty sessionScope.msg}">
                            <div class="alert"
                                style="background: #d4edda; color: #155724; padding: 10px; border-radius: 5px; margin-bottom: 15px;">
                                ${sessionScope.msg}
                            </div>
                            <c:remove var="msg" scope="session" />
                        </c:if>

                        <%-- Live Status Board --%>
                            <div class="queue-status-board"
                                style="background: #f4f4f4; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #ddd;">
                                <h3 style="margin-top: 0;">Live Queue Status</h3>
                                <div style="display: flex; gap: 40px;">
                                    <div style="text-align: center;">
                                        <p style="margin: 0; color: #666;">Current Progress</p>
                                        <h2 id="currentProgressDisplay"
                                            style="color: #2ecc71; font-size: 2.5em; margin: 10px 0;">${currentProgress
                                            != null ? currentProgress : '--'}</h2>
                                    </div>
                                    <div style="text-align: center;">
                                        <p style="margin: 0; color: #666;">Latest Number Issued</p>
                                        <h2 id="latestJoinedDisplay"
                                            style="color: #3498db; font-size: 2.5em; margin: 10px 0;">${latestJoined !=
                                            null ? latestJoined : '--'}</h2>
                                    </div>
                                    <div style="text-align: center;">
                                        <p style="margin: 0; color: #666;">Your Queue Number</p>
                                        <h2 id="currentQueueNumber"
                                            style="color: #3498db; font-size: 2.5em; margin: 10px 0;">--</h2>
                                    </div>
                                </div>
                                <small id="statusDetail" style="color: #888;">* Select a clinic and service to see live
                                    numbers.</small>
                            </div>

                            <%-- Queue Entry Form --%>
                                <form id="walkinForm" action="handlePatientService" method="POST">
                                    <input type="hidden" name="action" value="joinQueue">

                                    <div style="margin-bottom: 15px;">
                                        <label style="display: block; font-weight: bold;">Select Clinic:</label>
                                        <select name="clinicId" id="clinicId" style="padding: 8px; width: 300px;"
                                            required>
                                            <option value="">-- Select Clinic --</option>
                                            <c:forEach var="clinic" items="${clinics}">
                                                <c:if test="${clinic.isWalkinEnabled}">
                                                    <option value="${clinic.clinicId}" <c:if
                                                        test="${param.clinicId == clinic.clinicId}">selected
                                                </c:if>>${clinic.clinicName}</option>
                                                </c:if>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <div style="margin-bottom: 15px;">
                                        <label style="display: block; font-weight: bold;">Select Service:</label>
                                        <select name="serviceId" id="serviceId" style="padding: 8px; width: 300px;"
                                            required>
                                            <option value="">-- Select Service --</option>
                                            <c:forEach var="service" items="${services}">
                                                <option value="${service.serviceId}" <c:if
                                                    test="${param.serviceId == service.serviceId}">selected</c:if>>
                                                    ${service.serviceName}
                                                </option>
                                            </c:forEach>
                                        </select>
                                    </div>

                                    <input type="submit" value="Get Queue Number"
                                        style="padding: 10px 20px; background: #3498db; color: white; border: none; border-radius: 4px; cursor: pointer;">
                                </form>

                                <p style="margin-top: 20px;"><a href="handlePatient?action=profile">Back to Profile</a>
                                </p>
                </div>
        </body>

        </html>