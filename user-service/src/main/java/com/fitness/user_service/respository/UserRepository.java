package com.fitness.user_service.respository;

import com.fitness.user_service.models.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {
    Mono<Boolean> existsByEmail(String email);
    Mono<Boolean> existsById(UUID id);
}
