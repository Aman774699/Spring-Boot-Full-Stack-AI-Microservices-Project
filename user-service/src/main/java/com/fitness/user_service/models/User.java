package com.fitness.user_service.models;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table(name = "users")
@Data
public class User {
    @Id
    private String id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private UserRole role = UserRole.USER;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
