package com.graduate.api.auth.service;

import com.graduate.api.auth.exceptions.AuthErrorEex;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface AuthService {

    String login(String username, String password) throws UsernameNotFoundException,AuthErrorEex;
    String refresh(String oldToken);
}
