package com.company.presentation.servlet;

import com.company.configuration.DependencyManager;
import com.company.core.controllers.OrderController;
import com.company.core.exceptions.IncorrectURLArgumentException;
import com.company.core.models.goods.Order;
import com.company.core.models.user.User;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "order-servlet", value = "/c/orders/*")
public class OrderServlet extends HttpServlet {
    private OrderController orderController;

    @Override
    public void init() {
        this.orderController = DependencyManager.getInstance().getOrderController();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo != null) {
            Long orderId;

            try {
                orderId = Long.valueOf(pathInfo.substring(1));
            } catch (NumberFormatException e) {
                throw new IncorrectURLArgumentException();
            }

            Order order = orderController.findOrderById(orderId);

            User user = (User) request.getSession().getAttribute("user");

            if (!order.getUser().getId().equals(user.getId())) {
                throw new IncorrectURLArgumentException();
            }

            request.setAttribute("order", order);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/c/order-page");
            dispatcher.forward(request, response);
        }
    }
}