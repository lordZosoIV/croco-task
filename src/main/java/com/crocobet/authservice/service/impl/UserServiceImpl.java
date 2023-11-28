package com.crocobet.authservice.service.impl;

import com.crocobet.authservice.entity.RoleEntity;
import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.exception.HandledException;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.repository.UserRepository;
import com.crocobet.authservice.service.UserService;
import com.crocobet.authservice.specs.UserSpecifications;
import com.crocobet.authservice.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Register a new user based on the provided registration request and role.
     *
     * @param request The registration request containing user information.
     * @param role    The role to assign to the new user.
     * @return The registered user entity.
     */
    @Transactional
    @CachePut(key = "#request.email", value = "user")
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

    /**
     * Find a user by email and password, checking if the provided password matches the stored hash.
     *
     * @param email    The email of the user.
     * @param password The password of the user.
     * @return The user entity if found and the password matches.
     * @throws HandledException with HttpStatus.UNAUTHORIZED if the credentials are invalid.
     */
    public UserEntity findByEmailAndPassword(String email, String password) {
        UserEntity user = userRepository.getByEmail(email).orElseThrow(() -> new HandledException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new HandledException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
        return user;
    }

    /**
     * Get the currently authenticated user.
     *
     * @return UserEntity object representing the current user.
     */
    public UserEntity getCurrentUser() {
        return getById(SecurityUtils.getAuthenticatedUserId());
    }

    /**
     * Get all users based on the specified active status.
     *
     * @param active The active status of users to filter.
     * @return List of users matching the criteria.
     */
    public List<UserEntity> getAllByActive(Boolean active) {
        Specification<UserEntity> spec = UserSpecifications.isActive(active);
        return userRepository.findAll(spec);
    }

    /**
     * Update the currently authenticated user based on the provided registration request.
     *
     * @param request The registration request containing updated user information.
     * @return UserEntity object representing the updated user.
     */
    @Transactional
    @CachePut(key = "#request.email", value = "user")
    public UserEntity update(UserRegistrationRequest request, Long id) {
        UserEntity user = getById(id);
        if (!user.getActive()) {
            throw new HandledException(String.format("User with email '%s' is deleted", user.getEmail()));
        }
        user.setName(request.getName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user = userRepository.save(user);
        return user;
    }

    /**
     * Deactivate the currently authenticated user.
     */
    @Transactional
    public void deactivate() {
        UserEntity user = getById(SecurityUtils.getAuthenticatedUserId());
        user.setActive(false);
        userRepository.save(user);
    }

    /**
     * Update a user based on the provided information.
     *
     * @param id       The ID of the user to update.
     * @param name     The new name of the user.
     * @param email    The new email of the user.
     * @param password The new password of the user.
     * @param role     The new role of the user.
     * @return The updated user entity.
     */
    @Transactional
    public UserEntity update(Long id, String name, String email, String password, RoleEntity role) {
        UserEntity user = getById(id);
        if (!user.getActive()) {
            throw new HandledException(String.format("User with email '%s' is deleted", email));
        }
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        if (role != null) {
            user.setRoles(new ArrayList<>(Collections.singletonList(role)));
        }
        user = userRepository.save(user);
        return user;
    }

    /**
     * Update the role of a user.
     *
     * @param id   The ID of the user to update.
     * @param role The new role of the user.
     * @return The updated user entity.
     */
    @Transactional
    public UserEntity updateRole(Long id, RoleEntity role) {
        UserEntity user = getById(id);
        user = updateRoles(user, new ArrayList<>(Collections.singletonList(role)));
        return user;
    }

    /**
     * Deactivate a user based on the specified ID.
     *
     * @param id The ID of the user to deactivate.
     */
    @Transactional
    @CacheEvict(key = "#id", value = "user")
    public void deactivate(Long id) {
        UserEntity user = getById(id);
        user.setActive(false);
        userRepository.save(user);
    }

    /**
     * Activate a user based on the specified ID.
     *
     * @param id The ID of the user to activate.
     */
    @Transactional
    public void activate(Long id) {
        UserEntity user = getById(id);
        user.setActive(true);
        userRepository.save(user);
    }

    /**
     * Get a user by ID.
     *
     * @param id The ID of the user to retrieve.
     * @return The user entity with the specified ID.
     */
    @Transactional
    @Cacheable(value = "user", key = "#id")
    public UserEntity getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new HandledException("User not found"));
    }


    private UserEntity updateRoles(UserEntity user, List<RoleEntity> roles) {
        user.setRoles(roles);
        return userRepository.save(user);
    }

}
