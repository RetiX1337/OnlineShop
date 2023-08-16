<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.company.core.models.user.User" %>
<%@ page import="com.company.core.models.user.UserRole" %>
<%@ page import="java.util.Set" %><%--
  Created by IntelliJ IDEA.
  User: matve
  Date: 16.08.2023
  Time: 14:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<% User user = (User) request.getSession().getAttribute("user"); %>
<% Set<UserRole> userRoles = user.getRoles(); %>
<html>
<head>
  <link rel="stylesheet" href="<c:url value="/styles/styles.css"/>"/>
  <title>Login</title>
</head>
<body>
<div class="container">
  <h1>Choose Role</h1>
  <%for (UserRole userRole : userRoles) { %>
  <form action="<c:url value="authorization"/>" method="post">
    <input type="hidden" name="userRole" value="<%=userRole.name()%>">
    <input type="submit" value="<%=userRole.name()%>">
  </form>
  <% } %>
</div>
</body>
</html>