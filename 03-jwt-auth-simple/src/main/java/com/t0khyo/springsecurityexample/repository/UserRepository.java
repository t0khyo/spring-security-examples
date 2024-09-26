package com.t0khyo.springsecurityexample.repository;

import com.t0khyo.springsecurityexample.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
