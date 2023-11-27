package com.crocobet.authservice.controller;


import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.request.UserLoginRequest;
import com.crocobet.authservice.model.response.UserAuthResponse;
import com.crocobet.authservice.service.facade.AuthServiceFacade;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceFacade service;


    @PostMapping("/login")
    public ResponseEntity<UserAuthResponse> login(@Valid @RequestBody UserLoginRequest request) {
        UserAuthResponse authResponse = service.login(request);
        return ResponseEntity.ok(authResponse);
    }



}