package com.demo.external.hash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SaltGenerationService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${hash-service.salt.host}")
    private String host;

    @Value("${hash-service.salt.endpoint}")
    private String endpoint;

    public String generateRandomSalt() {
        return restTemplate.getForEntity(host + endpoint, String.class).getBody();
    }
}
