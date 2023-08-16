<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.company.core.models.goods.Item" %>
<%@ page import="com.company.core.models.user.User" %>
<%@ page import="com.company.core.models.user.customer.Cart" %><%--
  Created by IntelliJ IDEA.
  User: matve
  Date: 02.07.2023
  Time: 19:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    User user = (User) session.getAttribute("user");
    Cart cart = (Cart) session.getAttribute("cart");
    Collection<Item> items = cart.getCartItems();
%>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/styles/styles.css"/>"/>
    <title>Cart</title>
</head>
<body>
<header>
    <div class="header-left">
        <a href="c/browse-products">Browse Products</a>
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
    <h1>Shopping Cart</h1>
    <table class="cart-table">
        <thead>
        <tr>
            <th>Item</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Total</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
            <% for (Item item : items) { %>
            <tr>
                <td><%=item.getProduct().getBrand() + " " + item.getProduct().getName()%></td>
                <td><%=item.getProduct().getPrice()%></td>
                <td><%=item.getQuantity()%></td>
                <td><%=item.getPrice()%></td>

            </tr>
            <% } %>
        </tbody>
    </table>
    <div>
        <p>Summary price: <%=cart.getSummaryPrice()%></p>
        <form method="POST" action="<c:url value="/c/order-processing"/>">
            <input class="input-submit" type="submit" value="Checkout" />
        </form>
    </div>
</div>
</body>
</html>
