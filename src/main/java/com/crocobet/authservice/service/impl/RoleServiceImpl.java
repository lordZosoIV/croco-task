package com.crocobet.authservice.service.impl;

import com.crocobet.authservice.entity.RoleEntity;
import com.crocobet.authservice.entity.model.Role;
import com.crocobet.authservice.exception.HandledException;
import com.crocobet.authservice.repository.RoleRepository;
import com.crocobet.authservice.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;

    /**
     * Get a role entity by its name.
     *
     * @param name The name of the role.
     * @return The role entity.
     * @throws HandledException if the role is not found.
     */
    @Override
    @Cacheable(key = "#name.name()", value = "role")
    public RoleEntity getRoleByName(Role name) {
        return repository.findByName(name)
                .orElseThrow(() -> new HandledException("Role not found"));
    }

    /**
     * Get a role entity by its name.
     *
     * @param name The name of the role as a string.
     * @return The role entity.
     * @throws HandledException if the role is not found.
     */
    @Override
    public RoleEntity getRoleByName(String name) {
        return getRoleByName(Role.valueOf(name));
    }

    /**
     * Get the default role (e.g., ROLE_USER).
     *
     * @return The default role entity.
     */
    @Override
    public RoleEntity getDefaultRole() {
        return getRoleByName(Role.USER);
    }

}
