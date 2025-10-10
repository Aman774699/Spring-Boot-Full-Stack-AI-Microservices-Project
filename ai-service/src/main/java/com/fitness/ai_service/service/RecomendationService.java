package com.fitness.ai_service.service;

import com.fitness.ai_service.model.Recommendation;
import com.fitness.ai_service.repository.RecomendationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecomendationService {
    private final RecomendationRepo recomendationRepo;

    public List<Recommendation> getUserRecommendation(String userId){
        return recomendationRepo.findByUserId(userId);
    }

    public Recommendation getActivityRecommendation(String activityId) {
        return recomendationRepo.findByActivityId(activityId)
                .orElseThrow(() -> new RuntimeException("No recommendation found for this activity: " + activityId));
    }
}
