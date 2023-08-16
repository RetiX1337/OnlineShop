package com.company.core.controllers;

import com.company.core.models.UserAuthenticationDTO;
import com.company.core.models.user.User;
import com.company.core.services.logicservices.UserService;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public User findUser(Long userId) {
        return userService.findUser(userId);
    }

    public boolean registerUser(UserAuthenticationDTO userAuthenticationDTO) {
        return userService.registerUser(userAuthenticationDTO);
    }

    public User loginUser(UserAuthenticationDTO userAuthenticationDTO) {
        return userService.loginUser(userAuthenticationDTO);
    }
}
