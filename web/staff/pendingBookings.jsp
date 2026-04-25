<%-- Document : clientHome Created on : 2026年3月24日, 下午08:54:57 Author : Tong --%>
    <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

        <%@page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>Home page</title>
                <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
                <script>
                    $(window).on('load', () => {
                        async function updateQueueStats() {
                            const $clinicId = $('#clinicId');
                            const $serviceId = $('#serviceId');
                            const $statusDetail = $('#statusDetail');
                            if ($clinicId.val() && $serviceId.val()) {
                                const data = await $.post('handlePatient', {
                                    action: 'getQueueStatus',
                                    clinicId: $clinicId.val(),
                                    serviceId: $serviceId.val()
                                });
                                const { current, latest } = data;
                                $('#currentProgressDisplay').text(current);
                                $('#latestJoinedDisplay').text(latest);
                                $statusDetail.text('* Showing status for ' + $clinicId.find('option:selected').text() + ' in ' + $serviceId.find('option:selected').text());
                            } else {
                                $statusDetail.text("* Select a clinic and service to see live numbers.");
                            }
                        }

                        setInterval(async () => {
                            await updateQueueStats();
                        }, 1000);

                        $('#clinicId, #serviceId').on('change', updateQueueStats);

                        $('.btnOperate').click(async function () {
                            const $this = $(this);
                            const $clinicId = $('#clinicId');
                            const $serviceId = $('#serviceId');
                            if (!$clinicId.val() || !$serviceId.val()) return;
                            const data = await $.post('handleStaffProcess', {
                                action: $this.val().toLowerCase() === 'next' ? 'nextQueueNumber' : 'skipQueueNumber',
                                clinicId: $clinicId.val(),
                                serviceId: $serviceId.val()
                            });
                            updateQueueStats();
                        });
                    });
                </script>
            </head>

            <body>
                <%@ include file="component/heading.jsp" %>
                    <jsp:useBean id="userBean" class="ict.bean.UserBean" scope="session" />
                    <ict:hello name="${userBean.fullName}" />
                    <h1>This is home page.</h1>
                    <div class="queue-status-board"
                        style="background: #f4f4f4; padding: 15px; border-radius: 8px; margin-bottom: 20px; border: 1px solid #ddd;">
                        <h3 style="margin-top: 0;">Live Queue Status</h3>
                        <div style="display: flex; gap: 40px;">
                            <div style="text-align: center;">
                                <p style="margin: 0; color: #666;">Current Progress</p>
                                <h2 id="currentProgressDisplay"
                                    style="color: #2ecc71; font-size: 2.5em; margin: 10px 0;">${currentProgress != null
                                    ? currentProgress : '--'}</h2>
                            </div>
                            <div style="text-align: center;">
                                <p style="margin: 0; color: #666;">Latest Number Issued</p>
                                <h2 id="latestJoinedDisplay" style="color: #3498db; font-size: 2.5em; margin: 10px 0;">
                                    ${latestJoined != null ? latestJoined : '--'}</h2>
                            </div>
                        </div>
                        <small id="statusDetail" style="color: #888;">* Select a clinic and service to see live
                            numbers.</small>
                    </div>
                    <div style="margin-bottom: 15px;">
                        <label style="display: block; font-weight: bold;">Select Clinic:</label>
                        <select name="clinicId" id="clinicId" style="padding: 8px; width: 300px;" required>
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
                        <select name="serviceId" id="serviceId" style="padding: 8px; width: 300px;" required>
                            <option value="">-- Select Service --</option>
                            <c:forEach var="service" items="${services}">
                                <option value="${service.serviceId}" <c:if
                                    test="${param.serviceId == service.serviceId}">selected</c:if>>
                                    ${service.serviceName}
                                </option>
                            </c:forEach>
                        </select>
                    </div>
                    <div>
                        <input type="button" class="btnOperate" value="Next"
                            style="padding: 1.3em 2em; background: #82a9ff; color: white; border: none; border-radius: 4px; cursor: pointer;">
                        <input type="button" class="btnOperate" value="Skip"
                            style="padding: 1.3em 2em; background: #ff5e5e; color: white; border: none; border-radius: 4px; cursor: pointer;">
                    </div>
            </body>

            </html>