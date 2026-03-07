package org.aclogistics.coe.infrastructure.configuration.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Rosendo Coquilla
 */
@Configuration
public class ObjectMapperConfiguration {

    @Bean
    public ObjectMapper mapper() {
        return JsonMapper.builder()
            .addModule(new JavaTimeModule())
            .disable(
                SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
                SerializationFeature.FAIL_ON_EMPTY_BEANS
            ).propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .build();
    }
}
