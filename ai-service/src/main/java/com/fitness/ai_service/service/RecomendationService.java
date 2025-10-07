package com.fitness.ai_service.service;

import com.fitness.ai_service.model.Recommendation;
import com.fitness.ai_service.repository.RecomendationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecomendationService {
    private final RecomendationRepo recomendationRepo;

    public Optional<Recommendation> getUserRecommendation(String userId){
        return recomendationRepo.findById(userId);
    }

    public Recommendation getActivityRecommendation(String activityId) {
        return recomendationRepo.findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("No recommendation found for this activity: " + activityId));
    }
}
