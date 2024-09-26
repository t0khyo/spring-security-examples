package com.t0khyo.springsecurityexample.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRequest(
        @NotBlank(message="{username.NotBlank}")
        String username,
        @NotBlank(message="password.NotBlank")
        String password
) {
}
