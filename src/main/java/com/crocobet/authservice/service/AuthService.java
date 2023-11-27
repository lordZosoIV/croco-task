package com.crocobet.authservice.service;

import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.exception.HandledException;
import com.crocobet.authservice.model.request.UserLoginRequest;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final RoleService roleService;

    public UserEntity register(UserRegistrationRequest request) {
        return userService.registerUser(request, roleService.getDefaultRole());
    }

    public UserEntity login(UserLoginRequest request) {
        UserEntity user = userService.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (!user.getActive()) throw new HandledException("User is deactivated");
        return user;
    }

}
