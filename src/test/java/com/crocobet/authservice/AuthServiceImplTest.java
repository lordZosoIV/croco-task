package com.crocobet.authservice;

import com.crocobet.authservice.entity.RoleEntity;
import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.entity.model.Role;
import com.crocobet.authservice.exception.HandledException;
import com.crocobet.authservice.model.request.UserLoginRequest;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.service.RoleService;
import com.crocobet.authservice.service.UserService;
import com.crocobet.authservice.service.impl.AuthServiceImpl;
import com.crocobet.authservice.util.TimestampUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    RoleEntity adminRoleEntity = RoleEntity.builder()
            .id(1L)
            .name(Role.ADMINISTRATOR)
            .build();
    RoleEntity userRoleEntity = RoleEntity.builder()
            .id(2L)
            .name(Role.USER)
            .build();
    UserEntity user = UserEntity.builder()
            .id(1L)
            .email("mock@example.com")
            .name("Mock User")
            .password("$2a$10$f9AwetKGg1AdsEFz9gkoFu2YaZCHBBat7AM3.sYQbPFbg.xZrP6X2")
            .active(true)
            .roles(new ArrayList<>(Collections.singletonList(userRoleEntity)))
            .createdAt(TimestampUtils.now())
            .createdAt(TimestampUtils.now())
            .build();
    UserEntity admin = UserEntity.builder()
            .id(2L)
            .email("mock2@example.com")
            .name("Mock 2User")
            .password("$2a$10$f9AwetKGg1AdsEFz9gkoFu2YaZCHBBat7AM3.sYQbPFbg.xZrP6X2")
            .active(true)
            .roles(new ArrayList<>(Collections.singletonList(adminRoleEntity)))
            .createdAt(TimestampUtils.now())
            .createdAt(TimestampUtils.now())
            .build();
    @Mock
    private UserService userService;
    @Mock
    private RoleService roleService;
    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void registerUser() {
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest();
        when(roleService.getDefaultRole()).thenReturn(userRoleEntity);
        when(userService.registerUser(any(UserRegistrationRequest.class), any(RoleEntity.class)))
                .thenReturn(user);

        UserEntity result = authService.register(registrationRequest);

        Assertions.assertEquals(user.getName(), result.getName());
        Assertions.assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void loginSuccess() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword(user.getPassword());
        user.setActive(true);

        when(userService.findByEmailAndPassword(anyString(), anyString())).thenReturn(user);

        UserEntity result = authService.login(loginRequest);

        Assertions.assertEquals(user.getName(), result.getName());
        Assertions.assertEquals(user.getEmail(), result.getEmail());
    }

    @Test
    void loginDeactivatedUserThrowsHandledException() {
        UserLoginRequest loginRequest = new UserLoginRequest();
        loginRequest.setEmail(user.getEmail());
        loginRequest.setPassword(user.getPassword());
        user.setActive(false);

        when(userService.findByEmailAndPassword(anyString(), anyString())).thenReturn(user);

        assertThrows(HandledException.class, () -> authService.login(loginRequest));

        verify(userService, times(1)).findByEmailAndPassword(anyString(), anyString());
    }
}
