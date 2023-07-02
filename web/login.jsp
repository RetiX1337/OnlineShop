<%@ page import="com.company.configuration.DependencyManager" %><%--
  Created by IntelliJ IDEA.
  User: matve
  Date: 02.07.2023
  Time: 13:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="styles/styles.css"/>
    <title>Login</title>
</head>
<body>
<div class="container">
    <h1>Login</h1>
<form action="login-processing" method="post">
    <label for="customerId">Customer ID:</label>
    <input type="text" name="customerId" id="customerId" required><br>
    <label for="shopId">Shop ID:</label>
    <input type="text" name="shopId" id="shopId" required><br>
    <input class="input-submit" type="submit" value="Submit">
</form>
</div>
</body>
</html>
