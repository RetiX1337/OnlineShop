package com.company.presentation.servlet;

import com.company.configuration.DependencyManager;
import com.company.core.controllers.UserController;
import com.company.core.exceptions.AuthenticationException;
import com.company.core.models.UserAuthenticationDTO;
import com.company.core.models.user.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebServlet(name = "authentication-servlet", value = "/authentication")
public class AuthenticationServlet extends HttpServlet {
    private UserController userController;

    @Override
    public void init() {
        this.userController = DependencyManager.getInstance().getUserController();
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String parameterEmail = request.getParameter("email");
        String parameterPassword = request.getParameter("password");
        UserAuthenticationDTO userAuthenticationDTO = new UserAuthenticationDTO(parameterEmail, parameterPassword);
        Long parameterShopId = 1L;
        try {
            User user = userController.loginUser(userAuthenticationDTO);
            request.getSession().setAttribute("user", user);

            //TODO move shop definition to another servlet
            Long shopId = DependencyManager.getInstance().getShopController().findShop(parameterShopId).getId();
            request.getSession().setAttribute("shopId", shopId);

            response.sendRedirect("/choose-role");
        } catch (AuthenticationException e) {
            request.getSession().setAttribute("loginError", "Email or password are incorrect");
            response.sendRedirect("/login");
        }
    }
}