<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Author: Jiri Matyas
  Date: 31.12.2017
  Time: 15:50
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3 id="tea.listOfAllTeachers.table.title"><spring:message code="tea.listOfAllTeachers.table.title"/></h3>
<table id="tea.listOfAllTeachers.table" class="table table-hover">
    <thead class="thead-inverse">
    <tr>
        <th><spring:message code="tea.listOfAllTeachers.table.columnNumber"/></th>
        <th><spring:message code="tea.listOfAllTeachers.table.nameColumn"/></th>
        <th><spring:message code="tea.listOfAllTeachers.table.subjectsColumn"/></th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${not empty listOfAllTeachers}">
        <c:forEach var="teacher" items="${listOfAllTeachers}" varStatus="teacherLoop">
            <tr id="tea.listOfAllTeachers.table.teacherRow-${teacherLoop.index}">
                <td>${teacherLoop.index + 1}</td>
                <td>${teacher.firstName} ${teacher.lastName}</td>
                <td>
                    <c:forEach var="subject" items="${teacher.listOfTaughtSubjects}" varStatus="loop">
                        ${subject.name}<c:if test="${!loop.last}">,</c:if>
                    </c:forEach>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${empty listOfAllTeachers}">
        <tr id="tea.listOfAllTeachers.table.noRecords"><td class="text-center" colspan="3"><spring:message code="tea.listOfAllTeachers.table.noRecords"/></tr>
    </c:if>
    </tbody>
</table>
