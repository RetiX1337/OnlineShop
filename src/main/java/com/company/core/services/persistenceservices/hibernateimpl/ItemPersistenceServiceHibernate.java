package com.company.core.services.persistenceservices.hibernateimpl;

import com.company.core.pools.HibernateSessionPool;
import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.models.goods.Item;
import com.company.core.services.persistenceservices.PersistenceInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class ItemPersistenceServiceHibernate implements PersistenceInterface<Item> {
    private final HibernateSessionPool pool;

    public ItemPersistenceServiceHibernate(HibernateSessionPool pool) {
        this.pool = pool;
    }

    @Override
    public Item save(Item entity) {
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
    public Item findById(Long id) {
        Session session = pool.checkOut();
        Item item = session.get(Item.class, id);
        pool.checkIn(session);
        if (item == null) {
            throw new EntityNotFoundException();
        }
        return item;
    }

    @Override
    public List<Item> findAll() {
        Session session = pool.checkOut();
        try {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            JpaCriteriaQuery<Item> criteriaQuery = builder.createQuery(Item.class);
            JpaRoot<Item> root = criteriaQuery.from(Item.class);
            criteriaQuery.select(root);
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            pool.checkIn(session);
        }
    }

    @Override
    public Item update(Item entity) {
        Session session = pool.checkOut();
        Transaction tr = session.beginTransaction();
        try {
            Item item = (Item) session.merge(entity);
            tr.commit();
            return item;
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
            session.delete(session.load(Item.class, id));
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
        boolean isPresent = session.get(Item.class, id) != null;
        pool.checkIn(session);
        return isPresent;
    }
}
