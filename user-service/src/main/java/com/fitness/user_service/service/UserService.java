package com.fitness.user_service.service;

import com.fitness.user_service.models.DTO.RegisterRequest;
import com.fitness.user_service.models.DTO.RegisterResponse;
import com.fitness.user_service.models.User;
import com.fitness.user_service.respository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class UserService {
    UserRepository userRepository;
    ModelMapper modelMapper;

    public UserService(UserRepository userRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    public Mono<RegisterRequest> regisration(RegisterRequest registerRequest) {
        User user = modelMapper.map(registerRequest, User.class);
        return userRepository.save(user).map(savedUser -> modelMapper.map(savedUser, RegisterRequest.class));
    }

    public Mono<Boolean> isEmailExist(String email) {
        return userRepository.existsByEmail(email);
    }

    public Mono<RegisterResponse> getUser(UUID id) {
        return userRepository.findById(id).map(registerUser -> modelMapper.map(registerUser, RegisterResponse.class)).switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }

    public Mono<Boolean> isUserValidate(UUID id) {
        return userRepository.existsByKeycloakId(id);
    }

}
