package com.example.webservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProxyController {

    private final WebClient webClient;
    private final String nameServiceUrl;

    public ProxyController(WebClient webClient,
                           @Value("${name-service.url:http://localhost:8081}") String nameServiceUrl) {
        this.webClient = webClient;
        this.nameServiceUrl = nameServiceUrl;
    }

    @PostMapping(value = "/submit", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> submit(@RequestBody Map<String, String> body) {
        try {
            Object response = webClient.post()
                    .uri(nameServiceUrl + "/api/names")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return ResponseEntity.ok(response);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getResponseBodyAsString()));
        } catch (Exception e) {
            return ResponseEntity.status(502)
                    .body(Map.of("error", "name-service unreachable: " + e.getMessage()));
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> list() {
        try {
            Object response = webClient.get()
                    .uri(nameServiceUrl + "/api/names")
                    .retrieve()
                    .bodyToMono(Object.class)
                    .block();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(502)
                    .body(Map.of("error", "name-service unreachable: " + e.getMessage()));
        }
    }

    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP", "service", "web-service");
    }
}
