package com.company.presentation.servlet;

import com.company.configuration.DependencyManager;
import com.company.core.controllers.CartController;
import com.company.core.controllers.UserController;
import com.company.core.models.user.User;
import com.company.core.models.user.UserRole;
import com.company.core.models.user.customer.Cart;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "authorization-servlet", value = "/authorization")
public class AuthorizationServlet extends HttpServlet {
    private CartController cartController;

    @Override
    public void init() {
        this.cartController = DependencyManager.getInstance().getCartController();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserRole userRole = UserRole.valueOf(request.getParameter("userRole"));
        User user = (User) request.getSession().getAttribute("user");
        if (!user.getRoles().contains(userRole)) {
            return;
        }

        request.getSession().setAttribute("userRole", userRole);
        if (userRole.equals(UserRole.CUSTOMER)) {
            Cart cart = cartController.createUserCart(user);
            request.getSession().setAttribute("cart", cart);
        }
        response.sendRedirect("/menu");
    }
}
