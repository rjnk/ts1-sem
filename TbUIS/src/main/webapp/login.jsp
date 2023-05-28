<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 13.08.2017
  Time: 18:22
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title><spring:message code="loginPage.title"/></title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="<spring:url value="css/bootstrap.min.css"/>">
    <!-- Default stylesheet -->
    <link rel="stylesheet" href="<spring:url value="css/style.css"/>">
    <link rel="shortcut icon" href="<spring:url value="images/favico-uis.ico"/>" type="image/x-icon">
</head>
<body>
<!-- Top Menu -->
<c:import url="WEB-INF/pages/modules/menus/menu.jsp"/>
<!-- Page content -->
<div id="main-content" class="container">
    <!-- ALERTS -->
    <!-- Bad login -->
    <c:if test="${not empty error}">
        <div id="loginPage.errorAlert" class="alert alert-danger" role="alert">
            <strong><spring:message code="alert.error"/></strong> <spring:message code="loginPage.badLogin"/>
        </div>
    </c:if>
    <!-- Logout successful -->
    <c:if test="${param.logout != null}">
        <div id="loginPage.successAlert" class="alert alert-success" role="alert">
            <strong><spring:message code="alert.success"/></strong> <spring:message code="loginPage.successLogout"/>
        </div>
    </c:if>

    <h1 id="loginPage.title"><spring:message code="loginPage.title"/></h1>
    <form id="loginPage.loginForm" action="<spring:url value="/login"/>" method="post">
        <div id="loginPage.userNameField" class="form-group">
            <label id="loginPage.usernameLabel" for="loginPage.userNameInput"><spring:message code="loginPage.usernameLabel"/></label>
            <input type="text" class="form-control" id="loginPage.userNameInput" name="username" placeholder="<spring:message code="loginPage.usernamePlacehoder"/>" title="<spring:message code="loginPage.usernameTitle"/>">
        </div>
        <div id="loginPage.passwordField" class="form-group">
            <label id="loginPage.passwordLabel" for="loginPage.passwordInput"><spring:message code="loginPage.passwordLabel"/></label>
            <input type="password" class="form-control" id="loginPage.passwordInput" name="password" placeholder="<spring:message code="loginPage.passwordPlaceholder"/>" title="<spring:message code="loginPage.passwordTitle"/>">
        </div>
        <button id="loginPage.loginFormSubmit" type="submit" class="btn btn-primary" title="<spring:message code="loginPage.loginButtonTitle"/>"><spring:message code="loginPage.loginButton"/></button>
    </form>
</div>
<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<!-- Bootstrap 4-->
<script src="<spring:url value="js/jquery-3.1.1.slim.min.js"/>"></script>
<script src="<spring:url value="js/popper.min.js"/>"></script>
<script src="<spring:url value="js/bootstrap.min.js"/>"></script>

</body>
</html>
