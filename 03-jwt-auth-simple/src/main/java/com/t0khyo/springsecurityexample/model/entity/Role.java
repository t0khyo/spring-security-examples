package com.t0khyo.springsecurityexample.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;


@Setter
@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
public class Role extends BaseEntity implements GrantedAuthority {

    @Column(nullable=false, unique=true)
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
