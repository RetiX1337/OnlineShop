package com.company.core.services.persistenceservices.hibernateimpl;

import com.company.core.pools.HibernateSessionPool;
import com.company.core.exceptions.EntityNotFoundException;
import com.company.core.models.Storage;
import com.company.core.services.persistenceservices.PersistenceInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.criteria.HibernateCriteriaBuilder;
import org.hibernate.query.criteria.JpaCriteriaQuery;
import org.hibernate.query.criteria.JpaRoot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class StoragePersistenceServiceHibernate implements PersistenceInterface<Storage> {
    private final HibernateSessionPool pool;

    public StoragePersistenceServiceHibernate(@Autowired @Qualifier("hibernateSessionPool") HibernateSessionPool pool) {
        this.pool = pool;
    }

    @Override
    public Storage save(Storage entity) {
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
    public Storage findById(Long id) {
        Session session = pool.checkOut();
        Storage storage = session.get(Storage.class, id);
        pool.checkIn(session);
        if (storage == null) {
            throw new EntityNotFoundException();
        }
        return storage;
    }

    @Override
    public List<Storage> findAll() {
        Session session = pool.checkOut();
        try {
            HibernateCriteriaBuilder builder = session.getCriteriaBuilder();
            JpaCriteriaQuery<Storage> criteriaQuery = builder.createQuery(Storage.class);
            JpaRoot<Storage> root = criteriaQuery.from(Storage.class);
            criteriaQuery.select(root);
            List<Storage> storages = session.createQuery(criteriaQuery).getResultList();
            return session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            return null;
        } finally {
            pool.checkIn(session);
        }
    }

    @Override
    public Storage update(Storage entity) {
        Session session = pool.checkOut();
        Transaction tr = session.beginTransaction();
        try {
            Storage storage = (Storage) session.merge(entity);
            tr.commit();
            return storage;
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
            session.delete(session.load(Storage.class, id));
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
        boolean isPresent = session.get(Storage.class, id) != null;
        pool.checkIn(session);
        return isPresent;
    }

}
