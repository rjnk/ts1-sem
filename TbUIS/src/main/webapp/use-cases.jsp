<%--
  Author: Jiri Matyas
  Date: 21.10.2017
  Time: 13:49
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Use-cases</title>

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="<spring:url value="css/bootstrap.min.css"/>">
    <!-- Default stylesheet -->
    <link rel="stylesheet" href="<spring:url value="css/style.css"/>">
    <link rel="shortcut icon" href="<spring:url value="images/favico-uis.ico"/>" type="image/x-icon">
</head>

<body>
<!-- Top Menu -->
<c:import url="WEB-INF/pages/modules/menus/menu.jsp"/>
<div id="main-content" class="container">
    <!-- <h3>Unlogged user use-cases</h3>-->
    <h3>Use-cases</h3>
    <table class="table table-hover">
        <thead class="table-inverse">
            <tr>
                <th colspan="3"><strong>Actor: Unlogged user</strong></th>
            </tr>
            <tr>
                <th>ID</th>
                <th>Name of UC</th>
                <th>Context of Use</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-01.html" target="_blank">UC.01</a></td>
                <td>Application login</td>
                <td>Registered user will log into the application and based on the login name the user will be recognized
                    either as a student or a teacher
                </td>
            </tr>
            <tr>
                <th colspan="3"><strong>Actor: General logged user &mdash; student or teacher</strong></th>
            </tr>
            <tr>
                <th>ID</th>
                <th>Name of UC</th>
                <th>Context of Use</th>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-02.html" target="_blank">UC.02</a></td>
                <td>Application logout</td>
                <td>Logged in user is logging out of the Application</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-03.html" target="_blank">UC.03</a></td>
                <td>Change of user&#39;s personal data</td>
                <td>Logged in user can change his/her first name and/or last name and/or email</td>
            </tr>
            <tr>
                <th colspan="3"><strong>Actor: Logged users &mdash; Student</strong></th>
            </tr>
            <tr>
                <th>ID</th>
                <th>Name of UC</th>
                <th>Context of Use</th>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-04.html" target="_blank">UC.04</a></td>
                <td>Cancelling the registration (enrollment) to a subject</td>
                <td>Student cancels the registration to a subject he/she previously enrolled</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-05.html" target="_blank">UC.05</a></td>
                <td>Displays subjects with passed examination</td>
                <td>Summarized information about all subjects with successful examination and their evaluations</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-06.html" target="_blank">UC.06</a></td>
                <td>Subject enrollment</td>
                <td>Student enrolls to one of the so far not enrolled subjects</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-07.html" target="_blank">UC.07</a></td>
                <td>Cancellation of registered exam date</td>
                <td>Student unregisters his/her exam date for the selected subject</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-08.html" target="_blank">UC.08</a></td>
                <td>Registration of a new exam date</td>
                <td>Student registers a new exam date for the selected subject</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-09.html" target="_blank">UC.09</a></td>
                <td>Displays all examination participants <strong>&mdash;</strong> classmates</td>
                <td>Summarized overview of all students registered for the date of an examination for a specific subject (&quot;participants&quot;),
                    i.e. the student&#39;s classmates
                </td>
            </tr>
            <tr>
                <th colspan="3"><strong>Actor: Logged user &mdash; Teacher</strong></th>
            </tr>
            <tr>
                <th>ID</th>
                <th>Name of UC</th>
                <th>Context of Use</th>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-10.html" target="_blank">UC.10</a></td>
                <td>Cancellation of registered subject</td>
                <td>Teacher cancels a subject he/she teaches</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-11.html" target="_blank">UC.11</a></td>
                <td>Displays students registered in the subject</td>
                <td>Summarized list of all students attending the specific subject</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-12.html" target="_blank">UC.12</a></td>
                <td>Cancellation of the exam date</td>
                <td>Teacher cancels the exam date for his/her subject</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-13.html" target="_blank">UC.13</a></td>
                <td>Displays all examination participants</td>
                <td>An overview of all students registered for the date of an examination for a specific subject (&quot;participants&quot;)</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-14.html" target="_blank">UC.14</a></td>
                <td>Announcement of a new exam date</td>
                <td>Teacher announces a new exam date for his/her subject</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-15.html" target="_blank">UC.15</a></td>
                <td>Enters the examination evaluation</td>
                <td>Teacher enters the examination evaluation of the selected student for the selected exam date he/she
                    teaches
                </td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-16.html" target="_blank">UC.16</a></td>
                <td>Enters or reenters (changes) the evaluation of the examination</td>
                <td>Teacher enters an evaluation of an examination of the selected student or changes already entered
                    evaluation
                </td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-17.html" target="_blank">UC.17</a></td>
                <td>Signs up to teach a subject</td>
                <td>Teacher begins to teach a certain subject</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-18.html" target="_blank">UC.18</a></td>
                <td>Displays the list of teachers and subjects they teach</td>
                <td>Overview of teachers and subjects they teach</td>
            </tr>
        </tbody>
        <thead>
            <tr>
                <th colspan="3"><strong>Actor: Unlogged user &mdash; a special activity for testing</strong></th>
            </tr>
            <tr>
                <th>ID</th>
                <th>Name of UC</th>
                <th>Context of Use</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-19.html" target="_blank">UC.19</a></td>
                <td>Restoration of the database</td>
                <td>Unlogged user restores DB&#39;s into the default state, usually before proceeding to the tests</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-20.html" target="_blank">UC.20</a></td>
                <td>Displays information about system&#39;s abilities</td>
                <td>Unlogged user gets information about existing use-cases, contents of databes and restrictions in the
                    UIS
                </td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-21.html" target="_blank">UC.21</a></td>
                <td>Export of the database</td>
                <td>Unlogged user saves the current content of the database as an external XML/JSON file</td>
            </tr>
            <tr>
                <td><a href="https://projects.kiv.zcu.cz/tbuis/web/files/uis/uc/en/html/use-case-22.html" target="_blank">UC.22</a></td>
                <td>Import of the database</td>
                <td>Unlogged user rewriters the current content of the database with the content of external XML or JSON
                    file
                </td>
            </tr>
        </tbody>
    </table>
    <h3>Diagram</h3>
    <img src="<spring:url value="/images/use-case.png"/>" class="img-fluid" alt="Use case diagram">
</div><!-- /.container -->


<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<!-- Bootstrap 4 -->
<script src="<spring:url value="js/jquery-3.1.1.slim.min.js"/>"></script>
<script src="<spring:url value="js/popper.min.js"/>"></script>
<script src="<spring:url value="js/bootstrap.min.js"/>"></script>

</body>
</html>
