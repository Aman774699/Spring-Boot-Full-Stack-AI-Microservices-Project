package com.fitness.ai_service.service;

import com.fitness.ai_service.model.Activity;
import com.fitness.ai_service.model.Recommendation;
import com.fitness.ai_service.repository.RecomendationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListner {

    private final ActivityAIService activityAIService;

    private final RecomendationRepo recomendationRepo;

    @KafkaListener(topics = "${spring.kafka.topic.name}",groupId = "activity-processor-group")
    public void processActivity(Activity activity)
    {
      log.info("Received Activity for processing..."+activity.getUserId());
      Recommendation recommendation=activityAIService.generateRecommendation(activity);
        log.info("Recomendation is..."+recommendation);
      recomendationRepo.save(recommendation);
    }

}
