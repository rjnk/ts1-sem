<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 27.08.2017
  Time: 21:00
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3 id="stu.mySubjects.enrolledTable.Title"><spring:message code="stu.mySubjects.enrolledTable.Title"/></h3>
<table id="stu.mySubjects.enrolledSubjects.table" class="table table-hover">
    <thead class="thead-inverse">
    <tr>
        <th><spring:message code="stu.mySubjects.enrolledTable.NumberColumn"/></th>
        <th><spring:message code="stu.mySubjects.enrolledTable.NameColumn"/></th>
        <th><spring:message code="stu.mySubjects.enrolledTable.TeachersColumn"/></th>
        <th class="text-center"><spring:message code="stu.mySubjects.enrolledTable.CreditRatingColumn"/></th>
        <th class="text-center"><spring:message code="stu.mySubjects.enrolledTable.UnregisterSubjectColumn"/></th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${not empty subjectList}">
        <c:forEach var="subject" items="${subjectList}" varStatus="subjectListIndex">
            <tr id="stu.mySubjects.enrolledTable.subjectRow-${subjectListIndex.index}">
                <td>${subjectListIndex.index + 1}</td>
                <td>${subject.name}</td>
                <td>
                    <c:forEach var="teacher" items="${subject.teachers}" varStatus="loop">
                        ${teacher.firstName} ${teacher.lastName}<c:if test="${!loop.last}">,</c:if>
                    </c:forEach>
                </td>
                <td class="text-center">${subject.creditRating}</td>
                <c:if test="${indexOfHiddenSubject != subjectListIndex.index}">
                    <td class="text-center"><button id="stu.mySubjects.enrolledTable.unenrollSubjectButton-${subjectListIndex.index}" type="button" class="btn btn-danger btn-sm" data-toggle="modal" data-target="#unenrollSubjectModal" onclick="setSubjectInModal(${subject.id});" title="<spring:message code="stu.mySubjects.enrolledTable.UnregisterSubjectButtonTitle"/>">X</button></td>
                </c:if>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${empty subjectList}">
        <tr id="stu.mySubjects.enrolledTable.NoRecords" class="text-center"><td colspan="5"><spring:message code="stu.mySubjects.enrolledTable.NoRecords"/></td></tr>
    </c:if>
    </tbody>
</table>

<h3 id="stu.mySubjects.completedTable.title"><spring:message code="stu.mySubjects.completedTable.title"/><c:if test="${not empty totalCredits}"> - <small id="stu.mySubjects.completedTable.creditAmount">${totalCredits} <spring:message code="stu.mySubjects.completedTable.creditSum"/></small></c:if></h3>
<table id="stu.mySubjects.completedTable" class="table table-hover">
    <thead class="thead-inverse">
    <tr>
        <th><spring:message code="stu.mySubjects.completedTable.NumberColumn"/></th>
        <th><spring:message code="stu.mySubjects.completedTable.NameColumn"/></th>
        <th><spring:message code="stu.mySubjects.completedTable.TeacherColumn"/></th>
        <th class="text-center"><spring:message code="stu.mySubjects.completedTable.CreditRatingColumn"/></th>
        <th class="text-center"><spring:message code="stu.mySubjects.completedTable.GradeColumn"/></th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${not empty completedSubjectList}">
        <c:forEach var="subject" items="${completedSubjectList}" varStatus="completedSubjectListIndex">
            <tr id="stu.mySubjects.completedTable.completedSubjectRow-${completedSubjectListIndex.index}">
                <td>${completedSubjectListIndex.index + 1}</td>
                <td>${subject.name}</td>
                <td>
                    <c:forEach var="teacher" items="${subject.teachers}" varStatus="loop">
                        ${teacher.firstName} ${teacher.lastName}<c:if test="${!loop.last}">,</c:if>
                    </c:forEach>
                </td>
                <td class="text-center">${subject.creditRating}</td>
                <td class="text-center">
                <c:forEach var="grade" items="${listOfGrades}" varStatus="loop">
                    <c:if test="${grade.subject.id == subject.id}">
                        ${grade.typeOfGrade.name}
                    </c:if>
                </c:forEach>
                </  td>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${empty completedSubjectList}">
        <tr id="stu.mySubjects.completedTable.NoRecords" class="text-center"><td colspan="5"><spring:message code="stu.mySubjects.completedTable.NoRecords"/></td></tr>
    </c:if>
    </tbody>
</table>

<div class="modal fade" id="unenrollSubjectModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 id="stu.mySubjects.unenrollSubjectModal.ModalTitle" class="modal-title"><spring:message code="stu.mySubjects.unenrollSubjectModal.ModalTitle"/></h5>
                <button id="closeModalButtonCross" type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <form id="unenrollSubjectForm" method="POST">
                <div class="modal-body">
                    <div class="form-group">
                        <label  id="stu.mySubjects.unenrollSubjectModal.subjectSelectLabel" for="stu.mySubjects.unenrollSubjectModal.subjectSelect"><spring:message code="stu.mySubjects.unenrollSubjectModal.SubjectSelect"/></label>
                        <select id="stu.mySubjects.unenrollSubjectModal.subjectSelect" name="subjectId" title="<spring:message code="stu.mySubjects.unenrollSubjectModal.SubjectSelectTooltip"/>">
                            <c:if test="${not empty subjectList}">
                                <c:forEach var="subject" items="${subjectList}">
                                    <option value="${subject.id}">${subject.name}</option>
                                </c:forEach>
                            </c:if>
                        </select>
                    </div>
                </div>

                <div id="buttonsField" class="modal-footer">
                    <button id="stu.mySubjects.unenrollSubjectModal.unenrollSubjectButton" type="submit" class="btn btn-primary" title="<spring:message code="stu.mySubjects.unenrollSubjectModal.unenrollSubjectButtonTitle"/>"><spring:message code="stu.mySubjects.unenrollSubjectModal.unenrollSubjectButtonLabel"/></button>
                    <button id="stu.mySubjects.unenrollSubjectModal.CloseModalButton" type="button" class="btn btn-secondary" data-dismiss="modal" title="<spring:message code="stu.mySubjects.unenrollSubjectModal.CloseModalButtonTitle"/>"><spring:message code="stu.mySubjects.unenrollSubjectModal.CloseModalButtonLabel"/></button>
                </div>
            </form>
        </div>
    </div>
</div>

<script>
    function setSubjectInModal(subjectId){
        $("#stu\\.mySubjects\\.unenrollSubjectModal\\.subjectSelect").val(subjectId).change();
    }
</script>