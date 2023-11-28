package com.crocobet.authservice.service.facade;

import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.response.UserResponse;

public interface UserServiceFacade {

    UserResponse getCurrentUser();

    UserResponse update(UserRegistrationRequest request);

    void deactivate();
}
