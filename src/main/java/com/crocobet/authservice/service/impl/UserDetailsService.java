package com.crocobet.authservice.service.impl;

import com.crocobet.authservice.config.security.UserDetailsImpl;
import com.crocobet.authservice.exception.HandledException;
import com.crocobet.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository repository;

    /**
     * Load user details by username (in this case, the user's ID).
     *
     * @param id The ID of the user.
     * @return UserDetails object representing the user details.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return UserDetailsImpl.transform(repository.findById(Long.parseLong(id))
                .orElseThrow(() -> new HandledException("User not found")));
    }
}
