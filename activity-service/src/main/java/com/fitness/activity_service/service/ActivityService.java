package com.fitness.activity_service.service;

import com.fitness.activity_service.model.Activity;
import com.fitness.activity_service.model.DTOs.ActivityRequest;
import com.fitness.activity_service.model.DTOs.ActivityResponse;
import com.fitness.activity_service.repository.ActivityRepo;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ActivityService {

    ActivityRepo activityRepo;

    ModelMapper modelMapper;

    public ActivityResponse trackActivity(ActivityRequest activityRequest) {
        Activity activity = Activity.builder()
                .userId(activityRequest.getUserId())
                .type(activityRequest.getType())
                .duration(activityRequest.getDuration())
                .caloriesBurned(activityRequest.getCaloriesBurned())
                .startTime(activityRequest.getStartTime())
                .additionalMetrics(activityRequest.getAdditionalMetrics())
                .build();
        activityRepo.save(activity);
        return modelMapper.map(activity, ActivityResponse.class);
    }
}
