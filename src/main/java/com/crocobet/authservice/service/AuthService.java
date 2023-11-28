package com.crocobet.authservice.service;

import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.model.request.UserLoginRequest;
import com.crocobet.authservice.model.request.UserRegistrationRequest;

public interface AuthService {

    UserEntity register(UserRegistrationRequest request);

    UserEntity login(UserLoginRequest request);

}
