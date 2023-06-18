package com.company.core.services.persistenceservices;

import com.company.core.models.goods.Identifiable;

import java.util.List;

public interface PersistenceInterface<T extends Identifiable> {
    T save(T entity);
    T findById(Long id);
    List<T> findAll();
    T update(T entity, Long id);
    void deleteById(Long id);
    boolean isPresent(Long id);
}
