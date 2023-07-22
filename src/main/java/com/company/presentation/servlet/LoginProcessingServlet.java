package com.company.presentation.servlet;

import com.company.configuration.DependencyManager;
import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.models.user.customer.Customer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "login-processing-servlet", value = "/login-processing")
public class LoginProcessingServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Long parameterCustomerId = Long.valueOf(request.getParameter("customerId"));
        Long parameterShopId = Long.valueOf(request.getParameter("shopId"));
        try {
            Customer customer = DependencyManager.getInstance().getCustomerController().findCustomer(parameterCustomerId);
            Long shopId = DependencyManager.getInstance().getShopController().findShop(parameterShopId).getId();
            request.getSession().setAttribute("customer", customer);
            request.getSession().setAttribute("shopId", shopId);
            response.sendRedirect("/menu");
        } catch (EntityNotFoundException e) {
            request.getSession().setAttribute("loginError", "Customer ID or Shop ID are not valid");
            response.sendRedirect("/login");
        }
    }
}