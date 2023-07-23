package com.company.core.services.persistenceservices.hibernateimpl;

import com.company.HibernateSessionPool;
import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.models.Shop;
import com.company.core.services.persistenceservices.PersistenceInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;

import java.util.List;

public class ShopPersistenceServiceHibernate implements PersistenceInterface<Shop> {
    private final HibernateSessionPool pool;

    public ShopPersistenceServiceHibernate(HibernateSessionPool pool) {
        this.pool = pool;
    }

    @Override
    public Shop save(Shop entity) {
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
    public Shop findById(Long id) {
        Session session = pool.checkOut();
        Shop shop = session.get(Shop.class, id);
        pool.checkIn(session);
        if (shop == null) {
            throw new EntityNotFoundException();
        }
        return shop;
    }

    @Override
    public List<Shop> findAll() {
        Session session = pool.checkOut();
        try {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            JpaCriteriaQuery<Shop> criteriaQuery = builder.createQuery(Shop.class);
            JpaRoot<Shop> root = criteriaQuery.from(Shop.class);
            criteriaQuery.select(root);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            pool.checkIn(session);
        }
    }

    @Override
    public Shop update(Shop entity) {
        Session session = pool.checkOut();
        Transaction tr = session.beginTransaction();
        try {
            Shop shop = (Shop) session.merge(entity);
            tr.commit();
            return shop;
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
            session.delete(session.load(Shop.class, id));
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
        boolean isPresent = session.get(Shop.class, id) != null;
        pool.checkIn(session);
        return isPresent;
    }

}
