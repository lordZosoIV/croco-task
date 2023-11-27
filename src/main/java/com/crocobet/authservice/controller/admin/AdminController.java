package com.crocobet.authservice.controller.admin;



import com.crocobet.authservice.entity.model.Role;
import com.crocobet.authservice.model.request.UserRegistrationRequest;
import com.crocobet.authservice.model.request.UserUpdateRequest;
import com.crocobet.authservice.model.response.UserInfoResponse;
import com.crocobet.authservice.service.facade.AdminServiceFacade;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminController {
    private final AdminServiceFacade service;

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoResponse> getUser(@PathVariable("id") @Min(1) Long id) {
        UserInfoResponse user = service.getById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserInfoResponse>> getAllActiveUsers(@RequestParam(required = false) Boolean active) {
        List<UserInfoResponse> users = service.getAllActiveUsers(active);
        return ResponseEntity.ok(users);
    }

    @PostMapping
    public ResponseEntity<UserInfoResponse> createUser(@RequestBody @Valid UserRegistrationRequest userRegistrationRequest) {
        UserInfoResponse user = service.create(userRegistrationRequest);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserInfoResponse> updateUser(@PathVariable("id") Long id,
                                                           @RequestBody @Valid UserUpdateRequest userUpdateRequest) {
        UserInfoResponse user = service.update(id, userUpdateRequest);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") @Min(1) Long id) {
        service.deactivate(id);
        return ResponseEntity.ok().build();
    }


}