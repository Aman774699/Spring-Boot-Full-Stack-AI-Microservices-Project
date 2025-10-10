package com.fitness.activity_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.util.List;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    // Using java.time.Instant in the domain model removes the need for LocalDateTime converters.

    @Bean
    public MongoMappingContext mongoMappingContext(MongoCustomConversions customConversions) {
        // Let Spring inject custom conversions so the mapping context is aware of
        // the LocalDateTime <-> String converters and won't attempt reflective
        // access to java.time internals.
        MongoMappingContext context = new MongoMappingContext();
        // Ensure the mapping context uses the SimpleTypeHolder from our custom conversions
        // so LocalDateTime is treated via converters rather than being reflectively inspected.
        context.setSimpleTypeHolder(customConversions.getSimpleTypeHolder());
        return context;
    }

    @Bean
    public MongoCustomConversions customConversions() {
        // No custom conversions required for Instant. Keep a placeholder empty conversion list
        // so other code that expects a bean can still autowire it.
        return new MongoCustomConversions(List.of());
    }

}


