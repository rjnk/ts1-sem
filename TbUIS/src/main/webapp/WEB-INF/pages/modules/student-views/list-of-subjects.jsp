<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 10.09.2017
  Time: 15:31
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h3 id="stu.otherSubjects.table.title"><spring:message code="stu.otherSubjects.table.title"/></h3>
<table id="stu.otherSubjects.table" class="table table-hover">
    <thead class="thead-inverse">
    <tr>
        <th><spring:message code="stu.otherSubjects.table.numberColumn"/></th>
        <th><spring:message code="stu.otherSubjects.table.nameColumn"/></th>
        <c:if test="${hideTeacherColumn == false}"><th><spring:message code="stu.otherSubjects.table.teachersColumn"/></th></c:if>
        <th class="text-center"><spring:message code="stu.otherSubjects.table.creditRatingColumn"/></th>
        <th class="text-center"><spring:message code="stu.otherSubjects.table.registerColumn"/></th>
    </tr>
    </thead>
    <tbody>
    <c:if test="${not empty subjectList}">
        <c:forEach var="subject" items="${subjectList}" varStatus="subjectListLoop">
            <tr id="stu.otherSubjects.table.subjectRow-${subjectListLoop.index}">
                <td>${subjectListLoop.index + 1}</td>
                <td>${subject.name}</td>
                <c:if test="${hideTeacherColumn == false}"><td>
                    <c:forEach var="teacher" items="${subject.teachers}" varStatus="loop">
                        ${teacher.firstName} ${teacher.lastName}<c:if test="${!loop.last}">,</c:if>
                    </c:forEach>
                </td></c:if>
                <td class="text-center">${subject.creditRating}</td>
                <td class="text-center">
                    <form id="stu.otherSubjects.table.enrollSubjectForm-${subjectListLoop.index}" method="post">
                        <input id="stu.otherSubjects.table.subjectInput-${subjectListLoop.index}" type="hidden"
                               name="subjectId" value="${subject.id}">
                            <button id="stu.otherSubjects.table.enrollButton-${subjectListLoop.index}" type="submit"
                                    class="btn btn-primary btn-sm" title="<spring:message code="stu.otherSubjects.table.RegisterButtonTitle"/>"
                            <c:if test="${fn:length(subject.teachers) == 0 || isRegisterAllowed == false}"> disabled </c:if>>
                                <spring:message code="stu.otherSubjects.table.RegisterButtonLabel"/>
                            </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
    </c:if>
    <c:if test="${empty subjectList}">
        <tr id="stu.otherSubjects.table.NoRecords" class="text-center">
            <td colspan="5"><spring:message code="stu.otherSubjects.table.NoRecords"/></td>
        </tr>
    </c:if>
    </tbody>
</table>