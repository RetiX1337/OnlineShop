package com.company.presentation.servlet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;


public class AuthenticationFilter implements Filter {
    private static final String[] loginRequiredURLs = {
            "/orders", "/update-quantity", "/cart"
    };

    private HttpServletRequest httpRequest;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {
        httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String path = httpRequest.getRequestURI();

        HttpSession session = httpRequest.getSession();

        boolean isLoggedIn = (session != null && session.getAttribute("customer") != null);

        boolean isLoginPage = path.contains("/login");

        boolean isLoginRequest = path.contains("/login-processing");

        if (isLoggedIn && (isLoginPage || isLoginRequest)) {
            httpResponse.sendRedirect("/menu");
        } else if (!isLoggedIn && isLoginRequired()) {
            httpResponse.sendRedirect("/login");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }

    }

    private boolean isLoginRequired() {
        String path = httpRequest.getRequestURI();
        for (String URL : loginRequiredURLs) {
            if (path.contains(URL)) {
                return true;
            }
        }
        return false;
    }

}
