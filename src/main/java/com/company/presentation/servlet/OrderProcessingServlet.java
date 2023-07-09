package com.company.presentation.servlet;

import com.company.configuration.DependencyManager;
import com.company.core.models.user.customer.Customer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "order-processing-servlet", value = "/order-processing")
public class OrderProcessingServlet extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        RequestDispatcher requestDispatcher = request.getRequestDispatcher("/order-confirmation");
        requestDispatcher.forward(request, response);
    }
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Customer customer = (Customer) request.getSession().getAttribute("customer");
        Long shopId = Long.valueOf(request.getSession().getAttribute("shopId").toString());
        boolean isProcessed = DependencyManager.getInstance().getCartController().checkoutCart(customer, shopId);
        response.sendRedirect("/order-processing?is-processed=" + isProcessed);
    }
}
