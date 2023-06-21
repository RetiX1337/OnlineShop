package com.company.core.services.logicservices.impl;

import com.company.core.models.EntityNotFoundException;
import com.company.core.models.Storage;
import com.company.core.services.logicservices.StorageService;
import com.company.core.services.persistenceservices.PersistenceInterface;

import java.util.List;

public class StorageServiceImpl implements StorageService {
    private final PersistenceInterface<Storage> storagePersistenceService;

    public StorageServiceImpl(PersistenceInterface<Storage> storagePersistenceService) {
        this.storagePersistenceService = storagePersistenceService;
    }

    @Override
    public Storage createStorage(String name, String address) {
        return new Storage(name, address);
    }

    @Override
    public void addStorage(Storage storage) {
        storagePersistenceService.save(storage);
    }

    @Override
    public Storage getStorage(Long id) throws EntityNotFoundException {
        if (storagePersistenceService.isPresent(id)) {
            return storagePersistenceService.findById(id);
        } else {
            throw new EntityNotFoundException();
        }
    }

    @Override
    public List<Storage> getAllStorages() {
        return storagePersistenceService.findAll();
    }
}
