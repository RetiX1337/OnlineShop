<%@ page import="com.company.configuration.DependencyManager" %>
<%@ page import="com.company.core.models.user.customer.Customer" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="styles/styles.css"/>
    <title>Login processing...</title>
</head>
<body>

<div class="container">
<%
    String customerId = request.getParameter("customerId");
    String shopId = request.getParameter("shopId");
    boolean customerExists = DependencyManager.getInstance().getCustomerController().isPresent(Long.valueOf(customerId));
    boolean shopExists = DependencyManager.getInstance().getShopController().isPresent(Long.valueOf(shopId));
%>

<% if (customerExists && shopExists) { %>
<%
    Customer customer = DependencyManager.getInstance().getCustomerController().findCustomer(Long.valueOf(customerId));
    session.setAttribute("customer", customer);
    session.setAttribute("shopId", shopId);
%>
<p>Logged in successfully! Customer ID: <%= customerId %>, Shop ID: <%= shopId %></p>
<a class="button" href="menu">Go to menu</a>
<% } else { %>
<p>Incorrect Customer ID or Shop ID</p>
<a class="button" href="login">Back to login page</a>
<% } %>
</div>
</body>
</html>
