package com.fitness.gateway_service.user;

import lombok.Data;

import java.util.UUID;

@Data
public class RegisterRequest {
    private String email;
    private UUID keycloakId;
    private String password;
    private String firstName;
    private String lastName;
}
