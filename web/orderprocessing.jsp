<%@ page import="com.company.core.models.user.customer.Customer" %>
<%@ page import="com.company.configuration.DependencyManager" %><%--
  Created by IntelliJ IDEA.
  User: matve
  Date: 02.07.2023
  Time: 20:11
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="styles/styles.css"/>
    <title>Order Processing</title>
</head>
<body>

<%
    Customer customer = (Customer) session.getAttribute("customer");
    Long shopId = Long.valueOf(session.getAttribute("shopId").toString());
%>
<header>
    <div class="header-left">
        <a href="browse-products">Browse Products</a>
    </div>
    <div class="header-right">
        <div class="profile-menu">
            <span class="username"><%=customer.getUsername()%></span>
            <div class="dropdown-content">
                <a href="cart">Show Cart</a>
                <a href="orders">Show Orders</a>
            </div>
        </div>
    </div>
</header>
<%
    boolean isProcessed = DependencyManager.getInstance().getCartController().checkoutCart(customer, shopId);
%>
<div class="container">
    <% if (isProcessed) { %>
    <p>Processed successfully! Check your orders: </p>
    <a href="orders">Show Orders</a>
    <% } else { %>
    <p>Something went wrong :(</p>
    <% } %>
</div>
</body>
</html>
