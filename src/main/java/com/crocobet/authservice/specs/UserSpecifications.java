package com.crocobet.authservice.specs;

import com.crocobet.authservice.entity.UserEntity;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {

    public static Specification<UserEntity> isActive(Boolean active) {
        return (root, query, criteriaBuilder) -> {
            if (active == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(UserEntity.Fields.active), active);
        };
    }
}
