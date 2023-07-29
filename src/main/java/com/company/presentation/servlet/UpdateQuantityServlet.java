package com.company.presentation.servlet;


import com.company.configuration.DependencyManager;
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

        Customer customer = (Customer) request.getSession().getAttribute("customer");

        Long productId = Long.valueOf(request.getParameter("productId"));
        Long shopId = Long.valueOf(request.getSession().getAttribute("shopId").toString());

        if (action.equals("add")) {
            if (!DependencyManager.getInstance().getCartController().addToCart(customer, shopId, productId)) {
                request.getSession().setAttribute("browseProductsQuantityModifyError", "You can't add!");
            }
        } else if (action.equals("delete")) {
            if (!DependencyManager.getInstance().getCartController().deleteFromCart(customer, productId)) {
                request.getSession().setAttribute("browseProductsQuantityModifyError", "You can't delete!");
            }
        }

        response.sendRedirect("/browse-products");
    }
}
