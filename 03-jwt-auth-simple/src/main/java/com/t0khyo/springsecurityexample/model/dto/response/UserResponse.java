package com.t0khyo.springsecurityexample.model.dto.response;

import lombok.Builder;

import java.util.Set;

@Builder
public record UserResponse(
        Long id,
        String firstName,
        String lastName,
        String username,
        String email,
        boolean enabled,
        Set<RoleResponse> roles
) {
}
