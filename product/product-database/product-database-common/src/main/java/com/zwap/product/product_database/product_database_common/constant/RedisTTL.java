package com.zwap.product.product_database.product_database_common.constant;

import lombok.Getter;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Getter
public enum RedisTTL {
    PRODUCT(Duration.ofSeconds(3000)),
    LOCKED(Duration.ofSeconds(50)),
    NOT_FOUND(Duration.ofSeconds(450));

    private final Duration duration;

    RedisTTL(Duration duration) {
        this.duration = duration;
    }

    /**
     * Returns the TTL in seconds, with jitter applied.
     */
    public Duration getJitteredDuration() {
        long baseSeconds = duration.getSeconds();
        if (baseSeconds <= 0) return Duration.ZERO;
        double factor = 0.8 + ThreadLocalRandom.current().nextDouble(0.4);
        long jitteredSeconds = Math.max(1, Math.round(baseSeconds * factor));
        return Duration.ofSeconds(jitteredSeconds);
    }
}