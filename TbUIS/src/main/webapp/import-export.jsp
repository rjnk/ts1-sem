<%--
  Author: Jiri Matyas
  Date: 19.01.2018
  Time: 17:06
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

    <title><spring:message code="importExportPage.title"/></title>

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
    <!-- Success alerts -->
    <c:if test="${not empty successMessage}">
        <div id="importExportPage.successAlert" class="alert alert-success" role="alert">
            <strong><spring:message code="alert.success"/></strong> ${successMessage}
        </div>
    </c:if>
    <!-- Error alerts -->
    <c:if test="${not empty errorMessage}">
        <div id="importExportPage.errorAlert" class="alert alert-danger" role="alert">
            <strong><spring:message code="alert.error"/></strong> ${errorMessage}
        </div>
    </c:if>
    <!-- Warning alerts -->
    <c:if test="${not empty warningMessage}">
        <div id="importExportPage.warningAlert" class="alert alert-warning" role="alert">
            <strong><spring:message code="alert.warning"/></strong> ${warningMessage}
        </div>
    </c:if>
    <div class="row">
        <!-- Export Form -->
        <div class="col">
            <h3 id="importExportPage.export.title"><spring:message code="importExportPage.export.title"/></h3>
            <form id="importExportPage.export.form" action="<c:url value="/exportdata"/>" method="get" enctype="multipart/form-data">
                <fieldset class="form-group">
                    <div class="form-check">
                        <label class="form-check-label">
                            <input id="importExportPage.export.xmlOption" type="radio" class="form-check-input" name="exportFormat" id="optionsRadios1" value="xml" checked title="<spring:message code="importExportPage.export.xmlOption.title"/>">
                            <spring:message code="importExportPage.export.xmlOption.label"/>
                        </label>
                    </div>
                    <div class="form-check">
                        <label class="form-check-label">
                            <input id="importExportPage.export.jsonOption" type="radio" class="form-check-input" name="exportFormat" id="optionsRadios3" value="json" title="<spring:message code="importExportPage.export.jsonOption.title"/>">
                            <spring:message code="importExportPage.export.jsonOption.label"/>
                        </label>
                    </div>
                </fieldset>
                <button id="importExportPage.export.submitButton" type="submit" class="btn btn-primary" title="<spring:message code="importExportPage.export.submitButton.title"/>"><spring:message code="importExportPage.export.submitButton.label"/></button>
            </form>
        </div>

        <!-- Import Form -->
        <div class="col">
            <h3 id="importExportPage.import.title"><spring:message code="importExportPage.import.title"/></h3>
            <form id="importExportPage.import.form" action="<c:url value="/importdata"/>" method="post" enctype="multipart/form-data">
                <div id="importExportPage.import.field" class="form-group">
                    <label id="importExportPage.import.input.label" for="importExportPage.import.fileInput"><spring:message code="importExportPage.import.input.label"/></label>
                    <input type="file" class="form-control-file" id="importExportPage.import.fileInput" name="importFile" title="<spring:message code="importExportPage.import.input.title"/>">
                </div>
                <button id="importExportPage.import.submitButton" type="submit" class="btn btn-primary" title="<spring:message code="importExportPage.import.submitButton.title"/>"><spring:message code="importExportPage.import.submitButton.label"/></button>

            </form>
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
