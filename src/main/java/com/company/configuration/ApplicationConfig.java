package com.company.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

@Configuration
@ComponentScan("com.company.core")
@PropertySource("classpath:database.properties")
@PropertySource("classpath:persistenceservicestrategy.properties")
public class ApplicationConfig {

    @Autowired
    private Environment environment;

    public String getValue() {
        return environment.getProperty("persistence.customer.bean");
    }
}
