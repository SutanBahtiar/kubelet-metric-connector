package com.github.sutanbahtiar.config;

/*
 * @author sutan.bahtiar@gmail.com
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JsonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
//                .configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, false)
                .build();
    }
}
