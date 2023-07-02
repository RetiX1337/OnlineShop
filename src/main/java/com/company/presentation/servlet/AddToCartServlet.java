package com.company.presentation.servlet;

import com.company.Main;
import com.company.core.controllers.TestController;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "addToCartServlet", value = "/add-cart-servlet")
public class AddToCartServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Long shopId = Long.valueOf(request.getParameter("shopID"));
        Long customerId = Long.valueOf(request.getParameter("customerID"));
        Main.initDependencyManager();
        TestController testController = Main.dependencyManager.getTestController();

        Main.customer = testController.findCustomer(customerId);
        Main.shop = testController.findShop(shopId);

        response.sendRedirect("/menu");
    }

}