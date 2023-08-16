package com.company.presentation.servlet;

import com.company.core.models.user.UserRole;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Objects;

public class AuthorizationFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        UserRole userRole = (UserRole) httpRequest.getSession().getAttribute("userRole");

        if (!httpRequest.getRequestURI().contains("/c/") && !httpRequest.getRequestURI().contains("/a/")) {
            filterChain.doFilter(servletRequest, servletResponse);
        }

        if (userRole == null) {
            httpResponse.sendRedirect("/choose-role");
            return;
        }

        boolean isCustomerCorrect = httpRequest.getRequestURI().contains("/c/") && userRole.equals(UserRole.CUSTOMER);
        boolean isAdminCorrect = httpRequest.getRequestURI().contains("/a/") && userRole.equals(UserRole.ADMIN);

        if (isCustomerCorrect || isAdminCorrect) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpResponse.sendRedirect("/error-page");
        }
    }
}