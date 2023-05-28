<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 13.09.2017
  Time: 11:54
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title><spring:message code="tea.view.title"/></title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="<spring:url value="/css/bootstrap.min.css"/>">
    <!-- Default stylesheet -->
    <link rel="stylesheet" href="<c:url value="/css/style.css"/>">
    <link rel="shortcut icon" href="<spring:url value="/images/favico-uis.ico"/>" type="image/x-icon">
</head>
<body>
<!-- Top Menu -->
<c:import url="modules/menus/menu.jsp"/>
<!-- Page content -->
<div id="main-content" class="container">
    <!-- ALERTS -->
    <!-- Success alerts -->
    <c:if test="${not empty successMessage}">
        <div id="tea.${view}.successAlert" class="alert alert-success" role="alert">
            <strong><spring:message code="alert.success"/></strong> ${successMessage}
        </div>
    </c:if>
    <!-- Error alerts -->
    <c:if test="${not empty errorMessage}">
        <div id="tea.${view}.errorAlert" class="alert alert-danger" role="alert">
            <strong><spring:message code="alert.error"/></strong> ${errorMessage}
        </div>
    </c:if>
    <!-- Warning alerts -->
    <c:if test="${not empty warningMessage}">
        <div id="tea.${view}.warningAlert" class="alert alert-warning" role="alert">
            <strong><spring:message code="alert.warning"/></strong> ${warningMessage}
        </div>
    </c:if>

    <h1 id="tea.view.title"><spring:message code="tea.view.title"/></h1>
    <div class="row">
        <div id="side-panel" class="col-4">
            <c:import url="modules/menus/side-menu.jsp"/>
        </div>

        <div id="content" class="col-8">
            <!-- Actual view -->
            <c:choose>
                <c:when test="${view eq 'overview'}">
                    <c:import url="modules/views/overview.jsp"/>
                </c:when>
                <c:when test="${view eq 'mySubjects'}">
                    <c:import url="modules/teacher-views/list-of-taught-subject.jsp"/>
                </c:when>
                <c:when test="${view eq 'myExamDates'}">
                    <c:import url="modules/teacher-views/list-of-exam-dates.jsp"/>
                </c:when>
                <c:when test="${view eq 'newExamDates'}">
                    <c:import url="modules/teacher-views/new-exam-dates.jsp"/>
                </c:when>
                <c:when test="${view eq 'setEvaluation'}">
                    <c:import url="modules/teacher-views/new-evaluation.jsp"/>
                </c:when>
                <c:when test="${view eq 'otherSubjects'}">
                    <c:import url="modules/teacher-views/list-of-not-taught-subjects.jsp"/>
                </c:when>
                <c:when test="${view eq 'listOfAllTeachers'}">
                    <c:import url="modules/teacher-views/list-of-all-teachers.jsp"/>
                </c:when>
                <c:when test="${view eq 'evaluationTable'}">
                    <c:import url="modules/teacher-views/evaluation-table.jsp"/>
                </c:when>
                <c:otherwise>
                    <c:import url="modules/views/overview.jsp"/>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="<spring:url value="/js/jquery-3.1.1.slim.min.js"/>"></script>
<script src="<spring:url value="/js/popper.min.js"/>"></script>
<script src="<spring:url value="/js/bootstrap.min.js"/>"></script>

</body>
</html>
