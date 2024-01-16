package com.swtp4.backend.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    // just a dto which is turned into a json by springboot
    private String token;
    private String role;
}