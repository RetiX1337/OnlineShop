package com.company.core.services.logicservices.impl;

import com.company.core.exceptions.AuthenticationException;
import com.company.core.models.UserAuthenticationDTO;
import com.company.core.models.user.User;
import com.company.core.models.user.UserRole;
import com.company.core.services.logicservices.UserService;
import com.company.core.services.persistenceservices.PersistenceInterface;

public class UserServiceImpl implements UserService {
    private final PersistenceInterface<User> userPersistenceService;

    public UserServiceImpl(PersistenceInterface<User> userPersistenceService) {
        this.userPersistenceService = userPersistenceService;
    }

    @Override
    public boolean registerUser(UserAuthenticationDTO userAuthenticationDTO) {
        if (findByEmail(userAuthenticationDTO.getEmail()) == null) {
            return false;
        }
        User user = new User();
        user.setEmail(userAuthenticationDTO.getEmail());
        user.setPasswordHash(userAuthenticationDTO.getPasswordHash());
        user.setUsername(userAuthenticationDTO.getUsername());
        user.addRole(UserRole.CUSTOMER);
        userPersistenceService.save(user);
        return true;
    }

    @Override
    public User loginUser(UserAuthenticationDTO userAuthenticationDTO) {
        User user = findByEmail(userAuthenticationDTO.getEmail());
        if (user == null || !(user.getPasswordHash().equals(userAuthenticationDTO.getPasswordHash()))) {
            throw new AuthenticationException();
        }
        return user;
    }

    @Override
    public User createUser(String name, String password, String email) {
        return null;
    }

    @Override
    public User addUser(User user) {
        return null;
    }

    @Override
    public User updateUser(User user, Long id) {
        return null;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public User findUser(Long id) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return userPersistenceService.findAll()
                .stream()
                .filter(retrievedUser -> retrievedUser.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean isPresent(Long userId) {
        return false;
    }

}
