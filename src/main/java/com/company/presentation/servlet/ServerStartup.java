package com.company.presentation.servlet;

import com.company.JDBCConnectionPool;
import com.company.configuration.DependencyManager;
import com.company.core.exceptions.JDBCInitializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;

import javax.servlet.*;
import javax.servlet.annotation.WebListener;
import java.util.Set;

@WebListener
public class ServerStartup implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent e) {
        try {
            DependencyManager.getInstance();
        } catch (JDBCInitializationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
