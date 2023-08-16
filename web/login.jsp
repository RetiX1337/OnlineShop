<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.company.configuration.DependencyManager" %><%--
  Created by IntelliJ IDEA.
  User: matve
  Date: 02.07.2023
  Time: 13:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/styles/styles.css"/>"/>
    <title>Login</title>
</head>
<body>
<div class="container">
    <h1>Login</h1>
    <form action="<c:url value="authentication"/>" method="post">
        <label for="email">Email address:</label>
        <input type="text" name="email" id="email" required><br>
        <label for="password">Password:</label>
        <input type="text" name="password" id="password" required><br>
        <input class="input-submit" type="submit" value="Submit">
    </form>
    <% if (session.getAttribute("loginError") != null) { %>
    <% String error = session.getAttribute("loginError").toString(); %>
    <p><%=error%></p>
    <% session.removeAttribute("loginError"); %>
    <% } %>
</div>
</body>
</html>
