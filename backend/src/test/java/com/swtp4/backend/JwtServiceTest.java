package com.swtp4.backend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.swtp4.backend.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    private final String jwtSecret = "kdcg/HWhPwXlI0IzwNAph97Vwv7mnQ5tM0EH//MmSjXrbGkFTB1jCupER02sJQykbmBHg8TahS7YIl2tJ8uh4A=="; // Use a test secret key
    private final long jwtExpirationInMillis = 3600000; // 1 hour for example
    private final String jwtHeader = "Authorization";
    private final String jwtPrefix = "Bearer ";

    @BeforeEach
    public void setUp() {
        lenient().when(authentication.getName()).thenReturn("testUser");

        // Use reflection to set the fields in JwtService
        ReflectionTestUtils.setField(jwtService, "jwtSecret", jwtSecret);
        ReflectionTestUtils.setField(jwtService, "jwtExpirationInMillis", jwtExpirationInMillis);
        ReflectionTestUtils.setField(jwtService, "jwtHeader", jwtHeader);
        ReflectionTestUtils.setField(jwtService, "jwtPrefix", jwtPrefix);
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

    @Test
    public void whenValidateToken_withValidToken_thenShouldReturnTrue() {
        String token = jwtService.generateToken(authentication);
        assertTrue(jwtService.validateToken(token));
    }

    @Test
    public void whenValidateToken_withInvalidToken_thenShouldReturnFalse() {
        String invalidToken = "invalid.token.here";
        assertFalse(jwtService.validateToken(invalidToken));
    }

    @Test
    public void whenGetUsernameFromToken_withValidToken_thenShouldReturnUsername() {
        String token = jwtService.generateToken(authentication);
        String username = jwtService.getUsernameFromToken(token);
        assertEquals("testUser", username);
    }

    @Test
    public void whenResolveToken_withBearerToken_thenShouldReturnToken() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer token");

        String token = jwtService.resolveToken(request);
        assertEquals("token", token);
    }

    @Test
    public void whenResolveToken_withoutBearerPrefix_thenShouldReturnNull() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("token");

        String token = jwtService.resolveToken(request);
        assertNull(token);
    }

}
