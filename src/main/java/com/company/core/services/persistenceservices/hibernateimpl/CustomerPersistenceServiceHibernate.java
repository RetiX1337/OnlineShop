package com.company.core.services.persistenceservices.hibernateimpl;

import com.company.HibernateSessionPool;
import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.models.user.customer.Customer;
import com.company.core.services.persistenceservices.PersistenceInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import java.util.List;

public class CustomerPersistenceServiceHibernate implements PersistenceInterface<Customer> {
    private final HibernateSessionPool pool;

    public CustomerPersistenceServiceHibernate(HibernateSessionPool pool) {
        this.pool = pool;
    }

    @Override
    public Customer save(Customer entity) {
        Session session = pool.checkOut();
        Transaction tr = session.beginTransaction();
        try {
            Long id = (Long) session.save(entity);
            entity.setId(id);
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
    public Customer findById(Long id) {
        Session session = pool.checkOut();
        Customer customer = session.get(Customer.class, id);
        pool.checkIn(session);
        if (customer == null) {
            throw new EntityNotFoundException();
        }
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        Session session = pool.checkOut();
        try {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            JpaCriteriaQuery<Customer> criteriaQuery = builder.createQuery(Customer.class);
            JpaRoot<Customer> root = criteriaQuery.from(Customer.class);
            criteriaQuery.select(root);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            pool.checkIn(session);
        }
    }

    @Override
    public Customer update(Customer entity) {
        Session session = pool.checkOut();
        Transaction tr = session.beginTransaction();
        try {
            Customer customer = (Customer) session.merge(entity);
            tr.commit();
            return customer;
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
            session.delete(session.load(Customer.class, id));
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
        boolean isPresent = session.get(Customer.class, id) != null;
        pool.checkIn(session);
        return isPresent;
    }
}
