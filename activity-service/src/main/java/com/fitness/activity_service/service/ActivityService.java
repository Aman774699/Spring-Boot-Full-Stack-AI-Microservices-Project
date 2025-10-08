package com.fitness.activity_service.service;

import com.fitness.activity_service.model.Activity;
import com.fitness.activity_service.model.DTOs.ActivityRequest;
import com.fitness.activity_service.model.DTOs.ActivityResponse;
import com.fitness.activity_service.repository.ActivityRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepo activityRepo;

    private final ModelMapper modelMapper;

    private final UserValidationService userValidationService;

    private final KafkaTemplate<String,Activity> kafkaTemplate;

    @Value("${spring.kafka.topic.event}")
    private String topicName;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        boolean isValidUser = userValidationService.validateUser(activityRequest.getUserId());
        if (!isValidUser) {
            throw new RuntimeException("Invalid User: " + activityRequest.getUserId());
        }
        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();
        activityRepo.save(activity);
        try {
            kafkaTemplate.send(topicName,activity.getUserId(),activity);
        }
        catch (Exception e){
           e.printStackTrace();
        }
        return modelMapper.map(activity, ActivityResponse.class);
    }
}
