<%@ page import="com.company.core.models.goods.ProductWithQuantity" %>
<%@ page import="com.company.configuration.DependencyManager" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.company.core.models.user.User" %>
<%@ page import="com.company.core.models.user.customer.Cart" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    //    Long shopId = Long.valueOf(request.getSession().getAttribute("shopId").toString());
    Long shopId = 1L;
    User user = (User) session.getAttribute("user");
    Cart cart = (Cart) session.getAttribute("cart");
    Collection<ProductWithQuantity> products = DependencyManager.getInstance().getProductController().getProductsWithQuantity(shopId);
    request.setAttribute("products", products);
    boolean isLoggedIn = user != null;
    boolean hasCart = cart != null;
%>
<html>
<head>
    <link rel="stylesheet" href="<c:url value="/styles/styles.css"/>"/>
    <title>Product Browse</title>
    <script>
        function closeErrorMessage() {
            document.getElementById("error-message").style.display = "none";
        }
    </script>
</head>
<body>
<header>
    <div class="header-left">
        <a href="browse-products">Browse Products</a>
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

<% if (session.getAttribute("browseProductsQuantityModifyError") != null) { %>
<div class="error-message" id="error-message">
    <span class="close-button" onclick="closeErrorMessage()">&times;</span>
    <%= session.getAttribute("browseProductsQuantityModifyError").toString() %>
    <%session.removeAttribute("browseProductsQuantityModifyError");%>
</div>
<% } %>
<div class="container">
    <h1>Products</h1>
    <div class="grid-container">
        <% for (ProductWithQuantity productWithQuantity : products) { %>
        <div class="product-card">
            <h3 class="title"><%=productWithQuantity.getProduct().getBrand() + " " + productWithQuantity.getProduct().getName()%>
            </h3>
            <p class="product-card-element"><%=productWithQuantity.getProduct().getPrice()%>
            </p>
            <p class="product-card-element">Storage quantity: <%=productWithQuantity.getQuantity()%>
            </p>
            <%if (isLoggedIn && hasCart) {%>
            <p class="product-card-element">Cart
                quantity: <%=DependencyManager.getInstance().getCartController().getProductQuantity(cart, productWithQuantity.getProduct().getId())%>
            </p>
            <% } %>
            <form method="POST" action="/c/update-quantity?action=add&productId=<%=productWithQuantity.getProduct().getId()%>">
                <input class="btn-add" type="submit" value="+"/>
            </form>
            <form method="POST" action="/c/update-quantity?action=delete&productId=<%=productWithQuantity.getProduct().getId()%>">
                <input class="btn-delete" type="submit" value="-"/>
            </form>
        </div>
        <% } %>
    </div>
</div>
</body>
</html>
