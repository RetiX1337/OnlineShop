<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.company.core.models.goods.Order" %>
<%@ page import="com.company.core.models.goods.Item" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.company.core.models.goods.Product" %>
<%@ page import="com.company.core.models.user.User" %>
<%--
  Created by IntelliJ IDEA.
  User: matve
  Date: 09.07.2023
  Time: 18:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Order order = (Order) request.getAttribute("order");
    User user = (User) session.getAttribute("user");
%>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/styles/styles.css"/>"/>
    <title>Order <%=order.getId()%>
    </title>
</head>
<body>
<header>
    <div class="header-left">
        <a href="<c:url value="/browse-products"/>">Browse Products</a>
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
    <div class="order-details">
        <h2>Order Details</h2>
        <div class="field">
            <span class="field-label">Order ID:</span> <%= order.getId() %>
        </div>
        <div class="field">
            <span class="field-label">Order Status:</span> <%= order.getOrderStatus().toString() %>
        </div>
        <div class="field">
            <span class="field-label">Summary Price:</span> <%= order.getSummaryPrice() %>
        </div>
        <div class="items">
            <h3>Items:</h3>
            <%
                Collection<Item> items = order.getItems();
                for (Item item : items) {
                    Product product = item.getProduct();
            %>
            <div class="item">
                <div><span class="field-label">Product Name:</span> <%= product.getBrand() + " " + product.getName()%>
                </div>
                <div><span class="field-label">Product Price:</span> <%= product.getPrice() %>
                </div>
                <div><span class="field-label">Quantity:</span> <%= item.getQuantity() %>
                </div>
                <div><span class="field-label">Summary price of item:</span> <%= item.getPrice()%>
                </div>
            </div>
            <% } %>
        </div>
    </div>
</div>
</body>
</html>
