package com.fitness.ai_service.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fitness.ai_service.model.Activity;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String bootstrapServers;

    // A tolerant Instant deserializer: try Instant.parse first, then fall back to LocalDateTime (assume UTC)
    public static class TolerantInstantDeserializer extends com.fasterxml.jackson.databind.JsonDeserializer<Instant> {
        private static final DateTimeFormatter ISO_LOCAL = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        @Override
        public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String text = p.getText();
            if (text == null || text.isEmpty()) {
                return null;
            }
            try {
                return Instant.parse(text);
            } catch (Exception ex) {
                try {
                    LocalDateTime ldt = LocalDateTime.parse(text, ISO_LOCAL);
                    return ldt.toInstant(ZoneOffset.UTC);
                } catch (Exception ex2) {
                    // rethrow as JsonMappingException
                    throw com.fasterxml.jackson.databind.JsonMappingException.from(ctxt, "Cannot parse Instant from value: " + text);
                }
            }
        }
    }

    @Bean
    public ObjectMapper kafkaObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // register JavaTimeModule for standard types
        mapper.registerModule(new JavaTimeModule());
        // register our tolerant Instant deserializer
        SimpleModule tolerant = new SimpleModule();
        tolerant.addDeserializer(Instant.class, new TolerantInstantDeserializer());
        mapper.registerModule(tolerant);
        // ensure dates are not written as timestamps if serializer is used elsewhere
        mapper.disable(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public ConsumerFactory<String, Activity> consumerFactory(ObjectMapper kafkaObjectMapper) {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // Keep basic consumer props; we'll set deserializers programmatically so we can inject our ObjectMapper
        DefaultKafkaConsumerFactory<String, Activity> cf = new DefaultKafkaConsumerFactory<>(props);
        // set key deserializer
        cf.setKeyDeserializer(new StringDeserializer());
        // create a Spring JsonDeserializer instance configured with our ObjectMapper
    org.springframework.kafka.support.serializer.JsonDeserializer<Activity> deserializer = new org.springframework.kafka.support.serializer.JsonDeserializer<>(Activity.class, kafkaObjectMapper);
    // Do not attempt to resolve producer-side class names from type headers (producer may use a different package).
    deserializer.setUseTypeHeaders(false);
    deserializer.addTrustedPackages("*");
        // wrap it with ErrorHandlingDeserializer so deserialization exceptions are handled before the listener error handler
        ErrorHandlingDeserializer<Activity> ehd = new ErrorHandlingDeserializer<>(deserializer);
        cf.setValueDeserializer(ehd);
        return cf;
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Activity> kafkaListenerContainerFactory(ConsumerFactory<String, Activity> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, Activity> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(new DefaultErrorHandler());
        return factory;
    }
}
