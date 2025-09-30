package com.fitness.user_service.models.DTO;

import com.fitness.user_service.models.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RegisterResponse {
    private String id;
    @Email
    @NotBlank
    private String email;
    @NotBlank(message = "Password cannot blank")
    private String password;
    private String firstName;
    private String lastName;
    private UserRole role = UserRole.USER;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
