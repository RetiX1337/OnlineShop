package com.company.core.services.persistenceservices.hibernateimpl;

import com.company.HibernateSessionPool;
import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.models.goods.Item;
import com.company.core.models.goods.Order;
import com.company.core.services.persistenceservices.PersistenceInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import java.util.List;

public class OrderPersistenceServiceHibernate implements PersistenceInterface<Order> {
    private final HibernateSessionPool pool;

    public OrderPersistenceServiceHibernate(HibernateSessionPool pool) {
        this.pool = pool;
    }

    @Override
    public Order save(Order entity) {
        Session session = pool.checkOut();
        Transaction tr = session.beginTransaction();
        try {
            session.persist(entity);
            tr.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tr.rollback();
        } finally {
            pool.checkIn(session);
        }
        return entity;
    }

    @Override
    public Order findById(Long id) {
        Session session = pool.checkOut();
        Order order = session.get(Order.class, id);
        pool.checkIn(session);
        if (order == null) {
            throw new EntityNotFoundException();
        }
        return order;
    }

    @Override
    public List<Order> findAll() {
        Session session = pool.checkOut();
        try {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            JpaCriteriaQuery<Order> criteriaQuery = builder.createQuery(Order.class);
            JpaRoot<Order> root = criteriaQuery.from(Order.class);
            criteriaQuery.select(root);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            pool.checkIn(session);
        }
    }

    @Override
    public Order update(Order entity) {
        Session session = pool.checkOut();
        Transaction tr = session.beginTransaction();
        try {
            Order order = (Order) session.merge(entity);
            tr.commit();
            return order;
        } catch (Exception e) {
            tr.rollback();
            throw new EntityNotFoundException();
        } finally {
            pool.checkIn(session);
        }
    }

    @Override
    public void deleteById(Long id) {
        Session session = pool.checkOut();
        Transaction tr = session.beginTransaction();
        try {
            session.delete(session.load(Order.class, id));
            tr.commit();
        } catch (Exception e) {
            tr.rollback();
        } finally {
            pool.checkIn(session);
        }
    }

    @Override
    public boolean isPresent(Long id) {
        Session session = pool.checkOut();
        boolean isPresent = session.get(Order.class, id) != null;
        pool.checkIn(session);
        return isPresent;
    }
}
