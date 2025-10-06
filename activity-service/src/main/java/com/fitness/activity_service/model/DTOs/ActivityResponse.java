package com.fitness.activity_service.model.DTOs;

import com.fitness.activity_service.model.ActivityType;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class ActivityResponse {
    private String id;
    private String userId;
    private ActivityType type;
    private Integer duration;
    private Integer caloriesBurned;
    private Instant startTime;
    private Map<String, Object> additionalMetrics;
    private Instant createdAt;
    private Instant updatedAt;
}
