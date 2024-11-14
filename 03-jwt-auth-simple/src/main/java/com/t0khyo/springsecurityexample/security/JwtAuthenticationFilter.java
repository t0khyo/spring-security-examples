package com.t0khyo.springsecurityexample.security;

import com.nimbusds.jwt.SignedJWT;
import com.t0khyo.springsecurityexample.exception.InvalidAccessTokenException;
import com.t0khyo.springsecurityexample.exception.InvalidSignatureException;
import com.t0khyo.springsecurityexample.exception.TokenExpiredException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Decide whether the filter should be applied.
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.warn("No JWT accessToken found in request headers or accessToken format is invalid.");
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Apply filter: authenticate or reject request
        final String jwt = authHeader.substring(7);
        final String username;
        final SignedJWT verifiedJwt;

        try {
            verifiedJwt = jwtUtil.validateToken(jwt);
            username = jwtUtil.extractUsername(verifiedJwt);

            if (username != null && !username.isBlank() && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (TokenExpiredException ex) {
            logger.warn("Token expired", ex);
            throw ex;

        } catch (InvalidSignatureException ex) {
            logger.warn("Invalid JWT signature", ex);
            throw ex;

        } catch (Exception ex) {
            logger.error("Token validation failed", ex);
            throw new InvalidAccessTokenException("JWT accessToken validation failed");
        }

        // 3. Invoke the "rest" of the chain
        filterChain.doFilter(request, response);

        // 4. No cleanup
    }
}
