package com.security.web.configuration;

import com.security.data.audit.config.DatabaseConfiguration;
import com.security.data.audit.holders.ObjectMapperHolder;
import com.security.data.audit.model.User;
import com.security.security.jwt.configuration.SecurityConfig;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@Import({DatabaseConfiguration.class, SecurityConfig.class})
@EnableAutoConfiguration
@EnableWebMvc
@ComponentScan(basePackages = {"com.security.service", "com.security.web.controller"})
public class AppConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper(){
        return new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setSerializationInclusion(JsonInclude.Include.NON_NULL);
    }

    @Bean
    public ModelMapper modelMapper(){
       ModelMapper modelMapper = new ModelMapper();
       modelMapper.addMappings(new PropertyMap<User, User>() {
           @Override
           protected void configure() {
               skip().setCreatedTime(null);
           }
       });
       return modelMapper;
    }

    @Bean
    public ObjectMapperHolder objectMapperHolder(){
        return new ObjectMapperHolder(objectMapper());
    }
}
