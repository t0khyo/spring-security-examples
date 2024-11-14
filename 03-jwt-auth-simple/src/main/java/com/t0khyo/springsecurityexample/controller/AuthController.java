package com.t0khyo.springsecurityexample.controller;

import com.nimbusds.jose.JOSEException;
import com.t0khyo.springsecurityexample.model.dto.request.LoginRequest;
import com.t0khyo.springsecurityexample.model.dto.request.RegisterRequest;
import com.t0khyo.springsecurityexample.model.dto.response.UserResponse;
import com.t0khyo.springsecurityexample.security.JwtUtil;
import com.t0khyo.springsecurityexample.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest) throws JOSEException {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    loginRequest.username(), loginRequest.password()
                            )
                    );

            log.info("logged in: {}", authentication.getPrincipal());

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtUtil.generateToken(authentication)
                    )
                    .build();
        } catch (BadCredentialsException ex) {
            log.warn("login(): BadCredentialsException -> {}", ex.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @GetMapping("/profile")
    public ResponseEntity<UserResponse> getProfile(Authentication authentication) {
        return null;
    }

}
