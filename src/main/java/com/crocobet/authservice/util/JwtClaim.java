package com.crocobet.authservice.util;

public enum JwtClaim {
    ROLES("roles");

    public final String value;

    JwtClaim(String value) {
        this.value = value;
    }
}