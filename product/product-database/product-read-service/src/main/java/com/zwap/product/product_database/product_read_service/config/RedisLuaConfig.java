package com.zwap.product.product_database.product_read_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.script.DefaultRedisScript;

@Configuration
public class RedisLuaConfig {
    @Bean
    public DefaultRedisScript<Boolean> casScript() {
        DefaultRedisScript<Boolean> s = new DefaultRedisScript<>();
        s.setLocation(new ClassPathResource("lua/compare_and_set.lua"));
        s.setResultType(Boolean.class);
        return s;
    }

    @Bean
    public DefaultRedisScript<Boolean> unlockScript() {
        DefaultRedisScript<Boolean> s = new DefaultRedisScript<>();
        s.setLocation(new ClassPathResource("lua/compare_and_del.lua"));
        s.setResultType(Boolean.class);
        return s;
    }
}