<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="srping" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%--
  Author: Jiri Matyas
  Date: 28.08.2017
  Time: 15:49
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3 id="stu.myExamDates.table.title"><spring:message code="stu.myExamDates.table.title"/></h3>
<table id="stu.myExamDates.table" class="table table-hover">
    <thead class="thead-inverse">
        <tr>
            <th><spring:message code="stu.myExamDates.table.numberColumn"/></th>
            <th><spring:message code="stu.myExamDates.table.subjectColumn"/></th>
            <th><spring:message code="stu.myExamDates.table.teachersColumn"/></th>
            <th><spring:message code="stu.myExamDates.table.dateColumn"/></th>
            <th><spring:message code="stu.myExamDates.table.participantsColumn"/></th>
            <th><spring:message code="stu.myExamDates.table.unregisterColumn"/></th>
        </tr>
    </thead>
    <tbody>
    <c:if test="${not empty examinationDateList}">
    <c:forEach var="examinationDate" items="${examinationDateList}" varStatus="examinationTermListLoop">
        <tr id="stu.myExamDates.table.examRow-${examinationTermListLoop.index}">
            <td>${examinationTermListLoop.index + 1}</td>
            <td>${examinationDate.subject.name}</td>
            <td>${examinationDate.teacher.firstName} ${examinationDate.teacher.lastName}</td>
            <td><fmt:formatDate pattern = "yyyy-MM-dd HH:mm" value ="${examinationDate.dateOfTest}"/></td>
            <c:if test="${changeParticipantsNumber == true}">
                <td>
                    <button id="stu.myExamDates.table.ParticipantsButton-${examinationTermListLoop.index}" type="button"
                            <c:if test="${changeParticipantsButtonColor == true}">class="btn btn-danger btn-sm"</c:if>
                            <c:if test="${changeParticipantsButtonColor == false}">class="btn btn-primary btn-sm"</c:if>
                            data-toggle="modal" data-target="#showListOfParticipants" onclick="showListOfParticipants(${examinationDate.id})" title="<spring:message code="stu.myExamDates.table.ParticipantsButtonTitle"/>"><spring:message code="stu.myExamDates.table.ParticipantsButtonLabel"/> (${fn:length(examinationDate.participants) + 1}/${examinationDate.maxParticipants})</button>
                </td></c:if>
            <c:if test="${changeParticipantsNumber == false}">
                <td>
                    <button id="stu.myExamDates.table.ParticipantsButton-${examinationTermListLoop.index}" type="button"
                            <c:if test="${changeParticipantsButtonColor == true}">class="btn btn-danger btn-sm"</c:if>
                            <c:if test="${changeParticipantsButtonColor == false}">class="btn btn-primary btn-sm"</c:if>
                            data-toggle="modal" data-target="#showListOfParticipants" onclick="showListOfParticipants(${examinationDate.id})" title="<spring:message code="stu.myExamDates.table.ParticipantsButtonTitle"/>"><spring:message code="stu.myExamDates.table.ParticipantsButtonLabel"/> (${fn:length(examinationDate.participants)}/${examinationDate.maxParticipants})</button>
                </td></c:if>
            <td class="text-center"><form method="post"><input type="hidden" name="examDateId" value="${examinationDate.id}"><button id="stu.myExamDates.table.unregisterButton-${examinationTermListLoop.index}" type="submit" class="btn btn-danger btn-sm" title="<spring:message code="stu.myExamDates.table.unregisterButtonTitle"/>"><spring:message code="stu.myExamDates.table.unregisterButtonLabel"/></button></form></td>
        </tr>
    </c:forEach>
    </c:if>
    <c:if test="${empty examinationDateList}">
        <tr id="stu.myExamDates.table.NoRecords" class="text-center"><td colspan="6"><spring:message code="stu.myExamDates.table.NoRecords"/></td></tr>
    </c:if>
    </tbody>
</table>

<div class="modal fade" id="showListOfParticipants" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 id="stu.myExamDates.participantsModal.title" class="modal-title"><spring:message code="stu.myExamDates.participantsModal.title"/></h5>
                <button id="closeModalButtonCross" type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <c:forEach var="examinationDate" items="${examinationDateList}" varStatus="examTermLoop">
                    <h6 id="stu.myExamDates.participantsModal.table.title-${examTermLoop.index}" class="term-${examinationDate.id}">${examinationDate.subject.name} - <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${examinationDate.dateOfTest}"/></h6>
                </c:forEach>
                <table id="stu.myExamDates.participantsModal.table" class="table table-hover">
                    <thead class="thead-inverse">
                        <tr>
                            <th><spring:message code="stu.myExamDates.participantsModal.table.columnNumber"/></th>
                            <th><spring:message code="stu.myExamDates.participantsModal.table.nameColumn"/></th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:if test="${not empty examinationDateList}">
                    <c:forEach var="examinationDate" items="${examinationDateList}" varStatus="examinationTermLoop">
                        <c:forEach var="participant" items="${examinationDate.participants}" varStatus="participantsLoop">
                            <tr id="stu.myExamDates.participantsModal.table.examTerm-${examinationTermLoop.index}.participantRow-${participantsLoop.index}" class="term-${examinationDate.id}">
                                <td>${participantsLoop.index + 1}</td>
                                <td>${participant.firstName} ${participant.lastName}</td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                    </c:if>
                    <c:if test="${empty examinationDateList}">
                        <tr id="stu.myExamDates.participantsModal.table.noRecords" class="text-center"><td><spring:message code="stu.myExamDates.participantsModal.table.noRecords"/></td></tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button id="stu.myExamDates.participantsModal.CloseModalButton" title="<spring:message code="stu.myExamDates.participantsModal.CloseModalButtonLTitle"/>" type="button" class="btn btn-secondary" data-dismiss="modal"><spring:message code="stu.myExamDates.participantsModal.CloseModalButtonLabel"/></button>
            </div>

        </div>
    </div>
</div>
<script>
    function showListOfParticipants(id) {
        // Hide all table titles
        $(".modal-body").find("h6").hide();
        // Hide all table items
        $("#stu\\.myExamDates\\.participantsModal\\.table").find("tbody").find("tr").hide();
        // Show term informations (title, participants)
        $(".term-" + id).show();
    }
</script>