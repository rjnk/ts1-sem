<%@ page import="java.util.Objects" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 26.08.2017
  Time: 14:22
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String view = (String) request.getAttribute("view");
%>
<c:if test = "${sessionScope.user_view == 'student-view'}">
<div id="side-menu-panel" class="bootstrap-vertical-nav">
    <ul id="menu" class="nav nav-pills flex-column">
        <li class="nav-item">
            <c:choose>
                <c:when test="${changeOverview == false}">
                    <a id="stu.menu.overview" class="nav-link<%if(Objects.equals(view, "overview")){%> active<%}%>" href="<c:url value="/student-view/overview"/>" title="<spring:message code="stu.side-menu.overview.tooltip"/>"><spring:message code="stu.side-menu.overview"/></a>
                </c:when>
                <c:otherwise>
                    <a id="stu.menu.overview" class="nav-link<%if(Objects.equals(view, "overview")){%> active<%}%>" href="<c:url value="/student-view/otherExamDates"/>" title="<spring:message code="stu.side-menu.overview.tooltip"/>"><spring:message code="stu.side-menu.overview"/></a>
                </c:otherwise>
            </c:choose>
            </li>
        <li class="nav-item">
            <a id="stu.menu.mySubjects" class="nav-link<%if(Objects.equals(view, "mySubjects")){%> active<%}%>" href="<c:url value="/student-view/mySubjects"/>" title="<spring:message code="stu.side-menu.mySubjects.tooltip"/>"><spring:message code="stu.side-menu.mySubjects"/></a>
        </li>
        <li class="nav-item">
            <a id="stu.menu.otherSubjects" class="nav-link<%if(Objects.equals(view, "otherSubjects")){%> active<%}%>" href="<c:url value="/student-view/otherSubjects"/>" title="<spring:message code="stu.side-menu.otherSubjects.tooltip"/>"><spring:message code="stu.side-menu.otherSubjects"/></a>
        </li>
        <li class="nav-item">
            <a id="stu.menu.myExamDates" class="nav-link<%if(Objects.equals(view, "myExamDates")){%> active<%}%>" href="<c:url value="/student-view/myExamDates"/>" title="<spring:message code="stu.side-menu.myExaminationTerms.tooltip"/>"><spring:message code="stu.side-menu.myExaminationTerms"/></a>
        </li>
        <li class="nav-item">
            <a id="stu.menu.otherExamDates" class="nav-link<%if(Objects.equals(view, "otherExamDates")){%> active<%}%>" href="<c:url value="/student-view/otherExamDates"/>" title="<spring:message code="stu.side-menu.otherExaminationTerms.tooltip"/>"><spring:message code="stu.side-menu.otherExaminationTerms"/></a>
        </li>
    </ul>
</div>
</c:if>

<c:if test = "${sessionScope.user_view == 'teacher-view'}">
    <div id="side-menu-panel" class="bootstrap-vertical-nav">
        <ul id="menu" class="nav nav-pills flex-column">
            <li class="nav-item">
                <a id="tea.menu.overview" class="nav-link<%if(Objects.equals(view, "overview")){%> active<%}%>" href="<c:url value="/teacher-view/overview"/>" title="<spring:message code="tea.side-menu.overview.tooltip"/>"><spring:message code="tea.side-menu.overview"/></a>
            </li>
            <li class="nav-item">
                <a id="tea.menu.mySubjects" class="nav-link<%if(Objects.equals(view, "mySubjects")){%> active<%}%>" href="<c:url value="/teacher-view/mySubjects"/>" title="<spring:message code="tea.side-menu.mySubject.tooltip"/>"><spring:message code="tea.side-menu.mySubjects"/></a>
            </li>
            <li class="nav-item">
                <a id="tea.menu.myExamDates" class="nav-link<%if(Objects.equals(view, "myExamDates")){%> active<%}%>" href="<c:url value="/teacher-view/myExamDates"/>" title="<spring:message code="tea.side-menu.myExamDates.tooltip"/>"><spring:message code="tea.side-menu.myExamTerms"/></a>
            </li>
            <li class="nav-item">
                <a id="tea.menu.newExamDates" class="nav-link<%if(Objects.equals(view, "newExamDates")){%> active<%}%>" href="<c:url value="/teacher-view/newExamDates"/>" title="<spring:message code="tea.side-menu.newExamDates.tooltip"/>"><spring:message code="tea.side-menu.newExamTerm"/></a>
            </li>
            <li class="nav-item">
                <a id="tea.menu.setEvaluation" class="nav-link<%if(Objects.equals(view, "setEvaluation")   ){%> active<%}%>" href="<c:url value="/teacher-view/setEvaluation"/>" title="<spring:message code="tea.side-menu.setEvaluation.tooltip"/>"><spring:message code="tea.side-menu.setEvaluation"/></a>
            </li>
            <li class="nav-item">
                <a id="tea.menu.evaluationTable" class="nav-link<%if(Objects.equals(view, "evaluationTable")){%> active<%}%>" href="<c:url value="/teacher-view/evaluationTable"/>" title="<spring:message code="tea.side-menu.evaluationTable.tooltip"/>"><spring:message code="tea.side-menu.evaluationTable"/></a>
            </li>
            <li class="nav-item">
                <a id="tea.menu.otherSubjects" class="nav-link<%if(Objects.equals(view, "otherSubjects")){%> active<%}%>" href="<c:url value="/teacher-view/otherSubjects"/>" title="<spring:message code="tea.side-menu.othersSubjects.tooltip"/>"><spring:message code="tea.side-menu.notTeachedSubjects"/></a>
            </li>
            <li class="nav-item">
                <a id="tea.menu.listOfAllTeachers" class="nav-link<%if(Objects.equals(view, "listOfAllTeachers")){%> active<%}%>" href="<c:url value="/teacher-view/listOfAllTeachers"/>" title="<spring:message code="tea.side-menu.listOfAllTeachers.tooltip"/>"><spring:message code="tea.side-menu.allTeachers"/></a>
            </li>
        </ul>
    </div>
</c:if>