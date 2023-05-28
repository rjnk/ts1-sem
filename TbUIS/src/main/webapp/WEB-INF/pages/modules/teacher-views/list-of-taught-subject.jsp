<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Author: Jiri Matyas
  Date: 14.09.2017
  Time: 14:41
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3 id="tea.mySubjects.table.title"><spring:message code="tea.mySubjects.table.title"/></h3>
<table id="tea.mySubjects.table" class="table table-hover">
    <thead class="thead-inverse">
    <tr>
        <th><spring:message code="tea.mySubjects.table.numberColumn"/></th>
        <c:if test="${swapNameAndTeacher == true}">
            <th><spring:message code="tea.mySubjects.table.teachersColumn"/></th>
            <th><spring:message code="tea.mySubjects.table.nameColumn"/></th>
        </c:if>
        <c:if test="${swapNameAndTeacher == false}">
            <th><spring:message code="tea.mySubjects.table.nameColumn"/></th>
            <th><spring:message code="tea.mySubjects.table.teachersColumn"/></th>
        </c:if>
        <th><spring:message code="tea.mySubjects.table.newTermColumn"/></th>
        <th><spring:message code="tea.mySubjects.table.listOfStudentsColumn"/></th>
        <th><spring:message code="tea.mySubjects.table.unregisterSubjectsColumn"/></th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${not empty subjectList}">
        <c:forEach var="subject" items="${subjectList}" varStatus="subjectLoop">
            <tr id="tea.mySubjects.table.subjectRow-${subjectLoop.index}">
                <td>${subjectLoop.index + 1}</td>
                <c:if test="${swapNameAndTeacher == true}">
                    <td>
                        <c:forEach var="teacher" items="${subject.teachers}" varStatus="loop">
                            ${teacher.firstName} ${teacher.lastName}<c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    </td>
                    <td>${subject.name}</td>
                </c:if>
                <c:if test="${swapNameAndTeacher == false}">
                    <td>${subject.name}</td>
                    <td>
                        <c:forEach var="teacher" items="${subject.teachers}" varStatus="loop">
                            ${teacher.firstName} ${teacher.lastName}<c:if test="${!loop.last}">,</c:if>
                        </c:forEach>
                    </td>
                </c:if>
                <td class="text-center">
                    <form id="tea.mySubjects.table.newTermForm-${subjectLoop.index}" action="<c:url value="/teacher-view/newExamDates"/>/<c:out value="${subject.id}"/>" method="get">
                        <button id="tea.mySubjects.table.newTermButton-${subjectLoop.index}" type="submit" class="btn btn-primary btn-sm" title="<spring:message code="tea.mySubjects.table.newTermButtonTitle"/>"><spring:message code="tea.mySubjects.table.newTermButtonLabel"/></button>
                    </form>
                </td>
                <td class="text-center">
                    <button id="tea.mySubjects.table.listOfStudentsButton-${subjectLoop.index}" type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#showListOfStudents"
                            onclick="showListOfStudents(${subject.id})" title="<spring:message code="tea.mySubjects.table.listOfStudentsButtonTitle"/>"><spring:message
                            code="tea.mySubjects.table.listOfStudentsButtonLabel"/> (${fn:length(subject.listOfStudents)})
                    </button>
                </td>
                <td class="text-center">
                    <form id="tea.mySubjects.table.unregisterSubjectForm-${subjectLoop.index}" method="post"><input id="tea.mySubjects.table.subjectInput-${subjectLoop.index}" type="hidden" name="subjectId" value="${subject.id}">
                        <button id="tea.mySubjects.table.unregisterSubjectButton-${subjectLoop.index}"
                                type="submit"
                                class="btn btn-danger btn-sm"
                                title="<spring:message code="tea.mySubjects.table.unregisterSubjectButtonTitle"/>"
                                <c:if test="${not empty subject.listOfStudents}">disabled</c:if>
                        ><spring:message code="tea.mySubjects.table.unregisterSubjectButtonLabel"/></button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${empty subjectList}">
        <tr id="tea.mySubjects.table.noRecords"><td class="text-center" colspan="6"><spring:message code="tea.mySubjects.table.noRecords"/></td></tr>
    </c:if>
    </tbody>
</table>

<div class="modal fade" id="showListOfStudents" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 id="tea.mySubjects.studentsModal.title" class="modal-title"><spring:message code="tea.mySubjects.studentsModal.title"/></h5>
                <button id="closeModalButtonCross" type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <c:forEach var="subject" items="${subjectList}">
                    <h6 id="tea.mySubjects.studentsModal.table.title-${subject.id}" class="subject-${subject.id}">${subject.name}</h6>
                </c:forEach>
                <table id="tea.mySubjects.studentsModal.table" class="table table-hover">
                    <thead class="thead-inverse">
                    <tr>
                        <th><spring:message code="tea.mySubjects.studentsModal.table.numberColumn"/></th>
                        <th><spring:message code="tea.mySubjects.studentsModal.table.nameColumn"/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="subjectNumber" value="0"/>
                    <c:forEach var="subject" items="${subjectList}" varStatus="subjectLoop">

                        <c:forEach var="student" items="${subject.listOfStudents}" varStatus="studentLoop">
                            <tr id="tea.mySubjects.studentsModal.table.subject-${subjectLoop.index}.student-${studentLoop.index}" class="subject-${subject.id}">
                                <td>${studentLoop.index + 1}</td>
                                <td>${student.firstName} ${student.lastName}</td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty subject.listOfStudents}">
                            <tr id="tea.mySubjects.studentsModal.table.noRecords-${subject.id}" class="subject-${subject.id} text-center">
                                <td colspan="3"><spring:message code="tea.mySubjects.studentsModal.table.noRecords"/></td>
                            </tr>
                        </c:if>

                    </c:forEach>
                    <c:set var="subjectNumber" value="${subjectNumber + 1}"/>
                    </tbody>
                </table>
            </div>
            <div class="modal-footer">
                <button id="tea.mySubjects.studentsModal.closeButtonTitle" type="button" class="btn btn-secondary" data-dismiss="modal"
                        title="<spring:message code="tea.mySubjects.studentsModal.closeButtonTitle"/>">
                    <spring:message code="tea.mySubjects.studentsModal.closeButtonLabel"/>
                </button>
            </div>
        </div>
    </div>
</div>

<script>
  function showListOfStudents(id) {
    // Hide all table titles
    $(".modal-body").find("h6").hide();
    // Hide all table items
    $("#tea\\.mySubjects\\.studentsModal\\.table").find("tbody").find("tr").hide();
    // Show subject informations (title, students)
    $(".subject-" + id).show();
  }
</script>
