package com.fitness.ai_service.repository;

import com.fitness.ai_service.model.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RecomendationRepo extends MongoRepository<Recommendation,String> {
    List<Recommendation> findByUserId(String userId);
    Optional<Recommendation> findByActivityId(String activityId);
}
