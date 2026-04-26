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
        </head>

        <body>
            <%@ include file="component/heading.jsp" %>
                <table>
                    <tr>
                        <td>ClinicName</td>
                        <td>MonthlyPrice</td>
                        <td>Monthly</td>
                    </tr>
                    <c:forEach var="mrb" items="mrbs">
                        <tr>
                            <td>${mrb.clinicName}</td>
                            <td>${mrb.monthlyPrice}</td>
                            <td>${mrb.monthly}</td>
                        </tr>
                    </c:forEach>
                </table>
                <table>
                    <tr>
                        <td>ClinicName</td>
                        <td>YearlyPrice</td>
                        <td>Yearly</td>
                    </tr>
                    <c:forEach var="yrb" items="yrbs">
                        <tr>
                            <td>${yrb.clinicName}</td>
                            <td>${yrb.yearlyPrice}</td>
                            <td>${yrb.yearly}</td>
                        </tr>
                    </c:forEach>
                </table>
        </body>

        </html>