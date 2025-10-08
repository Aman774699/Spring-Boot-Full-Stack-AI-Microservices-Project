package com.fitness.ai_service.service;

import com.fitness.ai_service.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityMessageListner {

    @KafkaListener(topics = "${spring.kafka.topic.name}",groupId = "activity-processor-group")
    public void processActivity(Activity activity)
    {
      log.info("Received Activity for processing..."+activity.getUserId());
    }

}
