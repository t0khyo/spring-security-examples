package com.t0khyo.springsecurityexample.security;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix="jwt")
public record JwtProperties(long tokenExpirationSeconds, String issuer) {
}
