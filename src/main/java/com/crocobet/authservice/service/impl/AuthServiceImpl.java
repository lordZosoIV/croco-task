package com.crocobet.authservice.service.impl;

import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.exception.HandledException;
import com.crocobet.authservice.model.request.UserLoginRequest;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.service.AuthService;
import com.crocobet.authservice.service.RoleService;
import com.crocobet.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final RoleService roleService;

    /**
     * Register a new user based on the provided registration request.
     *
     * @param request The registration request containing user information.
     * @return The registered user entity.
     */
    public UserEntity register(UserRegistrationRequest request) {
        return userService.registerUser(request, roleService.getDefaultRole());
    }

    /**
     * Log in a user based on the provided login request.
     *
     * @param request The login request containing user credentials.
     * @return The logged-in user entity.
     * @throws HandledException if the user is deactivated.
     */
    public UserEntity login(UserLoginRequest request) {
        UserEntity user = userService.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (!user.getActive()) {
            throw new HandledException("User is deactivated");
        }
        return user;
    }

}
