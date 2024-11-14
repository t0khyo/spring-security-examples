package com.t0khyo.springsecurityexample.model.dto.response;

import lombok.Builder;

@Builder
public record AuthResponse(
        String accessToken,
        String username
) {
}
