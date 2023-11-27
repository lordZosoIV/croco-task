package com.crocobet.authservice.util;

import com.crocobet.authservice.config.security.JwtAuthenticationToken;
import com.crocobet.authservice.config.security.UserDetailsImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static Long getAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && authentication instanceof JwtAuthenticationToken jwtAuthentication) {
            UserDetailsImpl principal = (UserDetailsImpl) jwtAuthentication.getPrincipal();
            return principal.getId();
        }
        return null;
    }
}
