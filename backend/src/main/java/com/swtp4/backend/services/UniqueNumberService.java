package com.swtp4.backend.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UniqueNumberService {

    public String generateUniqueNumber() {
        // Generiert eine zufällige UUID
        return UUID.randomUUID().toString();
    }
}