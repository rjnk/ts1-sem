<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="cz.zcu.kiv.matyasj.dp.utils.properties.BasePropertyLoader" %>
<%@ page import="cz.zcu.kiv.matyasj.dp.utils.properties.PropertyLoader" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%
    PropertyLoader propertyLoader = new BasePropertyLoader();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Constraints</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="<spring:url value="css/bootstrap.min.css"/>">
    <!-- Default stylesheet -->
    <link rel="stylesheet" href="<spring:url value="css/style.css"/>">
    <link rel="shortcut icon" href="<spring:url value="images/favico-uis.ico"/>" type="image/x-icon">
</head>

<body>
<!-- Top Menu -->
<c:import url="WEB-INF/pages/modules/menus/menu.jsp"/>
<div id="main-content" class="container">
    <h3>Constraints</h3>

    <div class="row">
        <div class="col-xl-6">
            <table class="table table-hover">
                <thead class="thead-inverse">
                <tr class="table-primary">
                    <th colspan="3"><strong>Limitations regarding university</strong></th>
                </tr>
                <tr>
                    <th>Name</th>
                    <th>Limit</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Maximal number of subjects for students</td>
                    <td><%= propertyLoader.getProperty("studentMaxSubjects") %></td>
                </tr>
                <tr>
                    <td>Maximal number of subjects for teachers</td>
                    <td><%= propertyLoader.getProperty("teacherMaxSubjects") %></td>
                </tr>
                <tr>
                    <td>Maximal number of teachers for subject</td>
                    <td><%= propertyLoader.getProperty("subjectMaxTeachers") %></td>
                </tr>
                <tr>
                    <td>Maximal number of exam dates for student per subject
                        (only one - the oldest - can be active at one moment)</td>
                    <td><%= propertyLoader.getProperty("studentMaxExamDate") %></td>
                </tr>
                <tr>
                    <td>Maximal number of exam dates for teacher per subject
                        (all of them can be active at one moment)</td>
                    <td><%= propertyLoader.getProperty("subjectMaxExamDate") %></td>
                </tr>
                <tr>
                    <td>Minimal number of participants for exam date</td>
                    <td><%= propertyLoader.getProperty("examTermMinParticipants") %></td>
                </tr>
                <tr>
                    <td>Maximal number of participants for exam date</td>
                    <td><%= propertyLoader.getProperty("examTermMaxParticipants") %></td>
                </tr>

                </tbody>
            </table>
        </div>

        <div class="col-xl-6">
            <table class="table table-hover">
                <thead class="thead-inverse">
                <tr class="table-primary">
                    <th colspan="3"><strong>Limitations regarding users</strong></th>
                </tr>
                <tr>
                    <th>Name</th>
                    <th>Limit</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Minimal length of first name</td>
                    <td><%= propertyLoader.getProperty("minFirstNameLength") %></td>
                </tr>
                <tr>
                    <td>Maximal length of first name</td>
                    <td><%= propertyLoader.getProperty("maxFirstNameLength") %></td>
                </tr>
                <tr>
                    <td>Minimal length of last name</td>
                    <td><%= propertyLoader.getProperty("minLastNameLength") %></td>
                </tr>
                <tr>
                    <td>Maximal length of last name</td>
                    <td><%= propertyLoader.getProperty("maxLastNameLength") %></td>
                </tr>
                <tr>
                    <td>Minimal length of mail</td>
                    <td><%= propertyLoader.getProperty("minEmailLength") %></td>
                </tr>
                <tr>
                    <td>Maximal length of mail</td>
                    <td><%= propertyLoader.getProperty("maxEmailLength") %></td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>

</div>


<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<!-- Bootstrap 4 -->
<script src="<spring:url value="js/jquery-3.1.1.slim.min.js"/>"></script>
<script src="<spring:url value="js/popper.min.js"/>"></script>
<script src="<spring:url value="js/bootstrap.min.js"/>"></script>

</body>
</html>
