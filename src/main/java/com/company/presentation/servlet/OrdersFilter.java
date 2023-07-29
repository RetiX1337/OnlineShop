package com.company.presentation.servlet;

import com.company.configuration.DependencyManager;
import com.company.core.exceptions.IncorrectURLArgumentException;
import com.company.core.models.goods.Order;
import com.company.core.models.user.customer.Customer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OrdersFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;

        if (httpRequest.getRequestURI().equals("/orders")) {
            filterChain.doFilter(servletRequest, servletResponse);
        }

        Long orderId;

        try {
            orderId = Long.valueOf(httpRequest.getPathInfo().substring(1));
        } catch (NumberFormatException e) {
            throw new IncorrectURLArgumentException();
        }

        Order order = DependencyManager.getInstance().getOrderController().findOrderById(orderId);

        Customer customer = (Customer) httpRequest.getSession().getAttribute("customer");

        if (order.getCustomer().getId().equals(customer.getId())) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            throw new IncorrectURLArgumentException();
        }
    }

    @Override
    public void destroy() {
    }
}
