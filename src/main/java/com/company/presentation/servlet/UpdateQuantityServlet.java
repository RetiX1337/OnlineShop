package com.company.presentation.servlet;


import com.company.Main;
import com.company.configuration.DependencyManager;
import com.company.core.controllers.TestController;
import com.company.core.models.user.customer.Customer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "update-quantity-servlet", value = "/update-quantity")
public class UpdateQuantityServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");
        Long productId = Long.valueOf(request.getParameter("productId"));
        Long shopId = Long.valueOf(request.getSession().getAttribute("shopId").toString());
        Customer customer = (Customer) request.getSession().getAttribute("customer");

        System.out.println(customer.getUsername());
        System.out.println(customer.getShoppingCart().getProductsFromCart());
        if (action.equals("add")) {
            DependencyManager.getInstance().getCartController().addToCart(customer, shopId, productId);
        } else if (action.equals("delete")) {
            DependencyManager.getInstance().getCartController().deleteFromCart(customer, productId);
        }

        response.sendRedirect("/browse-products");
    }
}
