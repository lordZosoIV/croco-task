package com.crocobet.authservice.model.response;

import com.crocobet.authservice.entity.RoleEntity;
import com.crocobet.authservice.entity.UserEntity;
import com.crocobet.authservice.entity.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UserInfoResponse {
    private Long id;
    private String email;
    private String name;
    private Boolean active;
    private List<Role> roles;

    public static List<UserInfoResponse> transform(List<UserEntity> users) {
        if (users == null || users.isEmpty()) {
            return null;
        }
        return users.stream().map(UserInfoResponse::transform).collect(Collectors.toList());
    }

    public static UserInfoResponse transform(UserEntity user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .active(user.getActive())
                .roles(user.getRoles().stream().map(RoleEntity::getName).toList())
                .build();
    }
}
