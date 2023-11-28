package com.crocobet.authservice;

import com.crocobet.authservice.config.security.UserDetailsImpl;
import com.crocobet.authservice.entity.RoleEntity;
import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.entity.model.Role;
import com.crocobet.authservice.exception.HandledException;
import com.crocobet.authservice.repository.UserRepository;
import com.crocobet.authservice.service.impl.UserDetailsService;
import com.crocobet.authservice.util.TimestampUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

    RoleEntity adminRoleEntity = RoleEntity.builder()
            .id(1L)
            .name(Role.ADMINISTRATOR)
            .build();

    UserEntity user = UserEntity.builder()
            .id(1L)
            .email("mock@example.com")
            .name("Mock User")
            .password("mockPassword")
            .active(true)
            .roles(new ArrayList<>(Collections.singletonList(adminRoleEntity)))
            .createdAt(TimestampUtils.now())
            .createdAt(TimestampUtils.now())
            .build();

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserDetailsService userDetailsService;

    @Test
    void testLoadUserByUsernameUserFoundReturnsUserDetails() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.of(user));
        UserDetails userDetails = userDetailsService.loadUserByUsername(String.valueOf(1L));
        assertEquals(UserDetailsImpl.class, userDetails.getClass());
    }

    @Test
    void testLoadUserByUsernameUserNotFoundThrowsException() {
        when(repository.findById(any(Long.class))).thenReturn(Optional.empty());
        assertThrows(HandledException.class, () -> userDetailsService.loadUserByUsername("1"));
    }

}

