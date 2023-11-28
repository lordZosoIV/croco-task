package com.crocobet.authservice;

import com.crocobet.authservice.entity.RoleEntity;
import com.crocobet.authservice.entity.model.Role;
import com.crocobet.authservice.exception.HandledException;
import com.crocobet.authservice.repository.RoleRepository;
import com.crocobet.authservice.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    RoleEntity userEntity = RoleEntity.builder()
            .id(1L)
            .name(Role.USER)
            .build();
    RoleEntity adminEntity = RoleEntity.builder()
            .id(2L)
            .name(Role.ADMINISTRATOR)
            .build();
    @Mock
    private RoleRepository repository;
    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void getRoleByNameExistingRoleReturnsEntity() {
        when(repository.findByName(any(Role.class))).thenReturn(Optional.of(userEntity));
        RoleEntity result = roleService.getRoleByName(Role.USER);
        assertEquals(userEntity, result);
    }

    @Test
    void getRoleByNameNonExistingRoleThrowsException() {
        when(repository.findByName(any(Role.class))).thenReturn(Optional.empty());
        assertThrows(HandledException.class, () -> roleService.getRoleByName(Role.USER));
    }

    @Test
    void getRoleByNameStringParameterNonExistingRoleThrowsException() {
        when(repository.findByName(any(Role.class))).thenReturn(Optional.empty());
        assertThrows(HandledException.class, () -> roleService.getRoleByName(Role.USER));
    }

    @Test
    void getDefaultRoleReturnsDefaultEntity() {
        when(repository.findByName(any(Role.class))).thenReturn(Optional.of(userEntity));
        RoleEntity result = roleService.getDefaultRole();
        assertEquals(userEntity, result);
    }

    @Test
    void getAdministratorRoleExistingRoleReturnsEntity() {
        when(repository.findByName(any(Role.class))).thenReturn(Optional.of(adminEntity));
        RoleEntity result = roleService.getRoleByName(Role.ADMINISTRATOR);
        assertEquals(adminEntity, result);
    }

    @Test
    void getAdministratorRoleStringParameterExistingRoleReturnsEntity() {
        when(repository.findByName(any(Role.class))).thenReturn(Optional.of(adminEntity));
        RoleEntity result = roleService.getRoleByName(Role.ADMINISTRATOR);
        assertEquals(adminEntity, result);
    }
}
