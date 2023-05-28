<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--
  Author: Jiri Matyas
  Date: 13.09.2017
  Time: 10:12
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3 id="tea.otherSubjects.table.title"><spring:message code="tea.otherSubjects.table.title"/></h3>
<table id="tea.otherSubjects.table" class="table table-hover">
    <thead class="thead-inverse">
    <tr>
        <th><spring:message code="tea.otherSubjects.table.numberColumn"/></th>
        <th><spring:message code="tea.otherSubjects.table.nameColumn"/></th>
        <th><spring:message code="tea.otherSubjects.table.teachersColumn"/></th>
        <th><spring:message code="tea.otherSubjects.table.creditRatingColumn"/></th>
        <th><spring:message code="tea.otherSubjects.table.registerSubjectsColumn"/></th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${not empty subjectList}">
        <c:forEach var="subject" items="${subjectList}" varStatus="subjectLoop">
            <tr id="tea.otherSubjects.table.subjectRow-${subjectLoop.index}">
                <td>${subjectLoop.index + 1}</td>
                <td>${subject.name}</td>
                <td>
                    <c:forEach var="teacher" items="${subject.teachers}" varStatus="loop">
                        ${teacher.firstName} ${teacher.lastName}<c:if test="${!loop.last}">,</c:if>
                    </c:forEach>
                </td>
                <td>${subject.creditRating}</td>
                <td>
                    <form id="tea.otherSubjects.table.registerSubjectForm-${subjectLoop.index}" method="post">
                        <input id="tea.otherSubjects.table.subjectInput-${subjectLoop.index}" type="hidden" name="subjectId" value="${subject.id}">
                        <button id="tea.otherSubjects.table.participateButton-${subjectLoop.index}"
                                type="submit" class="btn btn-primary btn-sm"
                                title="<spring:message code="tea.otherSubjects.table.participateButtonTitle"/>"
                                <c:if test="${fn:length(subject.teachers) >= maxTeacherCount || not participationAllowed}">disabled</c:if>
                        ><spring:message code="tea.otherSubjects.table.participateButtonLabel"/></button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${empty subjectList}">
        <tr id="tea.otherSubjects.table.noRecords"><td class="text-center" colspan="5"><spring:message code="tea.otherSubjects.table.noRecords"/></tr>
    </c:if>
    </tbody>
</table>