package com.zwap.product.product_database.product_write_service.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import  com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

@Component
public class UpdateBuilderUtils {

    private final ObjectMapper objectMapper;

    public UpdateBuilderUtils(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Update buildUpdateFrom(Object source) {
        Update update = new Update();
        Map<String, Object> map = objectMapper.convertValue(source, new TypeReference<>() {
        });
        map.forEach((key, value) -> {
            if (value != null) {
                update.set(key, value);
            }
        });
        return update;
    }
}

