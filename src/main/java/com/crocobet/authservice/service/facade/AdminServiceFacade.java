package com.crocobet.authservice.service.facade;

import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.request.UserUpdateRequest;
import com.crocobet.authservice.model.response.UserAuthResponse;
import com.crocobet.authservice.model.response.UserInfoResponse;
import com.crocobet.authservice.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
@RequiredArgsConstructor
public class AdminServiceFacade {

    private final AdminService adminService;
    private final AuthServiceFacade authServiceFacade;

    public List<UserInfoResponse> getAllActiveUsers(Boolean active) {
        return adminService.getAllActiveUsers(active);
    }

    public UserInfoResponse getById(Long id) {
        return adminService.getById(id);
    }

    public UserInfoResponse create(UserRegistrationRequest userRegistrationRequest) {
        return authServiceFacade.register(userRegistrationRequest);
    }

    public UserInfoResponse update(Long id, UserUpdateRequest userUpdateRequest) {
        return adminService.update(id, userUpdateRequest);
    }

    public void deactivate(Long id) {
        adminService.deactivate(id);
    }

}