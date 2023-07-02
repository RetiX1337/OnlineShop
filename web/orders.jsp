<%@ page import="com.company.core.models.user.customer.Customer" %>
<%@ page import="com.company.core.models.goods.Order" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.company.configuration.DependencyManager" %><%--
  Created by IntelliJ IDEA.
  User: matve
  Date: 02.07.2023
  Time: 19:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="styles/styles.css"/>
    <title>Orders</title>
</head>
<body>
<%
    Customer customer = (Customer) session.getAttribute("customer");
    Collection<Order> orders = DependencyManager.getInstance().getOrderController().getOrders(customer);
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
            <td><%=order.getId()%></td>
            <td><%=order.getSummaryPrice()%></td>
            <td><%=order.getOrderStatus().toString()%></td>
        </tr>
        <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
