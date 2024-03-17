package com.swtp4.backend.services;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UniqueNumberService {

    public String generateUniqueNumber() {
        // Generiert eine zufällige UUID
        String generatedUUID = UUID.randomUUID().toString();
        String cleanedUUID = generatedUUID.replace("-", "");
        return  cleanedUUID.substring(0, 4) + "-" +
                cleanedUUID.substring(4, 8) + "-" +
                cleanedUUID.substring(8, 12) + "-" +
                cleanedUUID.substring(12, 16) + "-" +
                cleanedUUID.substring(16, 20) + "-" +
                cleanedUUID.substring(20, 24) + "-" +
                cleanedUUID.substring(24, 28) + "-" +
                cleanedUUID.substring(28);
    }
}