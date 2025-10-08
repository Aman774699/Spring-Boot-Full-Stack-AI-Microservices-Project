package com.fitness.activity_service.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@EnableMongoAuditing
public class MongoConfig {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    @Bean
    public MongoMappingContext mongoMappingContext()
    {
        return  new MongoMappingContext();
    }
    @WritingConverter
    public static class LocalDateTimeToStringConverter implements Converter<LocalDateTime, String> {
        @Override
        public String convert(LocalDateTime source) {
            return source.format(FORMATTER);
        }
    }

    @ReadingConverter
    public static class StringToLocalDateTimeConverter implements Converter<String, LocalDateTime> {
        @Override
        public LocalDateTime convert(String source) {
            return LocalDateTime.parse(source, FORMATTER);
        }
    }

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(
                List.of(new LocalDateTimeToStringConverter(), new StringToLocalDateTimeConverter())
        );
    }

}
