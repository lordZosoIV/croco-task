package com.crocobet.authservice;


import com.crocobet.authservice.config.HazelCastConfig;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.response.UserInfoResponse;
import com.crocobet.authservice.service.facade.AdminServiceFacade;
import com.hazelcast.core.HazelcastInstance;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Import(HazelCastConfig.class)
public class ServiceIntegrationTest {

    private static final String USER_NAME = "test";
    private static final String USER_EMAIL = "as@gmail.com";
    private static final String HAZELCAST_MAP = "user";
    private static final UserRegistrationRequest USER_REQUEST = UserRegistrationRequest.builder()
            .email("aaaasdasfs@gmail.com")
            .password("test")
            .name("test")
            .build();
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

    @Test
    public void testRegisterUser() {

        UserInfoResponse result = adminServiceFacade.create(USER_REQUEST);

        assertEquals(USER_NAME, adminServiceFacade.getById(4L).getName());
        assertEquals(USER_EMAIL, result.getEmail());
    }

}
