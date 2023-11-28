package com.crocobet.authservice.service.facade.impl;

import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.request.UserUpdateRequest;
import com.crocobet.authservice.model.response.UserInfoResponse;
import com.crocobet.authservice.service.RoleService;
import com.crocobet.authservice.service.UserService;
import com.crocobet.authservice.service.facade.AdminServiceFacade;
import com.crocobet.authservice.service.facade.AuthServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminServiceFacadeImpl implements AdminServiceFacade {

    private final AuthServiceFacade authServiceFacade;

    private final UserService userService;
    private final RoleService roleService;

    /**
     * Get all active users based on the specified active status.
     *
     * @param active The active status of users to filter.
     * @return List of active users.
     */
    @Override
    public List<UserInfoResponse> getAllActiveUsers(Boolean active) {
        return UserInfoResponse.transform(userService.getAllByActive(active));
    }

    /**
     * Get user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user entity with the specified ID.
     */
    @Override
    public UserInfoResponse getById(Long id) {
        return UserInfoResponse.transform(userService.getById(id));
    }

    /**
     * Update user information based on the provided user update request.
     *
     * @param id                The ID of the user to update.
     * @param userUpdateRequest The request containing updated user information.
     * @return The updated user entity.
     */
    @Override
    public UserInfoResponse update(Long id, UserUpdateRequest userUpdateRequest) {
        return UserInfoResponse.transform(userService.update(id, userUpdateRequest.getName(), userUpdateRequest.getEmail(), userUpdateRequest.getPassword(), roleService.getRoleByName(userUpdateRequest.getRole())));
    }


    /**
     * Deactivate a user with the specified ID.
     *
     * @param id The ID of the user to deactivate.
     */
    @Override
    public void deactivate(Long id) {
        userService.deactivate(id);
    }

    /**
     * Create a new user based on the provided registration request.
     *
     * @param userRegistrationRequest The registration request containing user information.
     * @return UserInfoResponse representing the newly created user.
     */
    @Override
    public UserInfoResponse create(UserRegistrationRequest userRegistrationRequest) {
        return authServiceFacade.register(userRegistrationRequest);
    }

}
