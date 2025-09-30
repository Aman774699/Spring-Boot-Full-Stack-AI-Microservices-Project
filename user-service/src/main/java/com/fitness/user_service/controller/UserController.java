package com.fitness.user_service.controller;

import com.fitness.user_service.models.DTO.RegisterRequest;
import com.fitness.user_service.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
public class UserController {


    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registerUser")
    Mono<ResponseEntity<RegisterRequest>> registerUser(@RequestBody RegisterRequest registerRequest) {
        return userService.regisration(registerRequest)
                .map(response -> ResponseEntity.ok(response))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }


}
