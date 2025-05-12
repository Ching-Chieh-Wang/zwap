package com.zwap.eureka_service.eureka_service.service;

import com.zwap.eureka_service.eureka_service.properties.PingProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class PingService {

    private final PingProperties pingProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 600000)
    public void pingClients() {
        for (String url : pingProperties.getClients()) {
            try {
                restTemplate.postForEntity(url, null, String.class);
                log.info("Successfully pinged {}", url);
            } catch (Exception ex) {
                log.warn("Failed to contact {}", url, ex);
            }
        }
    }
}
