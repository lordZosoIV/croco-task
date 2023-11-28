package com.crocobet.authservice.service.facade;

import com.crocobet.authservice.model.request.UserLoginRequest;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.response.UserAuthResponse;
import com.crocobet.authservice.model.response.UserInfoResponse;

public interface AuthServiceFacade {

    UserAuthResponse login(UserLoginRequest request);

    UserInfoResponse register(UserRegistrationRequest request);
}
