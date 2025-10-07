package com.fitness.ai_service.controller;

import com.fitness.ai_service.model.Recommendation;
import com.fitness.ai_service.service.RecomendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecomendationService recommendationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Optional<Recommendation>> getUserRecommendation(@PathVariable String userId) {
        return ResponseEntity.ok(recommendationService.getUserRecommendation(userId));
    }

    @GetMapping("/activity/{activityId}")
    public ResponseEntity<Recommendation> getActivityRecommendation(@PathVariable String activityId) {
        return ResponseEntity.ok(recommendationService.getActivityRecommendation(activityId));
    }

}
