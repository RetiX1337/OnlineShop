package com.company.presentation.servlet;

import com.company.configuration.DependencyManager;
import com.company.core.controllers.CartController;
import com.company.core.models.user.customer.Cart;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "order-processing-servlet", value = "/c/order-processing")
public class OrderProcessingServlet extends HttpServlet {
    private CartController cartController;

    @Override
    public void init() {
        this.cartController = DependencyManager.getInstance().getCartController();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/c/order-confirmation");
        requestDispatcher.forward(request, response);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        Long shopId = Long.valueOf(request.getSession().getAttribute("shopId").toString());
        boolean isProcessed = cartController.checkoutCart(cart, shopId);
        response.sendRedirect("/c/order-processing?is-processed=" + isProcessed);
    }
}
