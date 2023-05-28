<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%--
  Author: Jiri Matyas
  Date: 14.12.2017
  Time: 20:19
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>DB content</title>
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="<spring:url value="css/bootstrap.min.css"/>">
    <!-- Default stylesheet -->
    <link rel="stylesheet" href="<spring:url value="css/style.css"/>">
    <link rel="shortcut icon" href="<spring:url value="images/favico-uis.ico"/>" type="image/x-icon">
</head>
<body>
<!-- Top Menu -->
<c:import url="WEB-INF/pages/modules/menus/menu.jsp"/>
<!-- Page content -->
<div id="main-content" class="container-fluid">
    <h1>Initial DB content</h1>
    <div id="warningAlert" class="alert alert-warning" role="alert">
        <strong>Warning!</strong> This content is not influenced by possible import another testing data.
    </div>

    <strong>Generally:</strong>
    <ul>
        <li>Username = &lt;last name&gt; in lower case</li>
        <li>Password = "pass" in every cases</li>
        <li>email = &lt;username&gt;<code>@mail.edu</code></li>
    </ul>
    <table class="table">
        <tr>
            <td>
                <h2>Teachers</h2>
                <table>
                    <tr>
                        <th>Last name</th>
                        <th>First name</th>
                    </tr>
                    <tr>
                        <td>Easyrider</td>
                        <td>Julia</td>
                    </tr>
                    <tr>
                        <td>Keen</td>
                        <td>Olivia</td>
                    </tr>
                    <tr>
                        <td>Lazy</td>
                        <td>John</td>
                    </tr>
                    <tr>
                        <td>Pedant</td>
                        <td>Alice</td>
                    </tr>
                    <tr>
                        <td>Scatterbrained</td>
                        <td>Thomas</td>
                    </tr>
                    <tr>
                        <td>Strict</td>
                        <td>Peter</td>
                    </tr>
                </table>
            </td>
            <td>
                &nbsp;
            </td>
            <td>
                <h2>Students</h2>
                <table>
                    <tr>
                        <th>Last name</th>
                        <th>First name</th>
                    </tr>
                    <tr>
                        <td>Blue</td>
                        <td>James</td>
                    </tr>
                    <tr>
                        <td>Brown</td>
                        <td>Noah</td>
                    </tr>
                    <tr>
                        <td>Cyan</td>
                        <td>Ethan</td>
                    </tr>
                    <tr>
                        <td>Gray</td>
                        <td>Michael</td>
                    </tr>
                    <tr>
                        <td>Green</td>
                        <td>Benjamin</td>
                    </tr>
                    <tr>
                        <td>Magenta</td>
                        <td>Emily</td>
                    </tr>
                    <tr>
                        <td>Maroon</td>
                        <td>William</td>
                    </tr>
                    <tr>
                        <td>Orange</td>
                        <td>Mia</td>
                    </tr>
                    <tr>
                        <td>Pink</td>
                        <td>Emma</td>
                    </tr>
                    <tr>
                        <td>Purple</td>
                        <td>Charlotte</td>
                    </tr>
                    <tr>
                        <td>Red</td>
                        <td>Sophia</td>
                    </tr>
                    <tr>
                        <td>Yellow</td>
                        <td>Isabella</td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>

    <h2>Subjects Taught and Exam Dates Released</h2>
    <table style="width:100%" class="table-bordered">
        <tr>
            <th rowspan="2">Subject</th>
            <th rowspan="2">Credits</th>
            <th class="text-center" colspan="6"><strong>T</strong><small>aught subject</small><br><strong>r</strong><small>eleased exam date</small></th>
        </tr>
        <tr>
            <td>Easyrider</td>
            <td>Keen</td>
            <td>Lazy</td>
            <td>Pedant</td>
            <td>Scatterbrained</td>
            <td>Strict</td>
        </tr>
        <tr>
            <td rowspan="2">Computation Structures</td>
            <td rowspan="2">5</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>T</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Computer System Engineering</td>
            <td rowspan="2">6</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>T</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Database Systems</td>
            <td rowspan="2">3</td>
            <td>&nbsp;</td>
            <td>T</td>
            <td>&nbsp;</td>
            <td>T</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>-</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Fundamentals of Computer Networks</td>
            <td rowspan="2">3</td>
            <td>&nbsp;</td>
            <td>T</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Introduction to Algorithms</td>
            <td rowspan="2">3</td>
            <td>&nbsp;</td>
            <td>T</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Linear Algebra</td>
            <td rowspan="2">1</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Mobile Applications</td>
            <td rowspan="2">5</td>
            <td>&nbsp;</td>
            <td>T</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Numerical Methods</td>
            <td rowspan="2">2</td>
            <td>T</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&dash;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Operating Systems</td>
            <td rowspan="2">2</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>T</td>
            <td>&nbsp;</td>
            <td>T</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>-</td>
            <td>&nbsp;</td>
            <td>r</td>
        </tr>
        <tr>
            <td rowspan="2">Programming in Java</td>
            <td rowspan="2">4</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>T</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
        </tr>
        <tr>
            <td rowspan="2">Programming Techniques</td>
            <td rowspan="2">1</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>T</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Software Engineering</td>
            <td rowspan="2">6</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>T</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
        </tr>
        <tr>
            <td rowspan="2">Software Quality Assurance</td>
            <td rowspan="2">5</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>T</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
        </tr>
        <tr>
            <td rowspan="2">Web Programming</td>
            <td rowspan="2">4</td>
            <td>&nbsp;</td>
            <td>T</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
    </table>

    <h2>Subjects enrolled and Registered Exam Dates</h2>
    <table style="width:100%" class="table-bordered">
        <tr>
            <th rowspan="2">Subject</th>
            <th rowspan="2">Credits</th>
            <th class="text-center" colspan="12"><strong>E</strong><small>nrolled subject</small><br><strong>r</strong><small>egistered exam date</small></th>
        </tr>
        <tr>
            <td>Blue</td>
            <td>Brown</td>
            <td>Cyan</td>
            <td>Gray</td>
            <td>Green</td>
            <td>Magenta</td>
            <td>Maroon</td>
            <td>Orange</td>
            <td>Pink</td>
            <td>Purple</td>
            <td>Red</td>
            <td>Yellow</td>
        </tr>
        <tr>
            <td rowspan="2">Computation Structures</td>
            <td rowspan="2">5</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>E</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>E</td>
        </tr>
        <tr>
            <td>&ndash;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>r</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>r</td>
        </tr>
        <tr>
            <td rowspan="2">Computer System Engineering</td>
            <td rowspan="2">6</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>E</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>r</td>
        </tr>
        <tr>
            <td rowspan="2">Database Systems</td>
            <td rowspan="2">3</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Fundamentals of Computer Networks</td>
            <td rowspan="2">3</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&ndash;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Introduction to Algorithms</td>
            <td rowspan="2">3</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&ndash;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
        </tr>
        <tr>
            <td rowspan="2">Linear Algebra</td>
            <td rowspan="2">1</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Mobile Applications</td>
            <td rowspan="2">5</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&ndash;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Numerical Methods</td>
            <td rowspan="2">2</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&ndash;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Operating Systems</td>
            <td rowspan="2">2</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Programming in Java</td>
            <td rowspan="2">4</td>
            <td>E</td>
            <td>E</td>
            <td>E</td>
            <td>E</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>E</td>
            <td>E</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>E</td>
        </tr>
        <tr>
            <td>&ndash;</td>
            <td>r</td>
            <td>r</td>
            <td>r</td>
            <td>&ndash;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>r</td>
            <td>&ndash;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>r</td>
        </tr>
        <tr>
            <td rowspan="2">Programming Techniques</td>
            <td rowspan="2">1</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&ndash;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Software Engineering</td>
            <td rowspan="2">6</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>E</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&ndash;</td>
            <td>&ndash;</td>
        </tr>
        <tr>
            <td rowspan="2">Software Quality Assurance</td>
            <td rowspan="2">5</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
        </tr>
        <tr>
            <td rowspan="2">Web Programming</td>
            <td rowspan="2">4</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>E</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>E</td>
        </tr>
        <tr>
            <td>&ndash;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>r</td>
            <td>r</td>
            <td>&nbsp;</td>
            <td>&nbsp;</td>
            <td>r</td>
            <td>&nbsp;<td>
            <td>&nbsp;</td>
            <td>&ndash;</td>
        </tr>
    </table>
</div>

<!-- Optional JavaScript -->
<!-- jQuery first, then Popper.js, then Bootstrap JS -->
<script src="<spring:url value="js/jquery-3.1.1.slim.min.js"/>"></script>
<script src="<spring:url value="js/popper.min.js"/>"></script>
<script src="<spring:url value="js/bootstrap.min.js"/>"></script>
</body>
</html>
