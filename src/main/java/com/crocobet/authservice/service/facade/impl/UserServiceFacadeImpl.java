package com.crocobet.authservice.service.facade.impl;

import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.response.UserResponse;
import com.crocobet.authservice.service.UserService;
import com.crocobet.authservice.service.facade.UserServiceFacade;
import com.crocobet.authservice.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceFacadeImpl implements UserServiceFacade {

    private final UserService userService;

    /**
     * Get the current user's information.
     *
     * @return UserResponse representing the current user.
     */
    @Override
    public UserResponse getCurrentUser() {
        return UserResponse.transform(userService.getCurrentUser());
    }

    /**
     * Update the current user's information based on the provided registration request.
     *
     * @param request The registration request containing updated user information.
     * @return UserResponse representing the updated user.
     */
    @Override
    public UserResponse update(UserRegistrationRequest request) {
        return UserResponse.transform(userService.update(request, SecurityUtils.getAuthenticatedUserId()));
    }

    /**
     * Deactivate the current user.
     */
    @Override
    public void deactivate() {
        userService.deactivate();
    }

}
