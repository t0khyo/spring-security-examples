package com.t0khyo.springsecurityexample.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UserUpdateRequest(
        @NotBlank(message="{firstName.NotBlank}")
        String firstName,
        @NotBlank(message="{lastName.NotBlank}")
        String lastName,
        @NotBlank(message="{username.NotBlank}")
        @Size(min=3, max=50, message="{username.Size}")
        String username,
        @Email(message="{email.Email}")
        @NotBlank(message="{email.NotBlank}")
        String email
) {
}
