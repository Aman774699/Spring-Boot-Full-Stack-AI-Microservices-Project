package com.fitness.ai_service.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "recommendation")
@Builder
@Data
public class Recommendation {
    @Id
    private String id;
    private String activityId;
    private String type;
    private String userId;
    private String recommendation;
    private List<String> improvements;
    private List<String> suggesstion;
    private List<String> safety;
    @CreatedDate
    private LocalDateTime createdAt;

}
