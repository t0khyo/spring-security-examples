package com.t0khyo.springsecurityexample.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.t0khyo.springsecurityexample.exception.InvalidSignatureException;
import com.t0khyo.springsecurityexample.exception.TokenExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Component
public class JwtUtil {
    private final RSAKeyProperties rsaKeys;
    private final JwtProperties jwtProperties;

    public JwtUtil(RSAKeyProperties rsaKeys, JwtProperties jwtProperties) {
        this.rsaKeys = rsaKeys;
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(Authentication authentication) throws JOSEException {
        Instant now = Instant.now();
        Instant expirationTime = Instant.now().plus(jwtProperties.tokenExpirationSeconds(), ChronoUnit.SECONDS);

        final String username = authentication.getName();
        final List<String> roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        JWTClaimsSet claims = new JWTClaimsSet.Builder()
                .subject(username)
                .claim("roles", roles)
                .issuer(jwtProperties.issuer())
                .issueTime(Date.from(now))
                .expirationTime(Date.from(expirationTime))
                .jwtID(UUID.randomUUID().toString())
                .build();

        JWSSigner signer = new RSASSASigner(rsaKeys.privateKey());

        SignedJWT signedJWT = new SignedJWT(
                new JWSHeader.Builder(JWSAlgorithm.RS256).type(JOSEObjectType.JWT).build(),
                claims
        );

        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public SignedJWT validateToken(String token) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWSVerifier verifier = new RSASSAVerifier(rsaKeys.publicKey());

        if (!signedJWT.verify(verifier)) {
            throw new InvalidSignatureException("Invalid JWT signature");
        }

        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        Date expirationTime = claims.getExpirationTime();

        if (expirationTime.before(new Date())) {
            throw new TokenExpiredException("JWT accessToken has expired");
        }
        return signedJWT;
    }

    public String extractUsername(SignedJWT signedJWT) throws ParseException {
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        return claims.getSubject();
    }

    public Set<String> extractRoles(SignedJWT signedJWT) throws ParseException {
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        return new HashSet<>(claims.getStringListClaim("roles"));
    }

    public Date extractExpiration(SignedJWT signedJWT) throws ParseException {
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
        return claims.getExpirationTime();
    }

    public boolean isTokenExpired(SignedJWT signedJWT) throws ParseException {
        Date expiration = extractExpiration(signedJWT);
        return expiration.before(new Date());
    }
}
