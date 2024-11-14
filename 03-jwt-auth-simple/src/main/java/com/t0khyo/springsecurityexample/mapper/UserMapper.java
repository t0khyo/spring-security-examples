package com.t0khyo.springsecurityexample.mapper;

import com.t0khyo.springsecurityexample.model.dto.request.RegisterRequest;
import com.t0khyo.springsecurityexample.model.dto.response.UserResponse;
import com.t0khyo.springsecurityexample.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(RegisterRequest registerRequest);
    UserResponse toResponse(User user);
}
