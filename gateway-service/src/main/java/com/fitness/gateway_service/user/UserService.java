package com.fitness.gateway_service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final WebClient userServiceWebClient;

    public Mono<Boolean> validateUser(String userId)
    {
        log.info("Calling User Validation API  for userId:{}",userId);
        UUID uuid = UUID.fromString(userId);
        try{
            return userServiceWebClient.get()
                    .uri("/api/user/isUserExist/{id}",uuid)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .onErrorResume(WebClientResponseException.class,e->{
                        if(e.getStatusCode() == HttpStatus.NOT_FOUND)
                        {
                            return Mono.error(new RuntimeException("User not found"));
                        }
                        return Mono.error(new RuntimeException("Unexpected error: "+userId));
                    });
        }catch (WebClientResponseException e)
        {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND)
                throw new RuntimeException("User Not Found: " + userId);
            else if (e.getStatusCode() == HttpStatus.BAD_REQUEST)
                throw new RuntimeException("Invalid Request: " + userId);
        }
        return Mono.just(false);
    }

    public Mono<RegisterResponse> registerUser(RegisterRequest request) {
        log.info("Calling user Registration for {}",request.getEmail());
        // Call the user-service registration endpoint with POST and the request body.
        // The user-service endpoint returns a ResponseEntity<String> message on success.
        // For the gateway's filter we only need a successful completion, so map success
        // to a lightweight RegisterResponse constructed from the request.
        return userServiceWebClient.post()
                .uri("/api/user/registerUser")
                .bodyValue(request)
                .retrieve()
                .toEntity(String.class)
                .map(responseEntity -> {
                    // Build a minimal RegisterResponse to signal success to callers.
                    RegisterResponse rr = new RegisterResponse();
                    rr.setEmail(request.getEmail());
                    rr.setKeycloakId(request.getKeycloakId());
                    // password and other fields left null by intention
                    return rr;
                })
                .onErrorResume(WebClientResponseException.class, e -> {
                    // Log and return a minimal RegisterResponse so the gateway filter does not fail the chain.
                    log.warn("User registration request failed (status={}): {}; returning fallback RegisterResponse", e.getStatusCode(), e.getMessage());
                    RegisterResponse rr = new RegisterResponse();
                    rr.setEmail(request.getEmail());
                    rr.setKeycloakId(request.getKeycloakId());
                    return Mono.just(rr);
                })
                .onErrorResume( Throwable.class, t -> {
                    log.error("Unexpected error while calling user-service registerUser: {}", t.getMessage());
                    RegisterResponse rr = new RegisterResponse();
                    rr.setEmail(request.getEmail());
                    rr.setKeycloakId(request.getKeycloakId());
                    return Mono.just(rr);
                });
    }
}
