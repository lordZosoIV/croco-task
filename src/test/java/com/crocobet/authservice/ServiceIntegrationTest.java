package com.crocobet.authservice;


import com.crocobet.authservice.config.HazelCastConfig;
import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.exception.HandledException;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.request.UserUpdateRequest;
import com.crocobet.authservice.model.response.UserInfoResponse;
import com.crocobet.authservice.service.facade.AdminServiceFacade;
import com.hazelcast.core.HazelcastInstance;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Import(HazelCastConfig.class)
public class ServiceIntegrationTest {

    private static final String HAZELCAST_MAP = "user";
    private static final UserRegistrationRequest USER_REQUEST = getRandomUserRequest();

    private static final UserRegistrationRequest USER_REQUEST2 = getRandomUserRequest();

    @Autowired
    private AdminServiceFacade adminServiceFacade;
    @Autowired
    private HazelcastInstance hazelcastInstance;

    @AfterEach
    public void afterEach() {
        if (hazelcastInstance != null) {
            hazelcastInstance.getMap(HAZELCAST_MAP).clear();
        }
    }

    private static UserRegistrationRequest getRandomUserRequest() {
        return UserRegistrationRequest.builder()
                .email(RandomUserGenerator.generateRandomEmail())
                .password(RandomUserGenerator.generateRandomPassword())
                .name(RandomUserGenerator.generateRandomName())
                .build();
    }

    @Test
    public void testRegisterUser() {
        UserInfoResponse result = adminServiceFacade.create(USER_REQUEST);

        assertEquals(USER_REQUEST.getName(), result.getName());
        assertEquals(USER_REQUEST.getEmail(), result.getEmail());
    }

    @Test
    public void testDeleteUser() {
        UserInfoResponse userResponse = adminServiceFacade.create(USER_REQUEST2);
        adminServiceFacade.deactivate(userResponse.getId());
        assertEquals(USER_REQUEST2.getName(), userResponse.getName());
        assertEquals(adminServiceFacade.getById(userResponse.getId()).getActive(), false);
    }

    @Test
    public void testDeleteNotFoundUser() {
        assertThrows(HandledException.class, () -> {
            adminServiceFacade.deactivate(-100L);
        });
    }

    @Test
    public void testDuplicatedUserCreation() {
        UserRegistrationRequest u = getRandomUserRequest();
        adminServiceFacade.create(u);
        assertThrows(DataIntegrityViolationException.class, () -> {
            adminServiceFacade.create(u);
        });
    }

    @Test
    public void testUpdateUserWithDuplicatedName() {
        UserRegistrationRequest u = getRandomUserRequest();

        adminServiceFacade.create(u);

        UserRegistrationRequest u2 = getRandomUserRequest();

        UserInfoResponse r = adminServiceFacade.create(u2);

        UserUpdateRequest u3 = new UserUpdateRequest();
        u3.setEmail(u.getEmail());
        u3.setName(u2.getName());
        u3.setPassword(u2.getPassword());
        u3.setRole(r.getRoles().get(0));

        assertThrows(DataIntegrityViolationException.class, () -> {
            adminServiceFacade.update(r.getId(), u3);
        });
    }

    @Test
    public void getAllActiveUsers() {
        UserInfoResponse u = adminServiceFacade.create(getRandomUserRequest());

        List<UserInfoResponse> users = adminServiceFacade.getAllActiveUsers(true);

        Assertions.assertTrue(users.size() > 0);

        boolean userFound = false;
        for (UserInfoResponse user : users) {
            if (u.getId().equals(user.getId())) {
                userFound = true;
                assertEquals(u.getEmail(), user.getEmail());
                break;
            }
        }
        Assertions.assertTrue(userFound);

    }

    @Test
    public void getAllUsersWithDeactivedUser() {
        UserInfoResponse u = adminServiceFacade.create(getRandomUserRequest());
        adminServiceFacade.deactivate(u.getId());

        List<UserInfoResponse> users = adminServiceFacade.getAllActiveUsers(true);

        Assertions.assertTrue(users.size() > 0);

        boolean userNotFound = true;
        for (UserInfoResponse user : users) {
            if (u.getId().equals(user.getId())) {
                userNotFound = false;
                assertEquals(u.getEmail(), user.getEmail());
                break;
            }
        }
        Assertions.assertTrue(userNotFound);

    }


    @Test
    public void getAllUsersWithDeactivedUserNotFound() {
        UserInfoResponse u = adminServiceFacade.create(getRandomUserRequest());
        adminServiceFacade.deactivate(u.getId());

        List<UserInfoResponse> users = adminServiceFacade.getAllActiveUsers(true);

        Assertions.assertTrue(users.size() > 0);

        boolean userFound = false;
        for (UserInfoResponse user : users) {
            if (u.getId().equals(user.getId())) {
                userFound = true;
                assertEquals(u.getEmail(), user.getEmail());
                break;
            }
        }
        Assertions.assertFalse(userFound);

    }


    @Test
    @DirtiesContext
    public void testUpdateUserWithHazelCast() {
        UserInfoResponse userResponse = adminServiceFacade.create(getRandomUserRequest());
        assertEquals(userResponse.getName(), ((UserEntity) hazelcastInstance.getMap(HAZELCAST_MAP).get(userResponse.getEmail())).getName());
    }


}
