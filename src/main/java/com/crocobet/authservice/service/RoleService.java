package com.crocobet.authservice.service;

import com.crocobet.authservice.entity.RoleEntity;
import com.crocobet.authservice.entity.model.Role;

public interface RoleService {


    RoleEntity getRoleByName(Role name);

    RoleEntity getRoleByName(String name);

    RoleEntity getDefaultRole();

}
