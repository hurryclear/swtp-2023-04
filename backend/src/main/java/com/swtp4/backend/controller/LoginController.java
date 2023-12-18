package com.swtp4.backend.controller;

import com.swtp4.backend.services.AuthenticationService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private AuthenticationService authenticationService;
}
