package com.zwap.eureka_service.eureka_service.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix = "zwap.ping")
@Data
public class PingProperties {
    private List<String> clients;
}