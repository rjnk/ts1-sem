<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 17.09.2017
  Time: 19:34
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3 id="tea.setEvaluation.form.title"><spring:message code="tea.setEvaluation.form.title"/><c:if test="${ not empty selectedSubject}"> - ${selectedSubject.name}</c:if></h3>
<c:if test="${not empty examinationDate || not empty examinationDateList}">
<form id="tea.setEvaluation.form" action="<c:url value="/teacher-view/setEvaluation"/>" method="post">

    <c:if test="${ empty examinationDate && not empty examinationDateSelected}">
        <div class="form-group row">
            <label id="tea.setEvaluation.form.dateOfExaminationLabel" for="tea.setEvaluation.form.examTermDateSelect" class="col-sm-5 col-form-label"><spring:message code="tea.setEvaluation.form.dateOfExaminationLabel"/></label>
            <div class="col-sm-7">
                <select class="custom-select mb-4 mr-sm-4 mb-sm-0" name="examTermId" id="tea.setEvaluation.form.examTermDateSelect" onchange="showListOfParticipants(this);" title="<spring:message code="tea.setEvaluation.form.examTermDateSelect.title"/>">
                    <c:forEach var="examinationDate" items="${examinationDateList}">
                        <option value="${examinationDate.id}" ${((examinationDate.dateOfTest == examinationDateSelected.dateOfTest) && (examinationDate.subject.id) == (examinationDateSelected.subject.id)) ? 'selected="selected"' : ''}>${examinationDate.subject.name}
                            - <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${examinationDate.dateOfTest}"/></option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </c:if>

    <c:if test="${ not empty examinationDate}">
        <div class="form-group row">
            <label id="tea.setEvaluation.form.dateOfExaminationLabel" for="tea.setEvaluation.form.examTermDateInput" class="col-sm-5 col-form-label"><spring:message code="tea.setEvaluation.form.dateOfExaminationLabel"/></label>
            <div class="col-sm-7">
                <input class="form-control" id="tea.setEvaluation.form.examTermDateInput" type="text" value="${examinationDate.subject.name} - <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${examinationDate.dateOfTest}"/>" disabled title="<spring:message code="tea.setEvaluation.form.examTermDateSelect.title"/>">
                <input class="form-control" id="tea.setEvaluation.form.examTermIdInputHidden" name="examTermId" type="hidden" value="${examinationDate.id}">
            </div>
        </div>
    </c:if>

    <c:if test="${ empty examinationDate && empty examinationDateSelected && empty studentSelected}">
        <div class="form-group row">
            <label id="tea.setEvaluation.form.dateOfExaminationLabel" for="tea.setEvaluation.form.examTermDateSelect" class="col-sm-5 col-form-label"><spring:message code="tea.setEvaluation.form.dateOfExaminationLabel"/></label>
            <div class="col-sm-7">
                    <select class="custom-select mb-4 mr-sm-4 mb-sm-0" name="examTermId" id="tea.setEvaluation.form.examTermDateSelect" onchange="showListOfParticipants(this);" title="<spring:message code="tea.setEvaluation.form.examTermDateSelect.title"/>">
                        <c:forEach var="examinationDate" items="${examinationDateList}">
                            <option value="${examinationDate.id}">${examinationDate.subject.name} - <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${examinationDate.dateOfTest}"/></option>
                        </c:forEach>
                    </select>
            </div>
        </div>
    </c:if>

    <c:if test="${ not empty owner}">
        <div class="form-group row">
            <label id="tea.setEvaluation.form.studentLabel" for="tea.setEvaluation.form.studentNameInput" class="col-sm-5 col-form-label"><spring:message code="tea.setEvaluation.form.studentLabel"/></label>
            <div class="col-sm-7">
                <input class="form-control" id="tea.setEvaluation.form.studentNameInput" type="text" value="${owner.firstName} ${owner.lastName}"
                       disabled title="<spring:message code="tea.setEvaluation.form.studentNameInput.title"/>">
                <input class="form-control" id="tea.setEvaluation.form.studentIdInputHidden" name="ownerId" type="hidden" value="${owner.id}">
            </div>
        </div>
    </c:if>

    <c:if test="${ empty owner}">
        <div class="form-group row">
            <label id="tea.setEvaluation.form.studentLabel" for="tea.setEvaluation.form.studentSelect" class="col-sm-5 col-form-label"><spring:message code="tea.setEvaluation.form.studentLabel"/></label>
            <div class="col-sm-7">
                <select id="tea.setEvaluation.form.studentSelect" class="custom-select mb-4 mr-sm-4 mb-sm-0" name="ownerId" title="<spring:message code="tea.setEvaluation.form.studentNameInput.title"/>">
                    <c:forEach var="examinationDate" items="${examinationDateList}">
                        <c:forEach var="participant" items="${examinationDate.participants}">
                            <option class="examTerm-${examinationDate.id}" value="${participant.id}">${participant.firstName} ${participant.lastName}</option>
                        </c:forEach>
                    </c:forEach>
                </select>
                <p id="tea.setEvaluation.form.noOneStudent" class="form-control-static"><spring:message code="tea.setEvaluation.form.noOneStudent"/></p>
            </div>
        </div>
    </c:if>

    <c:if test="${ not empty gradeTypes}">
        <div class="form-group row">
            <label id="tea.setEvaluation.form.gradeLabel" for="tea.setEvaluation.form.gradeSelect" class="col-sm-5 col-form-label"><spring:message code="tea.setEvaluation.form.gradeLabel"/></label>
            <div class="col-sm-7">
                <select id="tea.setEvaluation.form.gradeSelect" class="custom-select mb-4 mr-sm-4 mb-sm-0" name="grade" title="<spring:message code="tea.setEvaluation.form.gradeSelect.title"/>">
                    <c:forEach var="gradeType" items="${gradeTypes}">
                        <option value="${gradeType.id}">${gradeType.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
    </c:if>
    <button id="tea.setEvaluation.form.saveButton" type="submit" class="btn btn-primary" title="<spring:message code="tea.setEvaluation.form.saveButtonTitle"/>"><spring:message code="tea.setEvaluation.form.saveButtonLabel"/></button>
</form>
</c:if>
<c:if test="${empty examinationDate && empty examinationDateList}">
    <div id="tea.setEvaluation.form.noRecords" class="alert alert-warning" role="alert">
        <strong><spring:message code="alert.warning"/></strong> <spring:message code="tea.setEvaluation.form.noRecords"/>
    </div>
</c:if>

<c:if test="${ empty examinationDate}">
<script>
    function showListOfParticipants(selectObject) {
        var id = selectObject.value;

        $("#tea\\.setEvaluation\\.form\\.studentSelect").show();
        $("#tea\\.setEvaluation\\.form\\.studentSelect").find("option").hide();
        if($(".examTerm-" + id).length){
            // Hide no students text
            $("#tea\\.setEvaluation\\.form\\.noOneStudent").hide();
            // Enable submit form button
            $("#tea\\.setEvaluation\\.form\\.saveButton").attr("disabled", false);
            // Show and set student select
            $(".examTerm-" + id).show();
            $("#tea\\.setEvaluation\\.form\\.studentSelect").val($(".examTerm-" + id)[0].value).change();
        }else{
            // No one student is registered on this term
            // Hide student select
            $("#tea\\.setEvaluation\\.form\\.studentSelect").hide();
            // Show no students text
            $("#tea\\.setEvaluation\\.form\\.noOneStudent").show();
            // Disable submit form button
            $("#tea\\.setEvaluation\\.form\\.saveButton").attr("disabled", true);
        }
    }
    window.addEventListener('load',
        function() {
            showListOfParticipants(document.getElementById("tea.setEvaluation.form.examTermDateSelect"));
        }, false);


</script>
</c:if>
