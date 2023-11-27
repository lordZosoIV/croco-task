package com.crocobet.authservice.service.facade;

import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.response.UserResponse;
import com.crocobet.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceFacade {

    private final UserService userService;

    public UserResponse getCurrentUser() {
        return userService.getCurrentUser();
    }

    public UserResponse update(UserRegistrationRequest request) {
        return userService.update(request);
    }

    public void deactivate() {
        userService.deactivate();
    }
}