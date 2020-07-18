package com.demo.external.hash;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HashService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${hash-service.hash.host}")
    private String host;

    @Value("${hash-service.hash.endpoint}")
    private String endpoint;

    public String hash(String clear, String salt) {
        HashRequest hashRequest = new HashRequest(clear, salt);
        HttpEntity<HashRequest> httpEntity = new HttpEntity<>(hashRequest);
        return restTemplate.postForEntity(host + endpoint, httpEntity, String.class).getBody();
    }
}
