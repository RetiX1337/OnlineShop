package com.company.presentation.servlet;

import com.company.configuration.DependencyManager;
import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.exceptions.IncorrectURLArgumentException;
import com.company.core.models.goods.Order;
import com.company.core.models.user.customer.Customer;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "order-servlet", value = "/orders/*")
public class OrderServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            Long orderId = Long.valueOf(pathInfo.substring(1));

            Order order = DependencyManager.getInstance().getOrderController().findOrderById(orderId);

            request.setAttribute("order", order);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/order-page");
            dispatcher.forward(request, response);
        }
    }
}