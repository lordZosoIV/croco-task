package com.crocobet.authservice;

import com.crocobet.authservice.entity.RoleEntity;
import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.entity.model.Role;
import com.crocobet.authservice.exception.HandledException;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.repository.UserRepository;
import com.crocobet.authservice.service.impl.UserServiceImpl;
import com.crocobet.authservice.util.TimestampUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

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
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testRegisterUser() {
        UserRegistrationRequest registrationRequest = new UserRegistrationRequest("mock@example.com", "Mock User", "$2a$10$f9AwetKGg1AdsEFz9gkoFu2YaZCHBBat7AM3.sYQbPFbg.xZrP6X2");

        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$f9AwetKGg1AdsEFz9gkoFu2YaZCHBBat7AM3.sYQbPFbg.xZrP6X2");
        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserEntity result = userService.registerUser(registrationRequest, userRoleEntity);

        assertNotNull(result);
        assertEquals("Mock User", result.getName());
        assertEquals("mock@example.com", result.getEmail());
        assertEquals("$2a$10$f9AwetKGg1AdsEFz9gkoFu2YaZCHBBat7AM3.sYQbPFbg.xZrP6X2", result.getPassword());
        assertEquals(Collections.singletonList(userRoleEntity), result.getRoles());
        assertTrue(result.getActive());
    }

    @Test
    void testFindByEmailAndPassword_UserFound_ReturnsUser() {
        UserEntity userEntity = new UserEntity();
        userEntity.setPassword("encodedPassword");

        when(userRepository.getByEmail(anyString())).thenReturn(java.util.Optional.of(userEntity));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);

        UserEntity result = userService.findByEmailAndPassword("john@example.com", "password");

        assertNotNull(result);
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    void testFindByEmailAndPassword_UserNotFound_ThrowsException() {
        when(userRepository.getByEmail(anyString())).thenReturn(java.util.Optional.empty());
        assertThrows(HandledException.class, () -> userService.findByEmailAndPassword("john@example.com", "password"));
    }


    @Test
    void testGetAllByActive() {
        when(userRepository.findAll(any(Specification.class))).thenReturn(Collections.singletonList(user));

        List<UserEntity> activeUsers = userService.getAllByActive(true);

        assertNotNull(activeUsers);
        assertEquals(1, activeUsers.size());
        assertEquals("Mock User", activeUsers.get(0).getName());
        assertEquals("mock@example.com", activeUsers.get(0).getEmail());
    }


    @Test
    void testUpdate() {

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(userRepository.save(any(UserEntity.class))).thenReturn(user);

        UserRegistrationRequest updatedRequest = new UserRegistrationRequest(user.getEmail(),
                user.getName(), "newPassword");

        UserEntity result = userService.update(updatedRequest, 1L);

        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getEmail(), result.getEmail());
        assertNotEquals("newPassword", result.getPassword());
    }


}
