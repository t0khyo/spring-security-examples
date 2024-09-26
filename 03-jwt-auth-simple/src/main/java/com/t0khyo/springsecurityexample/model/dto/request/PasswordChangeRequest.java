package com.t0khyo.springsecurityexample.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record PasswordChangeRequest(
        @NotBlank(message="{oldPassword.NotBlank}")
        String oldPassword,
        @NotBlank(message="{newPassword.NotBlank}")
        @Size(min=8, message="password.Size")
        String newPassword
) {
}
