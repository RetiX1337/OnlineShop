package com.company;

import com.company.core.exceptions.HibernateInitializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSessionPool extends ObjectPool<Session> {
    private SessionFactory sessionFactory;
    private final static Logger logger = LogManager.getLogger(HibernateSessionPool.class);

    public HibernateSessionPool() {
        setUp();
    }

    private void setUp() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure()
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
            logger.error(e);
            throw new HibernateInitializationException();
        }
    }

    @Override
    protected Session create() {
        try {
            return sessionFactory.openSession();
        } catch (HibernateException e) {
            logger.error(e);
            throw new HibernateInitializationException();
        }
    }

    @Override
    public boolean validate(Session o) {
        try {
            return !sessionFactory.isClosed();
        } catch (HibernateException e) {
            logger.error(e);
            throw new HibernateInitializationException();
        }
    }

    @Override
    public void expire(Session o) {
        try {
            sessionFactory.close();
        } catch (HibernateException e) {
            logger.error(e);
            throw new HibernateInitializationException();
        }
    }
}
