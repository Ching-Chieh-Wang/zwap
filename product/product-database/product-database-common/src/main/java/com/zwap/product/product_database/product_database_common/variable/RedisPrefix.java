package com.zwap.product.product_database.product_database_common.variable;

import lombok.Getter;

@Getter
public enum RedisPrefix {
    PRODUCT("product:");

    private final String prefix;

    RedisPrefix(String prefix) {
        this.prefix = prefix;
    }
}
