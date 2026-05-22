package com.example.nameservice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/api/names")
public class NameController {

    @GetMapping("/health")
    public Health health() {
        return new Health("UP", "name-service");
    }

    @PostMapping
    public NameResponse submit(@Valid @RequestBody NameRequest request) {
        String fullName = (request.firstName().trim() + " " + request.lastName().trim()).trim();
        String greeting = "Hello, " + fullName + "!";
        return new NameResponse(
                UUID.randomUUID().toString(),
                request.firstName().trim(),
                request.lastName().trim(),
                fullName,
                greeting,
                Instant.now().toString()
        );
    }

    public record NameRequest(
            @NotBlank @Size(max = 50) String firstName,
            @NotBlank @Size(max = 50) String lastName
    ) {}

    public record NameResponse(
            String id,
            String firstName,
            String lastName,
            String fullName,
            String greeting,
            String createdAt
    ) {}

    public record Health(String status, String service) {}
}
