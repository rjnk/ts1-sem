<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 28.08.2017
  Time: 17:08
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:if test="${not empty currentUser}">
<form id="overview.personalInfoForm" method="POST">
    <div class="form-group row">
        <label id="overview.firstNameLabel" for="overview.firstName" class="col-sm-2 control-label"><spring:message code="overview.firstNameField"/></label>
        <div class="col-sm-10">
            <input id="overview.firstName" title="<spring:message code="overview.firstNameTooltip"/>" type="text" name="firstName" class="form-control"placeholder="<spring:message code="overview.firstNamePlaceholder"/>" value="${currentUser.firstName}" >
        </div>
    </div>
    <div class="form-group row">
        <label id="overview.lastNameLabel" for="overview.lastName" class="col-sm-2 control-label"><spring:message code="overview.lastNameField"/></label>
        <div class="col-sm-10">
            <input id="overview.lastName" title="<spring:message code="overview.lastNameTooltip"/>" type="text" name="lastName" class="form-control" placeholder="<spring:message code="overview.lastNamePlaceholder"/>" value="${currentUser.lastName}" >
        </div>
    </div>
    <div class="form-group row">
        <label id="overview.emailLabel" for="overview.email" class="col-sm-2 control-label"><spring:message code="overview.emailField"/></label>
        <div class="col-sm-10">
            <input id="overview.email" title="<spring:message code="overview.emailTooltip"/>" type="email" name="email" class="form-control" placeholder="<spring:message code="overview.emailPlaceholder"/>" value="${currentUser.email}" >
        </div>
    </div>
    <div id="personalInfoFormSubmitField" class="form-group row">
        <div class="col-sm-offset-2 col-sm-10">
            <button id="overview.saveButton" type="submit" class="btn btn-success" title="<spring:message code="overview.saveButtonTitle"/>"><spring:message code="overview.saveButton"/></button>
        </div>
    </div>
</form>
</c:if>