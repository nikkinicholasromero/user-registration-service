package com.demo.external.email;

import com.demo.external.hash.HashRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${email-service.send.host}")
    private String host;

    @Value("${email-service.send.endpoint}")
    private String endpoint;

    public void send(Mail mail) {
        HttpEntity<Mail> httpEntity = new HttpEntity<>(mail);
        restTemplate.put(host + endpoint, httpEntity);
    }
}
