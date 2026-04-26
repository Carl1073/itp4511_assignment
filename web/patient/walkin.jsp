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

                    async function callTTS(currentQueueNumber) {
                        // 1. Submit TTS request
                        const API_KEY = 'sk-tts-8f4ee1e79b3849faae4a10fd67636017';
                        const BASE = 'https://api.tts.ai';
                        const resp = await fetch(BASE + '/v1/tts/', {
                            method: "POST",
                            headers: {
                                "Content-Type": "application/json",
                                "Authorization": 'Bearer ' + API_KEY
                            },
                            body: JSON.stringify({
                                model: "piper",
                                text: "Ticket number " + currentQueueNumber + ", please proceed.",
                                voice: "Amy (US)"
                            })
                        });
                        const data = await resp.json();
                        const uuid = data.uuid;

                        // 2. Poll for result
                        let result;
                        while (true) {
                            const poll = await fetch(BASE + '/v1/speech/results/?uuid=' + uuid, {
                                headers: { "Authorization": 'Bearer ' + API_KEY }
                            });
                            result = await poll.json();

                            if (result.status === "completed") {
                                const audioResp = await fetch(result.result_url);
                                const blob = await audioResp.blob();
                                const url = URL.createObjectURL(blob);
                                const player = document.getElementById("player");
                                player.src = url;
                                player.play();
                                break;
                            } else if (result.status === "failed") {
                                console.error("Generation failed:", result.error);
                                break;
                            }
                            await new Promise(r => setTimeout(r, 1500));
                        }
                    }

                    async function updateQueueStats() {
                        var clinicId = $('#clinicId').val();
                        var serviceId = $('#serviceId').val();
                        var clinicName = $("#clinicId option:selected").text();
                        var serviceName = $("#serviceId option:selected").text();
                        if (clinicId && serviceId) {
                            const response = await $.post('handlePatient', {
                                action: 'getQueueStatus',
                                clinicId,
                                serviceId
                            });

                            const { current, latest, currentQueueNumber, estimatedMinutes } = response;

                            $('#currentProgressDisplay').text(current);
                            $('#latestJoinedDisplay').text(latest);
                            $('#currentQueueNumber').text(currentQueueNumber);
                            $('#statusDetail').text("* Showing status for " + serviceName + " in " + clinicName);

                            // Update estimated time
                            updateEstimatedTime(estimatedMinutes, current);

                            if (!isAlerted && current === currentQueueNumber) {
                                callTTS(currentQueueNumber);
                                isAlerted = true;
                                alert("Dear patient, your ticket #" + currentQueueNumber + " is now being called. Please make your way to the clinic.");
                            }
                        }
                    }

                    let countdownInterval;
                    let currentEstimatedMinutes = 0;

                    function updateEstimatedTime(estimatedMinutes, currentProgress) {
                        currentEstimatedMinutes = estimatedMinutes;
                        displayEstimatedTime();

                        // Clear existing countdown
                        if (countdownInterval) {
                            clearInterval(countdownInterval);
                        }

                        // Start countdown if there's time remaining
                        if (currentEstimatedMinutes > 0) {
                            currentEstimatedMinutes--; // Decrement immediately for more accurate display
                            countdownInterval = setInterval(() => {
                                currentEstimatedMinutes--;
                                currentEstimatedMinutes = Math.max(0, currentEstimatedMinutes - 1);
                                displayEstimatedTime();

                                // Stop countdown when it reaches 0
                                if (currentEstimatedMinutes <= 0) {
                                    clearInterval(countdownInterval);
                                }
                            }, 60000); // Update every minute (60,000 milliseconds)
                        }
                    }

                    function displayEstimatedTime() {
                        const display = $('#estimatedTimeDisplay');
                        if (currentEstimatedMinutes > 0) {
                            const hours = Math.floor(currentEstimatedMinutes / 60);
                            const minutes = currentEstimatedMinutes % 60;
                            let timeString = '';
                            if (hours > 0) {
                                timeString += hours + 'h ';
                            }
                            timeString += minutes + 'm';
                            display.text(timeString).css('color', '#e74c3c');
                        } else if ($('#currentQueueNumber').text() !== '--') {
                            display.text('Your turn!').css('color', '#27ae60');
                        } else {
                            display.text('--').css('color', '#666');
                        }
                    }

                    setInterval(async () => {
                        await updateQueueStats();
                    }, 500);

                    $('#walkinForm').on('submit', function (e) {
                        e.preventDefault();

                        const $currentQueueNumber = $('#currentQueueNumber');

                        if ($currentQueueNumber.text() !== '--') {
                            alert("Sorry, you already have a queue number for this service.");
                            return;
                        }

                        // If allowed, submit via AJAX or manually
                        this.submit(); // native form submit, bypasses jQuery handler
                    });

                    // Update when either dropdown changes
                    $('#clinicId, #serviceId').on('change', updateQueueStats);

                    // Initialize estimated time display
                    displayEstimatedTime();

                    // Cleanup on page unload
                    $(window).on('beforeunload', function() {
                        if (countdownInterval) {
                            clearInterval(countdownInterval);
                        }
                    });
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
                                    <div style="text-align: center;">
                                        <p style="margin: 0; color: #666;">Estimated Wait Time</p>
                                        <h2 id="estimatedTimeDisplay"
                                            style="color: #666; font-size: 2.5em; margin: 10px 0;">--</h2>
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
                                    <audio id="player" style="display: none;"></audio>
                                    <input type="submit" value="Get Queue Number"
                                        style="padding: 10px 20px; background: #3498db; color: white; border: none; border-radius: 4px; cursor: pointer;">
                                </form>

                                <p style="margin-top: 20px;"><a href="handlePatient?action=profile">Back to Profile</a>
                                </p>
                </div>
        </body>

        </html>