package com.company.core;

import com.company.core.models.goods.Identifiable;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.persistenceservices.PersistenceInterface;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PersistenceServiceBeanFactory {
    private final BeanFactory beanFactory;
    private final Environment environment;

    public PersistenceServiceBeanFactory(@Autowired BeanFactory beanFactory,
                                         @Autowired Environment environment) {
        this.beanFactory = beanFactory;
        this.environment = environment;
    }


    public <T extends Identifiable> PersistenceInterface<T> getPersistenceBean(Class<T> clazz) {
        String persistenceClassName = "";
        persistenceClassName = environment.getProperty(clazz.getSimpleName());
        return (PersistenceInterface<T>) beanFactory.getBean(persistenceClassName);
    }
}
