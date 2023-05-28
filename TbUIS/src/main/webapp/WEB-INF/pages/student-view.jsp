<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 26.08.2017
  Time: 15:58
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title><spring:message code="stu.view.title"/></title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="<spring:url value="/css/bootstrap.min.css"/>">
    <!-- Default stylesheet -->
    <link rel="stylesheet" href="<spring:url value="/css/style.css"/>">
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
        <div id="stu.${view}.successAlert" class="alert alert-success" role="alert">
            <strong><spring:message code="alert.success"/></strong> ${successMessage}
        </div>
    </c:if>
    <!-- Error alerts -->
    <c:if test="${not empty errorMessage}">
        <div id="stu.${view}.errorAlert" class="alert alert-danger" role="alert">
            <strong><spring:message code="alert.error"/></strong> ${errorMessage}
        </div>
    </c:if>
    <!-- Warning alerts -->
    <c:if test="${not empty warningMessage}">
        <div id="stu.${view}.warningAlert" class="alert alert-warning" role="alert">
            <strong><spring:message code="alert.warning"/></strong> ${warningMessage}
        </div>
    </c:if>

    <h1 id="stu.view.title"><c:if test="${empty title}"><spring:message code="stu.view.title"/></c:if><c:if test="${not empty title}">${title}</c:if></h1>
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
                    <c:import url="modules/student-views/list-of-register-subjects.jsp"/>
                </c:when>
                <c:when test="${view eq 'otherSubjects'}">
                    <c:import url="modules/student-views/list-of-subjects.jsp"/>
                </c:when>
                <c:when test="${view eq 'myExamDates'}">
                    <c:import url="modules/student-views/list-registred-examination-dates.jsp"/>
                </c:when>
                <c:when test="${view eq 'otherExamDates'}">
                    <c:import url="modules/student-views/list-of-examination-dates.jsp"/>
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