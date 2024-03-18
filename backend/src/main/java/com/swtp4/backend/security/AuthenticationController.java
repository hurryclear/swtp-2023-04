package com.swtp4.backend.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class AuthenticationController {
    // controls login and logout procedure by using the AuthenticationService
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    // office and committee can log in using this endpoint
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @RequestBody AuthenticationRequest authenticationRequest) {
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(response);
    }

    // office and committee can log out using this endpoint
    @GetMapping("/logout")
    public ResponseEntity<?> logoutUser() {
        authenticationService.logout();
        return ResponseEntity.ok("Logout successful");
    }
}