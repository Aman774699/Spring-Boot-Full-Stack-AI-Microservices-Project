package com.fitness.activity_service.repository;

import com.fitness.activity_service.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepo extends MongoRepository<Activity,String> {
}
