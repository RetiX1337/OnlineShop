package com.company;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

public class HibernateSessionPool extends ObjectPool<Session> {
    private SessionFactory sessionFactory;

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
            e.printStackTrace();
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Override
    protected Session create() {
        return sessionFactory.openSession();
    }

    @Override
    public boolean validate(Session o) {
        return !sessionFactory.isClosed();
    }

    @Override
    public void expire(Session o) {
        sessionFactory.close();
    }
}
