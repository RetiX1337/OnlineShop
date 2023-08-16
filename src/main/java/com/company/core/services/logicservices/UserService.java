package com.company.core.services.logicservices;

import com.company.core.models.UserAuthenticationDTO;
import com.company.core.models.user.User;

public interface UserService {
    boolean registerUser(UserAuthenticationDTO userAuthenticationDTO);
    User loginUser(UserAuthenticationDTO userAuthenticationDTO);
    User createUser(String name, String password, String email);
    User addUser(User user);
    User updateUser(User user, Long id);
    void deleteById(Long id);
    User findUser(Long id);

    User findByEmail(String email);

    boolean isPresent(Long userId);
}
