package com.swtp4.backend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.swtp4.backend.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMillis;

    @BeforeEach
    public void setUp() {
        when(authentication.getName()).thenReturn("testUser");
    }

    @Test
    public void whenGenerateToken_thenTokenShouldContainCorrectClaims() {
        String token = jwtService.generateToken(authentication);
        assertNotNull(token);

        // Decode the token to verify its claims
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(jwtSecret)).build().verify(token);
        assertEquals("testUser", decodedJWT.getSubject());
        assertTrue(decodedJWT.getExpiresAt().after(new Date()));
    }
}
