package com.fitness.user_service.controller;

import com.fitness.user_service.models.DTO.RegisterRequest;
import com.fitness.user_service.models.DTO.RegisterResponse;
import com.fitness.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {


    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registerUser")
    Mono<ResponseEntity<String>> registerUser(@RequestBody RegisterRequest registerRequest) {
        return userService.isEmailExist(registerRequest.getEmail())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.just(ResponseEntity
                                .badRequest()
                                .body("Email is already register"));
                    } else {
                        return userService.regisration(registerRequest)
                                .map(savedUser -> ResponseEntity.ok("User Saved Successfully"))
                                .defaultIfEmpty(ResponseEntity.status(500).body("There was an error in registering the user"));
                    }
                });
    }

    @GetMapping("/getUser/{id}")
    Mono<ResponseEntity<RegisterResponse>> getUser(@PathVariable("id") UUID id)
    {
        return userService.getUser(id).map(ResponseEntity::ok).switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }


}
