<%@ page import="com.company.core.models.goods.ProductWithQuantity" %>
<%@ page import="com.company.configuration.DependencyManager" %>
<%@ page import="java.util.List" %>
<%@ page import="com.company.core.models.user.customer.Customer" %>
<%@ page import="java.util.Collection" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="styles/styles.css"/>
    <title>Product Browse</title>
</head>
<body>
<%
    Long shopId = Long.valueOf(session.getAttribute("shopId").toString());
    Customer customer = (Customer) session.getAttribute("customer");
    Collection<ProductWithQuantity> products = DependencyManager.getInstance().getProductController().getProducts(shopId);
    request.setAttribute("products", products);
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
    <h1>Products</h1>
<div class="grid-container">
    <% for (ProductWithQuantity product : products) { %>
        <div class="product-card">
            <h3 class="title"><%=product.getBrand() + " " + product.getName()%></h3>
            <p class="product-card-element"><%=product.getPrice()%></p>
            <p class="product-card-element">Storage quantity: <%=product.getQuantity()%></p>
            <p class="product-card-element">Cart quantity: <%=DependencyManager.getInstance().getCartController().getProductQuantity(customer, product.getId())%> </p>
            <form method="POST" action="update-quantity?action=add&productId=<%=product.getId()%>">
                <input class="btn-add" type="submit" value="+" />
            </form>
            <form method="POST" action="update-quantity?action=delete&productId=<%=product.getId()%>">
                <input class="btn-delete" type="submit" value="-" />
            </form>
        </div>
    <% } %>
</div>
</div>
</body>
</html>
