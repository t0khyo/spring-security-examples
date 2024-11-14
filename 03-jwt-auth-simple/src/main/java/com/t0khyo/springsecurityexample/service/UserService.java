package com.t0khyo.springsecurityexample.service;

import com.t0khyo.springsecurityexample.model.dto.request.RegisterRequest;
import com.t0khyo.springsecurityexample.model.dto.response.UserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    UserResponse register(RegisterRequest userRegistrationRequest);
}
