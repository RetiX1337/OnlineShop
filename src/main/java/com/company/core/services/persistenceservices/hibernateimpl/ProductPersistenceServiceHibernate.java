package com.company.core.services.persistenceservices.hibernateimpl;

import com.company.core.models.goods.ProductBase;
import com.company.core.services.persistenceservices.PersistenceInterface;
import com.company.HibernateSessionPool;
import com.company.core.exceptions.EntityNotFoundException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import java.util.List;

public class ProductPersistenceServiceHibernate implements PersistenceInterface<ProductBase> {
    private final HibernateSessionPool pool;

    public ProductPersistenceServiceHibernate(HibernateSessionPool pool) {
        this.pool = pool;
    }

    @Override
    public ProductBase save(ProductBase entity) {
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
    public ProductBase findById(Long id) {
        Session session = pool.checkOut();
        ProductBase product = session.get(ProductBase.class, id);
        pool.checkIn(session);
        if (product == null) {
            throw new EntityNotFoundException();
        }
        return product;
    }

    @Override
    public List<ProductBase> findAll() {
        Session session = pool.checkOut();
        try {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            JpaCriteriaQuery<ProductBase> criteriaQuery = builder.createQuery(ProductBase.class);
            JpaRoot<ProductBase> root = criteriaQuery.from(ProductBase.class);
            criteriaQuery.select(root);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            pool.checkIn(session);
        }
    }

    @Override
    public ProductBase update(ProductBase entity) {
        Session session = pool.checkOut();
        Transaction tr = session.beginTransaction();
        try {
            ProductBase product = (ProductBase) session.merge(entity);
            tr.commit();
            return product;
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
            session.delete(session.load(ProductBase.class, id));
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
        boolean isPresent = session.get(ProductBase.class, id) != null;
        pool.checkIn(session);
        return isPresent;
    }
}