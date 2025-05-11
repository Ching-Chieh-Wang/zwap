package com.zwap.eureka_service.eureka_service.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class PingService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${zwap.ping.clients}")
    private List<String> clientUrls;

    @Scheduled(fixedRate = 600000)
    public void pingClients() {
        for (String url : clientUrls) {
            try {
                restTemplate.postForEntity(url, null, String.class);
                log.info("Successfully pinged {}", url);
            } catch (Exception ex) {
                log.warn("Failed to contact {}", url, ex);
            }
        }
    }
}
