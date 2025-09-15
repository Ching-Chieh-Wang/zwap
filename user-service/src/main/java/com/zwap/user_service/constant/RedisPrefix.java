package com.zwap.user_service.constant;

import lombok.Getter;

@Getter
public enum RedisPrefix {
    USER("user:");

    private final String prefix;

    RedisPrefix(String prefix) {
        this.prefix = prefix;
    }
}
