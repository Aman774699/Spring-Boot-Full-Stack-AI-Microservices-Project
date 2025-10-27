package com.fitness.gateway_service.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RegisterResponse {
    private UUID id;
    @Email
    @NotBlank
    private String email;
    @NotBlank(message = "Password cannot blank")
    private String password;
    private UUID keycloakId;
    private String firstName;
    private String lastName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
