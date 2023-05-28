<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 23.01.2018
  Time: 10:52
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!-- Filter -->
<div class="row">
    <div>
        <h3 id="tea.evalTable.filter.title" class="card-title"><spring:message code="tea.evalTable.filter.title"/></h3>
        <form id="tea.evalTable.filter" action="<c:url value="/teacher-view/evaluationTable"/>" method="get">
            <div class="form-group row">
                <label id="tea.evalTable.filter.subjectSelect.label" for="tea.evalTable.filter.subjectSelect"
                       class="col-sm-2 col-form-label"><spring:message
                        code="tea.evalTable.filter.subjectSelect.label"/></label>
                <div class="col-sm-10">
                    <select class="form-control" id="tea.evalTable.filter.subjectSelect"
                            title="<spring:message code="tea.evalTable.filter.subjectSelect.tooltip"/>"
                            name="filterSubjectId">
                        <option value="-1"><spring:message
                                code="tea.evalTable.filter.subjectSelect.initOption"/></option>
                        <c:forEach var="subject" items="${subjectList}">
                            <option
                                    <c:if test="${subject.id == selectedSubjectId}">selected</c:if>
                                    value="${subject.id}">${subject.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="form-check">
                <label id="tea.evalTable.filter.includeGradedSubjects.label" class="form-check-label">
                    <input id="tea.evalTable.filter.includeGradedSubjects"
                           title="<spring:message code="tea.evalTable.filter.includeGradedSubjects.tooltip"/>"
                           <c:if test="${isIncludingGradedStudentsChecked}">checked </c:if>type="checkbox"
                           class="form-check-input" name="filterIncludeGradedSubjects">
                    <spring:message code="tea.evalTable.filter.includeGradedSubjects.label"/>
                </label>
            </div>
            <button id="tea.evalTable.filter.submitButton" type="submit" class="btn btn-primary"
                    title="<spring:message code="tea.evalTable.filter.submitButton.title"/>"><spring:message
                    code="tea.evalTable.filter.submitButton.label"/></button>
        </form>
    </div>
</div>
<hr>
<!-- Evaluation table -->
<div class="row"><h3 id="tea.evalTable.table.title"><spring:message code="tea.evalTable.table.title"/> <c:forEach
        var="subject" items="${subjectList}"> <c:if test="${subject.id == selectedSubjectId}"> - ${subject.name}</c:if>
</c:forEach></h3></div>
<c:if test="${not empty filterSubjectId && filterSubjectId != -1}">
    <table id="tea.evalTable.table" class="table table-hover table-bordered">
        <thead class="thead-inverse">
        <tr>
            <th><spring:message code="tea.evalTable.table.examDateColumn.label"/></th>
            <th><spring:message code="tea.evalTable.table.participantColumn.label"/></th>
            <th><spring:message code="tea.evalTable.table.gradeColumn.label"/></th>
        </tr>
        </thead>
        <tbody>
        <c:if test="${not empty listOfExamTerms}">
            <c:forEach var="examinationDate" items="${listOfExamTerms}" varStatus="listOfExamTermsLoop">
                <tr id="tea.evalTable.table.exam-${listOfExamTermsLoop.index}.row-0">
                <td
                        <c:if test="${fn:length(examinationDate.participants) > 0}">rowspan="${fn:length(examinationDate.participants)}"</c:if>>
                    <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${examinationDate.dateOfTest}"/></td>
                <c:forEach var="participant" items="${examinationDate.participants}" varStatus="loop">
                    <c:set var="participantHasGrade" value="false"/>
                    <c:set var="gradeDisabled" value=""/>
                    <c:if test="${loop.index != 0}"><tr id="tea.evalTable.table.exam-${listOfExamTermsLoop.index}.row-${loop.index}"></c:if>
                    <td>${participant.firstName} ${participant.lastName}</td>
                    <td>
                        <c:forEach var="grade" items="${gradeList}" varStatus="gradeListLoop">
                            <c:if test="${(grade.dayOfGrant == examinationDate.dateOfTest) && (grade.owner.id == participant.id)}">
                                <c:set var="participantHasGrade" value="true"/>
                                <c:set var="gradeIdToUpdate" value="${grade.id}"/>
                                <c:set var="gradeTypeIdToUpdate" value="${grade.typeOfGrade.id}"/>
                                <c:forEach var="grade2" items="${gradeList}" varStatus="innerGradeListLoop">
                                    <c:if test="${(grade2.owner.id == participant.id) && (grade.dayOfGrant.before(grade2.dayOfGrant))}">
                                        <c:set var="gradeDisabled" value="disabled"/>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                        </c:forEach>
                        <c:if test="${participantHasGrade == false}">
                            <!-- Create new grade -->
                            <form class="form-inline"
                                  action="<c:url value="/teacher-view/evaluationTable/createNewGrade"/>" method="post">
                                <input type="hidden" value="${participant.id}" name="studentId"/>
                                <input type="hidden" value="${examinationDate.subject.id}" name="subjectId"/>
                                <input type="hidden" value="${examinationDate.id}" name="examinationDateId"/>
                                <label class="sr-only" for="tea.evalTable.table.gradeSelect-${loop.index}">Grade</label>
                                <select id="tea.evalTable.table.gradeSelect-${loop.index}" name="gradeTypeId"
                                        class="form-control mb-2 mr-sm-2 mb-sm-0"
                                        title="<spring:message code="tea.evalTable.table.gradeSelect.title"/>">
                                    <option value="-1" selected>-</option>
                                    <c:forEach var="gradeType" items="${gradeTypes}">
                                        <option value="${gradeType.id}">${gradeType.name}</option>
                                    </c:forEach>
                                </select>
                                <button id="tea.evalTable.table.gradeSubmitButton-${loop.index}" type="submit"
                                        class="btn btn-primary"
                                        title="<spring:message code="tea.evalTable.table.gradeSubmitButton.title"/>">
                                    <spring:message code="tea.evalTable.table.gradeSubmitButton.label"/>
                                </button>
                            </form>
                        </c:if>
                        <c:if test="${participantHasGrade == true}">
                            <!-- Update existent Grade -->
                            <form class="form-inline"
                                  action="<c:url value="/teacher-view/evaluationTable/updateGrade"/>" method="post">
                                <input type="hidden" value="${gradeIdToUpdate}" name="gradeId"/>
                                <label class="sr-only" for="tea.evalTable.table.gradeSelect-${loop.index}">Grade</label>
                                <select id="tea.evalTable.table.gradeSelect-${loop.index}"
                                        name="gradeTypeId"
                                        class="form-control mb-2 mr-sm-2 mb-sm-0"
                                        title="<spring:message code="tea.evalTable.table.gradeSelect.title"/>"
                                    ${gradeDisabled}>
                                    <c:forEach var="gradeType" items="${gradeTypes}">
                                        <option <c:if test="${gradeTypeIdToUpdate == gradeType.id}">selected
                                                </c:if>value="${gradeType.id}">${gradeType.name}</option>
                                    </c:forEach>
                                </select>
                                <button id="tea.evalTable.table.gradeSubmitButton-${loop.index}"
                                        type="submit" class="btn btn-primary"
                                        title="<spring:message code="tea.evalTable.table.gradeSubmitButton.title"/>"
                                    ${gradeDisabled}>
                                    <spring:message code="tea.evalTable.table.gradeSubmitButton.label"/>
                                </button>
                            </form>
                        </c:if>
                    </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty examinationDate.participants}">
                    <td class="text-center" colspan="2"><spring:message
                            code="tea.evalTable.table.noRecords.noStudents"/></td>
                    </tr></c:if>
            </c:forEach>
        </c:if>
        <c:if test="${empty listOfExamTerms}">
            <tr>
                <td colspan="3" class="text-center"><spring:message
                        code="tea.evalTable.table.noRecords.noExamDates"/></td>
            </tr>
        </c:if>
        </tbody>
    </table>
</c:if>