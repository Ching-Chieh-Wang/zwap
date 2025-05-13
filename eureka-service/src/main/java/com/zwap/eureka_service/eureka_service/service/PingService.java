package com.zwap.eureka_service.eureka_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PingService {

    private final DiscoveryClient discoveryClient;
    private final RestTemplate restTemplate = new RestTemplate();

    @Scheduled(fixedRate = 600000)
    public void pingClients() {
        List<String> services = discoveryClient.getServices();
        for (String service : services) {
            // Skip pinging the eureka-service itself
            if ("eureka-service".equalsIgnoreCase(service)) continue;
            List<ServiceInstance> instances = discoveryClient.getInstances(service);
            for (ServiceInstance instance : instances) {
                String url = instance.getUri().toString();
                try {
                    restTemplate.postForEntity(url + "/ping", null, String.class);
                    log.info("Successfully pinged {}", url);
                } catch (Exception ex) {
                    log.warn("Failed to contact {}", url, ex);
                }
            }
        }
    }
}
