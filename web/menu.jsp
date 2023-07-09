<%@ page import="com.company.core.models.user.customer.Customer" %><%--
  Created by IntelliJ IDEA.
  User: matve
  Date: 02.07.2023
  Time: 13:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="styles/styles.css"/>
    <title>Menu</title>
</head>
<body>
<% Customer customer = (Customer) session.getAttribute("customer"); %>
<header>
    <div class="header-left">
        <a href="/browse-products">Browse Products</a>
    </div>
    <div class="header-right">
        <div class="profile-menu">
            <span class="username"><%=customer.getUsername()%></span>
            <div class="dropdown-content">
                <a href="/cart">Show Cart</a>
                <a href="/orders">Show Orders</a>
            </div>
        </div>
    </div>
</header>
<div class="grid">
    <a class="button" href="browse-products">Browse products</a>
    <a class="button" href="orders">Display orders</a>
    <a class="button" href="cart">Display cart</a>
</div>
</body>
</html>
