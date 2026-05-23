package com.example.nameservice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NameRepository extends JpaRepository<Name, UUID> {
}
