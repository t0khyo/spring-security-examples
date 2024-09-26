package com.t0khyo.springsecurityexample.model.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record AuthResponse(
        String token,
        String username,
        Set<RoleResponse> roles
) {
}
