package com.crocobet.authservice.service.facade.impl;

import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.model.request.UserLoginRequest;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.response.UserAuthResponse;
import com.crocobet.authservice.model.response.UserInfoResponse;
import com.crocobet.authservice.service.AuthService;
import com.crocobet.authservice.service.JwtService;
import com.crocobet.authservice.service.facade.AuthServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceFacadeImpl implements AuthServiceFacade {

    private final AuthService authService;
    private final JwtService jwtService;

    /**
     * Perform user login based on the provided login request.
     *
     * @param request The login request containing user credentials.
     * @return UserAuthResponse containing the access token.
     */
    @Override
    public UserAuthResponse login(UserLoginRequest request) {
        UserEntity user = authService.login(request);
        return getAuthResponse(user);
    }

    /**
     * Register a new user based on the provided registration request.
     *
     * @param request The registration request containing user information.
     * @return UserInfoResponse representing the newly registered user.
     */
    @Override
    public UserInfoResponse register(UserRegistrationRequest request) {
        UserEntity user = authService.register(request);
        return UserInfoResponse.transform(user);
    }

    /**
     * Generate UserAuthResponse containing the access token based on the user information.
     *
     * @param user The user entity for which the access token is generated.
     * @return UserAuthResponse containing the access token.
     */
    private UserAuthResponse getAuthResponse(UserEntity user) {
        String uid = user.getId().toString();
        List<String> roles = user.getRoles().stream().map(d -> d.getName().name()).collect(Collectors.toList());
        return UserAuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(uid, roles))
                .build();
    }
}
