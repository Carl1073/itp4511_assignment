<%@page contentType="text/html" pageEncoding="UTF-8" %>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <!DOCTYPE html>
        <html>

        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>User Profile</title>
            <style>
                .form-row {
                    margin-bottom: 15px;
                }

                label {
                    display: inline-block;
                    width: 120px;
                }

                .error {
                    color: red;
                }

                .success {
                    color: green;
                }
            </style>
            <script src="https://cdnjs.cloudflare.com/ajax/libs/jspdf/2.5.1/jspdf.umd.min.js"></script>
            <script
                src="https://cdnjs.cloudflare.com/ajax/libs/jspdf-autotable/3.5.25/jspdf.plugin.autotable.min.js"></script>

            <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
            <script>
                $(window).on('load', () => {
                    const mrbs = [];
                    <c:forEach var="mrb" items="${mrbs}">
                        mrbs.push({
                            "clinicName": "${mrb.getClinicName()}",
                        "monthlyPrice": 'HKD$'+ ${mrb.getMonthlyPrice()},
                        "monthly": new Date(0, ${mrb.getMonthly()} - 1).toLocaleDateString('en-US', {month: 'long' })
                            });
                    </c:forEach>

                    const yrbs = [];
                    <c:forEach var="yrb" items="${yrbs}">
                        yrbs.push({
                            "clinicName": "${yrb.getClinicName()}",
                        "yearlyPrice": 'HKD$'+ ${yrb.getYearlyPrice()},
                        "yearly": ${yrb.getYearly()}
                            });
                    </c:forEach>

                    $('#btnGenerateMRBS').click(() => {
                        exportPDF(['ClinicName', 'MonthlyPrice', 'Month'], mrbs.map(i => Object.values(i)), 'Monthly Clinic Report');
                        console.log(mrbs);
                    });

                    $('#btnGenerateYRBS').click(() => {
                        exportPDF(['ClinicName', 'YearlyPrice', 'Year'], yrbs.map(i => Object.values(i)), 'Yearly Clinic Report');
                        console.log(yrbs);
                    });


                    function exportPDF(heading, rows, fileName) {
                        const { jsPDF } = window.jspdf;
                        const doc = new jsPDF();

                        doc.autoTable({
                            head: [heading],
                            body: rows,
                            theme: "grid"
                        });

                        doc.save(fileName + ".pdf");
                    }

                    (() => {
                        const $mrbsTable = $('#mrbsTable');
                        const $yrbsTable = $('#yrbsTable');

                        for (const mrb of mrbs) {
                            const { clinicName, monthlyPrice, monthly } = mrb;
                            $mrbsTable.append('<tr><td>' + clinicName + '</td><td>' + monthlyPrice + '</td><td>' + monthly + '</td></tr>');
                        }

                        for (const yrb of yrbs) {
                            const { clinicName, yearlyPrice, yearly } = yrb;
                            $yrbsTable.append('<tr><td>' + clinicName + '</td><td>' + yearlyPrice + '</td><td>' + yearly + '</td></tr>');
                        }
                    })();
                })
            </script>
        </head>

        <body>
            <%@ include file="component/heading.jsp" %>
                <div style="display: flex; gap: 3em; width: 50%; height: 20em;">
                    <div style="width: 100%; height: 100%;">
                        <table style="height: 100%;" id="mrbsTable">
                            <thead>
                                <tr>
                                    <td colspan="3" style="text-align: center;">Month</td>
                                </tr>
                            </thead>
                            <tr>
                                <td>ClinicName</td>
                                <td>MonthlyPrice</td>
                                <td>Monthly</td>
                            </tr>
                            <!-- <c:forEach var="mrb" items="${mrbs}">
                                <tr>
                                    <td>${mrb.getClinicName()}</td>
                                    <td>HKD$${mrb.getMonthlyPrice()}</td>
                                    <td>${mrb.getMonthly()}</td>
                                </tr>
                            </c:forEach> -->
                        </table>
                        <input type="button" value="Export Monthly Report" id="btnGenerateMRBS"
                            style="padding: 1em; border-radius: 1em; border: solid 1px #bbb;">
                    </div>
                    <div style="width: 100%; height: 100%;">
                        <table style="height: 100%;" id="yrbsTable">
                            <thead>
                                <tr>
                                    <td colspan="3" style="text-align: center;">Yearly</td>
                                </tr>
                            </thead>
                            <tr>
                                <td>ClinicName</td>
                                <td>YearlyPrice</td>
                                <td>Yearly</td>
                            </tr>
                            <!-- <c:forEach var="yrb" items="${yrbs}">
                                <tr>
                                    <td>${yrb.getClinicName()}</td>
                                    <td>HKD$${yrb.getYearlyPrice()}</td>
                                    <td>${yrb.getYearly()}</td>
                                </tr>
                            </c:forEach> -->
                        </table>
                        <input type="button" value="Export Yearly Report" id="btnGenerateYRBS"
                            style="padding: 1em; border-radius: 1em; border: solid 1px #bbb;">
                    </div>
                </div>
        </body>

        </html>