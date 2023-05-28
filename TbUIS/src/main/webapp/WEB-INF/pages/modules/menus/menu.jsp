<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 26.08.2017
  Time: 14:04
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:if test="${not empty sessionScope.user_view}"><!-- Sign in user menu style -->
    <nav id="header" class="navbar navbar-expand-md navbar-dark fixed-top bg-dark student-nav ${sessionScope.user_view}-nav">
</c:if>

<c:if test="${empty sessionScope.user_view}"><!-- Public menu style -->
    <nav id="header" class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
</c:if>
    <a id="header.title.logoTitle" class="navbar-brand" href="<spring:url value="/index.jsp"/>" title="<spring:message code="header.title.logoTitle"/>"><spring:message code="header.link.logoTitle"/></a>
    <!-- Expand menu button for mobile devices -->
    <button id="header.title.expandButton" class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarsExampleDefault" aria-controls="navbarsExampleDefault" aria-expanded="false" aria-label="Toggle navigation" title="<spring:message code="header.title.expandButton"/>">
        <span class="navbar-toggler-icon"></span>
    </button>

    <!-- Collapsible menu items -->
    <div class="collapse navbar-collapse" id="navbarsExampleDefault">
        <!-- Left side -->
        <ul class="navbar-nav mr-auto">
            <li class="nav-item active">
                <a id="header.link.home" title="<spring:message code="header.title.home"/>" class="nav-link" href="<spring:url value="/index.jsp"/>"><spring:message code="header.link.home"/> <span class="sr-only">(current)</span></a>
            </li>
            <c:if test="${empty sessionScope.user_full_name}">
                <li class="nav-item">
                    <a id="header.link.exportImport" title="<spring:message code="header.title.exportImport"/>" class="nav-link" href="<spring:url value="/import-export"/>"><spring:message code="header.link.exportImport"/> </a>
                </li>
                <li class="nav-item">
                    <a id="header.link.restoreDB" title="<spring:message code="header.title.restoreDB"/>" class="nav-link" href="<spring:url value="/restoreDB"/>"><spring:message code="header.link.restoreDB"/> </a>
                </li>
                <li class="nav-item">
                    <a id="header.link.login" title="<spring:message code="header.title.login"/>" class="nav-link" href="<spring:url value="/login"/>"><spring:message code="header.link.login"/></a>
                </li>
            </c:if>
        </ul>
        <c:if test="${not empty sessionScope.user_full_name}">
        <!-- Right side (Logged in user only) -->
        <ul class="nav navbar-nav navbar-right">
            <li class="nav-item"><a id="header.title.userHome" title="<spring:message code="header.title.userHome"/>" class="nav-link" href="<spring:url value="/${sessionScope.user_view}"/>"><c:out value="${sessionScope.user_full_name}"/></a></li>
            <li class="nav-item"><a id="header.link.logout" title="<spring:message code="header.title.logout"/>" class="nav-link" href="<spring:url value="/logout"/>"><spring:message code="header.link.logout"/></a></li>
        </ul>
        </c:if>
    </div>
</nav>