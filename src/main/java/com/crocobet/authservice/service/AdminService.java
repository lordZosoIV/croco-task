package com.crocobet.authservice.service;

import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.request.UserUpdateRequest;
import com.crocobet.authservice.model.response.UserInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserService userService;
    private final RoleService roleService;

    public List<UserInfoResponse> getAllActiveUsers(Boolean active) {
        return UserInfoResponse.transform(userService.getAllByActive(active));
    }

    public UserInfoResponse getById(Long id) {
        return UserInfoResponse.transform(userService.getById(id));
    }

    public UserInfoResponse update(Long id, UserUpdateRequest userUpdateRequest) {
        UserEntity user = userService.update(id, userUpdateRequest.getName(), userUpdateRequest.getEmail(), userUpdateRequest.getPassword(), roleService.getRoleByName(userUpdateRequest.getRole()));
        return UserInfoResponse.transform(user);
    }


    public void deactivate(Long id) {
        userService.deactivate(id);
    }

}