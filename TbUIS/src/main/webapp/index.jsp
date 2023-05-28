<%--
  Author: Jiri Matyas
  Date: 13.08.2017
  Time: 18:22
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title><spring:message code="homePage.title"/></title>

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
    <c:if test="${not empty param.successMessage}">
        <div id="homePage.successAlert" class="alert alert-success" role="alert">
            <strong><spring:message code="alert.success"/></strong> ${param.successMessage}
        </div>
    </c:if>
    <div class="row">
        <div class="col-md-8">
            <div id="homePage.warningAlert" class="alert alert-warning" role="alert">
                <strong><spring:message code="alert.warning"/></strong> <spring:message code="homePage.notRealWarning"/>
            </div>
            <h1><spring:message code="homePage.title"/></h1>
            <p class="lead"><spring:message code="homePage.welcomeText"/></p>
            <p>You can explore:</p>
            <ul>
                <li><a href="<c:url value="/use-cases.jsp"/>">Application use-cases</a></li>
                <li><a href="<c:url value="/db-content.jsp"/>">Database content</a></li>
                <li><a href="<c:url value="/constraints.jsp"/>">Application constraints</a></li>
            </ul>
        </div>
        <div class="col-md-4">
            <ul class="list-group">
                <li class="list-group-item"><spring:message code="homePage.sidePanel.buildNumberLabel"/> <span id="homePage.build-version" class="badge badge-pill badge-primary"><spring:eval expression="@propertyLoader.getProperty('project.version')" /></span></li>
                <li class="list-group-item"><spring:message code="homePage.sidePanel.errors"/> <span id="homePage.inject-version" class="badge badge-pill badge-primary"><spring:eval expression="@injectVersion" /></span></li>
                <li class="list-group-item"><spring:message code="homePage.sidePanel.termExamDate"/> <span id="homePage.exam-date" class="badge badge-pill"><spring:eval expression="@propertyLoader.getProperty('termExamDate')" /></span></li>
            </ul>
        </div>
    </div>
</div><!-- /.container -->
<!-- FOOTER -->
<footer class="footer">
    <div class="container text-center">
        <span class="text-muted">This application has been developed by Jiří Matyáš, Jakub Šmaus and Radek Vais supervised by Pavel Herout. University of West Bohemia. 2018-2020</span>
        <span class="text-muted">University of West Bohemia. 2018</span>
    </div>
</footer>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<!-- Bootstrap 4 -->
<script src="<spring:url value="js/jquery-3.1.1.slim.min.js"/>"></script>
<script src="<spring:url value="js/popper.min.js"/>"></script>
<script src="<spring:url value="js/bootstrap.min.js"/>"></script>

</body>
</html>