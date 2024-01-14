package com.swtp4.backend;

import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("integration") // This controller is only active when the "integration" profile is set
@RequestMapping("/test")
public class IntegrationTestController {

    @GetMapping("/protectedEndpoint")
    public ResponseEntity<?> testProtectedEndpoint() {
        return ResponseEntity.ok("Access granted to protected endpoint.");
    }

    @GetMapping("/committeeOnly")
    @PreAuthorize("hasRole('COMMITTEE')")
    public ResponseEntity<String> committeeOnly() {
        return ResponseEntity.ok("Access granted to committee.");
    }

    @GetMapping("/office")
    @PreAuthorize("hasRole('OFFICE')")
    public ResponseEntity<String> officeOnly() {
        return ResponseEntity.ok("Access granted to office.");
    }

}