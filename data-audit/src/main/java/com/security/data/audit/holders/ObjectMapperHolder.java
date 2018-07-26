package com.security.data.audit.holders;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperHolder {
    private static ObjectMapper objectMapper;


    public ObjectMapperHolder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) throw new RuntimeException("You need to init ObjectMapper");
        return objectMapper;
    }


}
