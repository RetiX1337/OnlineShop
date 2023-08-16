package com.company.presentation.servlet;


import com.company.configuration.DependencyManager;
import com.company.core.controllers.CartController;
import com.company.core.models.user.customer.Cart;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "update-quantity-servlet", value = "/c/update-quantity")
public class UpdateQuantityServlet extends HttpServlet {
    private CartController cartController;

    @Override
    public void init() {
        this.cartController = DependencyManager.getInstance().getCartController();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String action = request.getParameter("action");

        Cart cart = (Cart) request.getSession().getAttribute("cart");

        Long productId = Long.valueOf(request.getParameter("productId"));
        Long shopId = Long.valueOf(request.getSession().getAttribute("shopId").toString());

        if (action.equals("add")) {
            if (!cartController.addConstQuantityToCart(cart, shopId, productId)) {
                request.getSession().setAttribute("browseProductsQuantityModifyError", "You can't add!");
            }
        } else if (action.equals("delete")) {
            if (!cartController.deleteConstQuantityFromCart(cart, productId)) {
                request.getSession().setAttribute("browseProductsQuantityModifyError", "You can't delete!");
            }
        }

        response.sendRedirect("/browse-products");
    }
}
