<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.company.core.models.goods.Order" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.company.configuration.DependencyManager" %>
<%@ page import="com.company.core.models.user.User" %><%--
  Created by IntelliJ IDEA.
  User: matve
  Date: 02.07.2023
  Time: 19:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    Collection<Order> orders = DependencyManager.getInstance().getOrderController().getOrders(user);
%>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/styles/styles.css"/>"/>
    <title>Orders</title>
</head>
<body>
<header>
    <div class="header-left">
        <a href=/browse-products>Browse Products</a>
    </div>
    <div class="header-right">
        <div class="profile-menu">
            <span class="username"><%=user.getUsername()%></span>
            <div class="dropdown-content">
            <a href="<c:url value="/c/cart"/>">Show Cart</a>
            <a href="<c:url value="/c/orders"/>">Show Orders</a>
            </div>
        </div>
    </div>
</header>
<div class="container">
    <h1>Orders</h1>
    <table class="cart-table">
        <thead>
        <tr>
            <th>ID</th>
            <th>Price</th>
            <th>Order Status</th>
        </tr>
        </thead>
        <tbody>
        <% for (Order order : orders) { %>
        <tr>
            <td><a href=/c/orders/<%=order.getId()%>><%=order.getId()%></a></td>
            <td><%=order.getSummaryPrice()%></td>
            <td><%=order.getOrderStatus().toString()%></td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
