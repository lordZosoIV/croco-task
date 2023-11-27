package com.crocobet.authservice.service.facade;


import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.model.request.UserLoginRequest;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.response.UserAuthResponse;
import com.crocobet.authservice.model.response.UserInfoResponse;
import com.crocobet.authservice.service.AuthService;
import com.crocobet.authservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceFacade {

    private final AuthService authService;
    private final JwtService jwtService;

    public UserAuthResponse login(UserLoginRequest request) {
        UserEntity user = authService.login(request);
        return getAuthResponse(user);
    }

    public UserInfoResponse register(UserRegistrationRequest request) {
        UserEntity user = authService.register(request);
        return UserInfoResponse.transform(user);
    }


    private UserAuthResponse getAuthResponse(UserEntity user) {
        String uid = user.getId().toString();
        List<String> roles = user.getRoles().stream().map(d->d.getName().name()).collect(Collectors.toList());
        return UserAuthResponse.builder()
                .accessToken(jwtService.generateAccessToken(uid, roles))
                .build();
    }

}
