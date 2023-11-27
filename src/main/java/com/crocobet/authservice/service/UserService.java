package com.crocobet.authservice.service;

import com.crocobet.authservice.entity.RoleEntity;
import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.exception.HandledException;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.response.UserResponse;
import com.crocobet.authservice.repository.UserRepository;
import com.crocobet.authservice.specs.UserSpecifications;
import com.crocobet.authservice.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserEntity registerUser(UserRegistrationRequest request, RoleEntity role) {
        UserEntity userEntity = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(role))
                .active(true)
                .build();
        return userRepository.save(userEntity);
    }

    public UserEntity findByEmailAndPassword(String email, String password) {
        UserEntity user = userRepository.getByEmail(email);
        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new HandledException(HttpStatus.UNAUTHORIZED, "bad credentials");
        return user;
    }

    public UserResponse getCurrentUser() {
        UserEntity user = getById(SecurityUtils.getAuthenticatedUserId());
        return UserResponse.transform(user);
    }


    public List<UserEntity> getAllByActive(Boolean active) {
        Specification<UserEntity> spec = UserSpecifications.isActive(active);
        return userRepository.findAll(spec);
    }

    public UserResponse update(UserRegistrationRequest request) {
        UserEntity user = getById(SecurityUtils.getAuthenticatedUserId());
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user = userRepository.save(user);
        return UserResponse.transform(user);
    }

    public void deactivate() {
        UserEntity user = getById(SecurityUtils.getAuthenticatedUserId());
        user.setActive(false);
        userRepository.save(user);
    }


    public UserEntity update(Long id, String name, String email, String password, RoleEntity role) {
        UserEntity user = getById(id);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        if (role != null) {
            user.setRoles(new ArrayList<>(Collections.singletonList(role)));
        }
        user = userRepository.save(user);
        return user;
    }

    public UserEntity updateRole(Long id, RoleEntity role) {
        UserEntity user = getById(id);
        user = updateRoles(user, new ArrayList<>(Collections.singletonList(role)));
        return user;
    }

    private UserEntity updateRoles(UserEntity user, List<RoleEntity> roles) {
        user.setRoles(roles);
        return userRepository.save(user);
    }

    public void deactivate(Long id) {
        UserEntity user = getById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    public void activate(Long id) {
        UserEntity user = getById(id);
        user.setActive(true);
        userRepository.save(user);
    }

    public UserEntity getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new HandledException("user not found"));
    }

}