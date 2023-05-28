<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 14.09.2017
  Time: 19:58
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3 id="tea.myExamDates.table.title"><spring:message code="tea.myExamDates.table.title"/></h3>
<table id="tea.myExamDates.table" class="table table-hover">
    <thead class="thead-inverse">
    <tr>
        <th><spring:message code="tea.myExamDates.table.numberColumn"/></th>
        <th><spring:message code="tea.myExamDates.table.examinerColumn"/></th>
        <th><spring:message code="tea.myExamDates.table.dateColumn"/></th>
        <th><spring:message code="tea.myExamDates.table.participantsColumn"/></th>
        <th class="text-center"><spring:message code="tea.myExamDates.table.unregisterTermColumn"/></th>
    </tr>
    </thead>
    <c:if test="${not empty taughtSubjectList}">
    <c:forEach var="subject" items="${taughtSubjectList}" varStatus="taughtSubjectListLoop">
        <c:set var="subjectHasExaminationTerm" value="false"/>
        <tbody class="table-dark">
        <tr id="tea.myExamDates.table.subjectRow-${taughtSubjectListLoop.index}">
            <th id="tea.myExamDates.table.subjectColumn-${taughtSubjectListLoop.index}" colspan="5">${subject.name}</th>
        </tr>
        </tbody>
        <tbody>
        <c:set var="lineNumber" value="1"/>
        <c:if test="${not empty listOfExamDates}">
            <c:set var="examTermIndex" value="0"/>
            <c:forEach var="examinationDate" items="${listOfExamDates}" varStatus="loop">
                <c:if test = "${examinationDate.subject.id == subject.id}">
                    <c:set var="subjectHasExaminationTerm" value="true"/>
                <tr id="tea.myExamDates.table.examDateRow-${taughtSubjectListLoop.index}-${examTermIndex}">
                    <td>${lineNumber}</td>
                    <td>${examinationDate.teacher.firstName} ${examinationDate.teacher.lastName}</td>
                    <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${examinationDate.dateOfTest}"/></td>
                    <td><button id="tea.myExamDates.table.participantsButton-${taughtSubjectListLoop.index}-${examTermIndex}" type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#showListOfParticipants"
                                onclick="showListOfParticipants(${examinationDate.id})" title="<spring:message code="tea.myExamDates.table.participantsButtonTitle"/>"><spring:message code="tea.myExamDates.table.participantsButtonLabel"/> (${fn:length(examinationDate.participants)}/${examinationDate.maxParticipants})
                        </button>
                    </td>
                    <td class="text-center">
                        <form onsubmit="participantsExistsBeforeExamTermDeletion(event, ${fn:length(examinationDate.participants)})" id="tea.myExamDates.table.unregisterTermForm-${taughtSubjectListLoop.index}-${examTermIndex}" method="post">
                            <input id="tea.myExamDates.table.examinationTermInput-${taughtSubjectListLoop.index}-${examTermIndex}" type="hidden" name="examDateId" value="${examinationDate.id}">
                            <button id="tea.myExamDates.table.cancelButton-${taughtSubjectListLoop.index}-${examTermIndex}"
                                    type="submit" class="btn btn-danger btn-sm"
                                    title="<spring:message code="tea.myExamDates.table.cancelButtonTitle"/>"
                                    <c:if test = "${fn:length(examinationDate.grades) > 0}">disabled</c:if>
                            ><spring:message code="tea.myExamDates.table.cancelButtonLabel"/></button>
                        </form>
                    </td>
                </tr>
                <c:set var="examTermIndex" value="${examTermIndex + 1}"/>
                <c:set var="lineNumber" value="${lineNumber + 1}"/>
                </c:if>
            </c:forEach>
        </c:if>
        <c:if test="${subjectHasExaminationTerm eq false}">
            <tr id="tea.myExamDates.table.noRecords-${taughtSubjectListLoop.index}"><td class="text-center" colspan="5"><spring:message code="tea.myExamDates.table.noRecords"/></td></tr>
        </c:if>
        </tbody>
    </c:forEach>
    </c:if>
    <c:if test="${empty taughtSubjectList}">
        <tr id="tea.myExamDates.table.noRecords"><td class="text-center" colspan="5"><spring:message code="tea.myExamDates.table.noRecords"/></td></tr>
    </c:if>
</table>

<div class="modal fade" id="showListOfParticipants" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 id="tea.myExamDates.participantsModal.title" class="modal-title"><spring:message code="tea.myExamDates.participantsModal.title"/></h5>
                <button id="closeModalButtonCross" type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <c:forEach var="examinationDate" items="${listOfExamDates}">
                    <h6 id="tea.myExamDates.participantsModal.table.title-${examinationDate.id}" class="term-${examinationDate.id}">${examinationDate.subject.name} - <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${examinationDate.dateOfTest}"/></h6>
                </c:forEach>
                <table id="tea.myExamDates.participantsModal.table" class="table table-hover">
                    <thead class="thead-inverse">
                    <tr>
                        <th><spring:message code="tea.myExamDates.participantsModal.table.numberColumn"/></th>
                        <th><spring:message code="tea.myExamDates.participantsModal.table.nameColumn"/></th>
                        <th><spring:message code="tea.myExamDates.participantsModal.table.gradeColumn"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="subject" items="${taughtSubjectList}" varStatus="subjectLoop">

                        <c:set var="examTermNumber" value="0"/>
                        <c:forEach var="examinationDate" items="${listOfExamDates}" varStatus="examTermLoop">

                            <c:if test="${examinationDate.subject.id == subject.id}">
                                <c:forEach var="participant" items="${examinationDate.participants}" varStatus="participantLoop">
                                    <tr id="tea.myExamDates.participantsModal.table.subject-${subjectLoop.index}.examTerm-${examTermNumber}.participant-${participantLoop.index}" class="term-${examinationDate.id}">
                                        <td>${participantLoop.index + 1}</td>
                                        <td>${participant.firstName} ${participant.lastName}</td>
                                        <td>
                                            <form method="get" action="<c:url value="/teacher-view/setEvaluation"/>/${examinationDate.id}/${participant.id}">
                                                <button id="tea.myExamDates.participantsModal.table.subject-${subjectLoop.index}.examTerm-${examTermNumber}.participant-${participantLoop.index}.gradeButton" type="submit" class="btn btn-primary btn-sm" title="<spring:message code="tea.myExamDates.participantsModal.table.gradeButtonTitle"/>"><spring:message code="tea.myExamDates.participantsModal.table.gradeButtonLabel"/></button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>

                                <c:if test="${empty examinationDate.participants}">
                                    <tr id="tea.myExamDates.participantsModal.table.noRecords-${examTermLoop.index}" class="term-${examinationDate.id} text-center">
                                        <td colspan="3"><spring:message code="tea.myExamDates.participantsModal.table.noRecords"/></td>
                                    </tr>
                                </c:if>
                                <c:set var="examTermNumber" value="${examTermNumber + 1}"/>

                            </c:if>
                        </c:forEach>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button id="tea.myExamDates.participantsModal.closeButtonTitle" type="button" class="btn btn-secondary" data-dismiss="modal" title="<spring:message code="tea.myExamDates.participantsModal.closeButtonTitle"/>"><spring:message code="tea.myExamDates.participantsModal.closeButtonLabel"/></button>
            </div>
        </div>
    </div>
</div>
<script>
    function showListOfParticipants(id) {
        // Hide all table titles
        $(".modal-body").find("h6").hide();
        // Hide all table items
        $("#tea\\.myExamDates\\.participantsModal\\.table").find("tbody").find("tr").hide();
        // Show term informations (title, participants)
        $(".term-" + id).show();
    }

    function participantsExistsBeforeExamTermDeletion(event, participantsCount) {
        if(participantsCount !== 0){
            var confirmation = confirm(participantsCount + " <spring:message code="tea.myExamDates.participantsModal.table.removeTermWithStudentsConfirm"/>");
            if(confirmation === true){
                return true;
            }else {
                event.preventDefault();
                return false;
            }
        }
        return true;
    }
</script>
