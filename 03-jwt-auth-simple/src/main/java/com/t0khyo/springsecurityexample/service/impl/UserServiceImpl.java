package com.t0khyo.springsecurityexample.service.impl;

import com.t0khyo.springsecurityexample.mapper.UserMapper;
import com.t0khyo.springsecurityexample.model.dto.request.RegisterRequest;
import com.t0khyo.springsecurityexample.model.dto.response.UserResponse;
import com.t0khyo.springsecurityexample.model.entity.User;
import com.t0khyo.springsecurityexample.repository.UserRepository;
import com.t0khyo.springsecurityexample.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
    }

    @Override
    public UserResponse register(RegisterRequest registerRequest) {
        User user = userMapper.toEntity(registerRequest);
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(registerRequest.password()));
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }
}
