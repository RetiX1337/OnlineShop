package com.company.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("com.company.core")
@PropertySource("classpath:database.properties")
public class ApplicationConfig {
}
