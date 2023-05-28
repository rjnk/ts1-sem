<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 16.09.2017
  Time: 10:57
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3 id="tea.newExamDates.form.title"><spring:message code="tea.newExamDates.form.title"/><c:if test="${ not empty selectedSubject}"> - ${selectedSubject.name}</c:if></h3>
<c:if test="${ not empty taughtSubjectList}">
<form id="tea.newExamDates.form" action="<c:url value="/teacher-view/newExamDates"/>" method="post">
    <div class="form-group row">
        <label id="tea.newExamDates.form.subjectLabel" for="tea.newExamDates.form.subjectSelect" class="col-sm-4"><spring:message code="tea.newExamDates.form.subjectLabel"/></label>
        <div class="col-sm-8">
        <c:if test="${empty selectedSubject}">
            <select class="form-control" name="subject" id="tea.newExamDates.form.subjectSelect" title="<spring:message code="tea.newExamDates.form.subjectSelect.title"/>">
                <c:forEach var="subject" items="${taughtSubjectList}">
                    <option value="${subject.id}">${subject.name}</option>
                </c:forEach>
            </select>
        </c:if>
        <c:if test="${not empty selectedSubject}">
            <input class="form-control" id="tea.newExamDates.form.subjectNameInput" type="text" value="${selectedSubject.name}" disabled title="<spring:message code="tea.newExamDates.form.subjectInput.title"/>">
            <input class="form-control" id="tea.newExamDates.form.subjectIdInputHidden" name="subject" type="hidden" value="${selectedSubject.id}">
        </c:if>
        </div>
    </div>

    <div class="form-group row">
        <label id="tea.newExamDates.form.maxParticipantsLabel" for="tea.newExamDates.form.maxParticipantsInput" class="col-sm-4 col-form-label"><spring:message code="tea.newExamDates.form.maxParticipantsLabel"/></label>
        <div class="col-sm-8">
            <input type="number" class="form-control form-control-sm" id="tea.newExamDates.form.maxParticipantsInput" name="maxParticipants" value="1" title="<spring:message code="tea.newExamDates.form.maxParticipantsInput.title"/>">
        </div>
    </div>
    <div class="form-group row">
        <label id="tea.newExamDates.form.dateOfExamLabel" for="tea.newExamDates.form.dateOfExamInput" class="col-sm-4 col-form-label"><spring:message code="tea.newExamDates.form.dateOfExaminationLabel"/></label>
        <div class="col-8">
            <input class="form-control" type="text" id="tea.newExamDates.form.dateOfExamInput" name="date-of-test" placeholder="yyyy-MM-dd HH:mm" title="<spring:message code="tea.newExamDates.form.dateOfExamInput.title"/>">
        </div>
    </div>
    <button id="tea.newExamDates.form.saveButton" type="submit" class="btn btn-primary" title="<spring:message code="tea.newExamDates.form.saveButtonTitle"/>"><spring:message code="tea.newExamDates.form.saveButtonLabel"/></button>
</form>
</c:if>
<c:if test="${ empty taughtSubjectList}">
    <div id="tea.newExamDates.form.noRecords" class="alert alert-warning" role="alert">
        <strong><spring:message code="alert.warning"/></strong> <spring:message code="tea.newExamDates.form.noRecords"/>
    </div>
</c:if>