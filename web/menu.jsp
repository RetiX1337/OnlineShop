<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.company.core.models.user.User" %><%--
  Created by IntelliJ IDEA.
  User: matve
  Date: 02.07.2023
  Time: 13:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/styles/styles.css"/>"/>
    <title>Menu</title>
</head>
<body>
<% User user = (User) session.getAttribute("user"); %>
<%
    boolean isLoggedIn = user != null;
%>
<header>
    <div class="header-left">
        <a href=/browse-products>Browse Products</a>
    </div>
    <div class="header-right">
        <div class="profile-menu">
            <% if (isLoggedIn) { %>
            <span class="username">
                <%=user.getUsername()%>
            </span>
            <div class="dropdown-content">
                <a href="<c:url value="/c/cart"/>">Show Cart</a>
                <a href="<c:url value="/c/orders"/>">Show Orders</a>
            </div>
            <%} else { %>
                <a href="<c:url value="/login"/>">Login</a>
            <% } %>
        </div>
    </div>
</header>
<div class="grid">
    <a class="button" href="<c:url value="/browse-products"/>">Browse products</a>
    <a class="button" href="<c:url value="/c/orders"/>">Display orders</a>
    <a class="button" href="<c:url value="/c/cart"/>">Display cart</a>
</div>
</body>
</html>