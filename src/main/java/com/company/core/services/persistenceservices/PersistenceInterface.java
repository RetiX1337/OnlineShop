package com.company.core.services.persistenceservices;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.goods.Identifiable;

import java.util.List;

public interface PersistenceInterface<T extends Identifiable> {
    T save(T entity);
    T findById(Long id);
    List<T> findAll();
    T update(T entity) throws EntityNotFoundException;
    void deleteById(Long id);
    void delete(T entity);
}
