package com.company.presentation.servlet;

import com.company.configuration.DependencyManager;
import com.company.core.exceptions.JDBCInitializationException;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServerStartup implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent e) {
        try {
            // strategy = 1 is Hibernate
            // anything else is JDBC
            DependencyManager.setStrategy(1);
            DependencyManager.getInstance();
        } catch (JDBCInitializationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
