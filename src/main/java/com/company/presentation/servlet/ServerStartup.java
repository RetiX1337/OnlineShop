package com.company.presentation.servlet;

import com.company.configuration.DependencyManager;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ServerStartup implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent e) {
        DependencyManager.getInstance();
    }
}
