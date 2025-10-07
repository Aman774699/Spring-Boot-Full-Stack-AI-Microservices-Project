package com.fitness.activity_service.controller;

import com.fitness.activity_service.model.DTOs.ActivityRequest;
import com.fitness.activity_service.service.ActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/activities")
@AllArgsConstructor
public class ActivityController {

    ActivityService activityService;

    @PostMapping("/post/activity/{userId}")
    ResponseEntity<?> postActivity(@RequestBody ActivityRequest activityRequest, @PathVariable("userId") String userId) {
        if(userId!=null)
        {
            activityRequest.setUserId(userId);
        }
        return ResponseEntity.ok(activityService.trackActivity(activityRequest));
    }

}
