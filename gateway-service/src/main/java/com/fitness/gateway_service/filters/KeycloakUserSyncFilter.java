package com.fitness.gateway_service.filters;

import com.fitness.gateway_service.user.RegisterRequest;
import com.fitness.gateway_service.user.UserService;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class KeycloakUserSyncFilter implements WebFilter {

    private final UserService userService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String userIdHeader = exchange.getRequest().getHeaders().getFirst("X-User-ID");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        // Safely parse UUID
        UUID userId = null;
        if (userIdHeader != null && !userIdHeader.isBlank()) {
            try {
                userId = UUID.fromString(userIdHeader);
            } catch (IllegalArgumentException e) {
                log.warn("Invalid UUID in X-User-ID header: {}", userIdHeader);
            }
        }

        RegisterRequest request = getUserDetails(token);
        if (userId == null && request != null) {
            userId = request.getKeycloakId();
        }

        // If still missing either userId or token, just continue the filter chain
        if (userId == null || token == null) {
            return chain.filter(exchange);
        }

        UUID finalUserId = userId;

        return userService.validateUser(String.valueOf(finalUserId))
                .flatMap(exists -> {
                    if (!exists) {
                        if (request != null) {
                            // Register new user if not exists
                            return userService.registerUser(request).then(Mono.just(true));
                        } else {
                            return Mono.just(true);
                        }
                    } else {
                        log.info("User already exists: {}", finalUserId);
                        return Mono.just(true);
                    }
                })
                .flatMap(done -> {
            // Mutate the reactive request with the X-User-ID header
            ServerHttpRequest mutatedRequest = exchange.getRequest()
                .mutate()
                .header("X-User-ID", finalUserId.toString())
                .build();

            // Continue the filter chain with the mutated exchange
            return chain.filter(exchange.mutate().request(mutatedRequest).build());
                });
    }


    private RegisterRequest getUserDetails(String token) {
        if (token == null || token.isBlank()) {
            log.warn("Authorization token is missing or empty");
            return null;
        }

        try {
            // Remove "Bearer" prefix if present
            String tokenWithoutBearer = token.replace("Bearer", "").trim();

            // Parse JWT
            SignedJWT signedJWT = SignedJWT.parse(tokenWithoutBearer);
            JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

            // Map claims to RegisterRequest
            RegisterRequest request = new RegisterRequest();
            request.setEmail(claims.getStringClaim("email"));
            request.setKeycloakId(UUID.fromString(claims.getStringClaim("sub")));
            request.setFirstName(claims.getStringClaim("given_name"));
            request.setLastName(claims.getStringClaim("family_name"));
            request.setPassword("dummy@123"); // Default password for first registration
            return request;

        } catch (Exception e) {
            log.error("Failed to parse JWT token: {}", e.getMessage());
            return null;
        }
    }

}
