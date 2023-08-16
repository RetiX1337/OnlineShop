<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.company.configuration.DependencyManager" %>
<%@ page import="com.company.core.models.user.User" %><%--
  Created by IntelliJ IDEA.
  User: matve
  Date: 02.07.2023
  Time: 20:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    boolean isProcessed = Boolean.parseBoolean(request.getParameter("is-processed"));
%>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/styles/styles.css"/>"/>
    <title>Order Processing</title>
</head>
<body>

<header>
    <div class="header-left">
        <a href=/browse-products>Browse Products</a>
    </div>
    <div class="header-right">
        <div class="profile-menu">
            <span class="username">
                <%=user.getUsername()%>
            </span>
            <div class="dropdown-content">
            <a href="<c:url value="/c/cart"/>">Show Cart</a>
            <a href="<c:url value="/c/orders"/>">Show Orders</a>
            </div>
        </div>
    </div>
</header>
<div class="container">
    <% if (isProcessed) { %>
    <p>Processed successfully! Check your orders: </p>
    <a href=/c/orders>Show Orders</a>
    <% } else { %>
    <p>Something went wrong :(</p>
    <% } %>
</div>
</body>
</html>
