<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 28.08.2017
  Time: 16:36
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3 id="stu.otherExamDates.table.title"><spring:message code="stu.otherExamDates.table.title"/></h3>
<table id="stu.otherExamDates.table" class="table table-hover">
    <thead class="thead-inverse">
    <tr>
        <th><spring:message code="stu.otherExamDates.table.numberColumn"/></th>
        <th><spring:message code="stu.otherExamDates.table.teachersColumn"/></th>
        <th><spring:message code="stu.otherExamDates.table.dateColumn"/></th>
        <th><spring:message code="stu.otherExamDates.table.participantsColumn"/></th>
        <th><spring:message code="stu.otherExamDates.table.registerColumn"/></th>
    </tr>
    </thead>
    <c:if test="${not empty studiedSubjects}">
        <c:set var="subjectIndex" value="0"/>

        <c:forEach var="subject" items="${studiedSubjects}">
            <c:set var="subjectHasExaminationTerm" value="false"/>
            <tbody class="table-dark">
            <tr id="stu.otherExamDates.table.subject-${subjectIndex}">
                <th id="stu.otherExamDates.table.subjectColumn-${subjectIndex}" colspan="5">${subject.name}</th>
            </tr>
            </tbody>
            <tbody>
            <c:set var="lineNumber" value="1"/>
            <c:set var="examTermIndex" value="0"/>
            <c:forEach var="examinationDate" items="${notRegisteredExaminationDatesList}">
                <c:set var="isSubjectWithRegisredExamTerms" value="false"/>
                <c:forEach var="subjectWithRegisredExamTerms" items="${subjectsWithExamDates}">
                    <c:if test="${subject.id == subjectWithRegisredExamTerms.id}">
                        <c:set var="isSubjectWithRegisredExamTerms" value="true"/>
                    </c:if>
                </c:forEach>
                <c:if test="${examinationDate.subject.id == subject.id}">
                    <c:set var="subjectHasExaminationTerm" value="true"/>
                    <tr id="stu.otherExamDates.table.subject-${subjectIndex}-${examTermIndex}" <c:if test="${isSubjectWithRegisredExamTerms eq true}">class="bg-warning"</c:if>>
                        <td>${lineNumber}</td>
                        <td>${examinationDate.teacher.firstName} ${examinationDate.teacher.lastName}</td>
                        <td><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${examinationDate.dateOfTest}"/></td>
                        <td>
                            <button id="stu.otherExamDates.table.participantsButton-${subjectIndex}-${examTermIndex}" type="button" class="btn btn-primary btn-sm"
                                    data-toggle="modal" data-target="#showListOfParticipants"
                                    onclick="showListOfParticipants(${examinationDate.id})"
                                    title="<spring:message code="stu.otherExamDates.table.participantsButtonTitle"/>"><spring:message code="stu.otherExamDates.table.participantsButtonLabel"/> (${fn:length(examinationDate.participants)}/${examinationDate.maxParticipants})
                            </button>
                        </td>
                        <td>
                            <form id="stu.otherExamDates.table.registerExamTermForm-${subjectIndex}-${examTermIndex}" method="post">
                                <input id="stu.otherExamDates.table.examTermInput-${subjectIndex}-${examTermIndex}" type="hidden" name="examDateId" value="${examinationDate.id}">
                                <button id="stu.otherExamDates.table.registerButton-${subjectIndex}-${examTermIndex}" type="submit"
                                        <c:if test="${(fn:length(examinationDate.participants) >= examinationDate.maxParticipants) || (isSubjectWithRegisredExamTerms eq true)}">disabled</c:if>
                                        class="btn btn-primary btn-sm" title="<spring:message code="stu.otherExamDates.table.registerButtonTitle"/>"><spring:message code="stu.otherExamDates.table.registerButtonLabel"/>
                                </button>
                            </form>

                        </td>
                    </tr>
                    <c:set var="examTermIndex" value="${examTermIndex + 1}"/>
                    <c:set var="lineNumber" value="${lineNumber + 1}"/>
                </c:if>
            </c:forEach>
            <c:if test="${subjectHasExaminationTerm eq false}">
                <tr id="stu.otherExamDates.table.noExamDatesForSubject-${subjectIndex}" class="text-center"><td colspan="5"><spring:message code="stu.otherExamDates.table.noTermForSubject"/></td></tr>
            </c:if>
            </tbody>
            <c:set var="subjectIndex" value="${subjectIndex + 1}"/>
        </c:forEach>
    </c:if>
    <c:if test="${empty studiedSubjects}">
        <tr class="text-center">
            <td colspan="5" id="stu.otherExamDates.table.noSubject"><spring:message code="stu.otherExamDates.table.noSubject"/></td>
        </tr>
    </c:if>
</table>

<div class="modal fade" id="showListOfParticipants" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 id="stu.otherExamDates.participantsModal.title" class="modal-title"><spring:message code="stu.otherExamDates.participantsModal.title"/></h5>
                <button id="closeModalButtonCross" type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <c:forEach var="examinationDate" items="${notRegisteredExaminationDatesList}" varStatus="examTermLoop">
                    <h6 id="stu.otherExamDates.participantsModal.table.title-${examTermLoop.index}" class="term-${examinationDate.id}">${examinationDate.subject.name} - <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${examinationDate.dateOfTest}"/></h6>
                </c:forEach>
                <table id="stu.otherExamDates.participantsModal.table" class="table table-hover">
                    <thead class="thead-inverse">
                    <tr>
                        <th><spring:message code="stu.otherExamDates.participantsModal.table.numberColumn"/></th>
                        <th><spring:message code="stu.otherExamDates.participantsModal.table.nameColumn"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="subject" items="${studiedSubjects}" varStatus="subjectLoop">
                        <c:set var="examTermNumber" value="0"/>
                        <c:forEach var="examinationDate" items="${notRegisteredExaminationDatesList}" varStatus="examTermLoop">
                            <c:if test="${examinationDate.subject.id == subject.id}">
                                <c:forEach var="participant" items="${examinationDate.participants}" varStatus="participantLoop">
                                    <tr id="stu.otherExamDates.participantsModal.table.subject-${subjectLoop.index}.examTerm-${examTermNumber}.participant-${participantLoop.index}" class="term-${examinationDate.id}">
                                        <td>${participantLoop.index + 1}</td>
                                        <td>${participant.firstName} ${participant.lastName}</td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty examinationDate.participants}">
                                    <tr id="stu.otherExamDates.participantsModal.table.noRecords-${examTermLoop.index}" class="text-center term-${examinationDate.id}">
                                        <td colspan="2"><spring:message code="stu.otherExamDates.participantsModal.table.noRecords"/></td>
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
                <button id="stu.otherExamDates.participantsModal.closeButton" title="<spring:message code="stu.otherExamDates.participantsModal.closeButton.tooltip"/>" type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="stu.otherExamDates.participantsModal.closeButton"/></button>
            </div>
        </div>
    </div>
</div>

<script>
    function showListOfParticipants(id) {
        // Hide all table titles
        $(".modal-body").find("h6").hide();
        // Hide all table items
        $("#stu\\.otherExamDates\\.participantsModal\\.table").find("tbody").find("tr").hide();
        // Show term information (title, participants)
        $(".term-" + id).show();
    }
</script>