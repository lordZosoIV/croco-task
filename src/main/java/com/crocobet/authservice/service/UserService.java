package com.crocobet.authservice.service;

import com.crocobet.authservice.entity.RoleEntity;
import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import jakarta.transaction.Transactional;

import java.util.List;

public interface UserService {

    UserEntity registerUser(UserRegistrationRequest request, RoleEntity role);

    UserEntity findByEmailAndPassword(String email, String password);

    UserEntity getCurrentUser();


    List<UserEntity> getAllByActive(Boolean active);

    UserEntity update(UserRegistrationRequest request, Long id);

    @Transactional
    void deactivate();

    UserEntity update(Long id, String name, String email, String password, RoleEntity role);

    UserEntity updateRole(Long id, RoleEntity role);

    void deactivate(Long id);

    void activate(Long id);

    UserEntity getById(Long id);

}
