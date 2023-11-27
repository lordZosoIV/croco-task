package com.crocobet.authservice.service;

import com.crocobet.authservice.config.security.UserDetailsImpl;
import com.crocobet.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
    private final UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        return UserDetailsImpl.transform(repository.findById(Long.parseLong(id)).orElseThrow(() -> new RuntimeException("asd")));
    }
}

