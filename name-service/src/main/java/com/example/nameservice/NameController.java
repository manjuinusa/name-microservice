package com.example.nameservice;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/names")
public class NameController {

    private final NameRepository repository;

    public NameController(NameRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/health")
    public Health health() {
        return new Health("UP", "name-service");
    }

    @PostMapping
    public NameResponse submit(@Valid @RequestBody NameRequest request) {
        Name entity = new Name();
        entity.setFirstName(request.firstName().trim());
        entity.setLastName(request.lastName().trim());
        entity.setCreatedAt(Instant.now());
        Name saved = repository.save(entity);
        return toResponse(saved);
    }

    @GetMapping
    public List<NameResponse> list() {
        return repository.findAll().stream().map(this::toResponse).toList();
    }

    private NameResponse toResponse(Name n) {
        String fullName = n.getFirstName() + " " + n.getLastName();
        return new NameResponse(
                n.getId().toString(),
                n.getFirstName(),
                n.getLastName(),
                fullName,
                "Hello, " + fullName + "!",
                n.getCreatedAt().toString()
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
