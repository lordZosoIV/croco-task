package com.crocobet.authservice.service.facade;

import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.request.UserUpdateRequest;
import com.crocobet.authservice.model.response.UserInfoResponse;

import java.util.List;

public interface AdminServiceFacade {

    List<UserInfoResponse> getAllActiveUsers(Boolean active);

    UserInfoResponse getById(Long id);

    UserInfoResponse create(UserRegistrationRequest userRegistrationRequest);

    UserInfoResponse update(Long id, UserUpdateRequest userUpdateRequest);

    void deactivate(Long id);
}
