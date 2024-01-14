package com.swtp4.backend;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("integration") // This controller is only active when the "integration" profile is set
public class IntegrationTestController {

    @GetMapping("/testProtectedEndpoint")
    public ResponseEntity<?> testProtectedEndpoint() {
        return ResponseEntity.ok("Accessed protected endpoint!");
    }
}