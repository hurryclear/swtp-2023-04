package com.swtp4.backend;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.swtp4.backend.security.Role;
import com.swtp4.backend.security.UserEntity;
import com.swtp4.backend.security.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class JwtAuthenticationAndAuthorizationIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationInMillis;

    @Value("${jwt.header}")
    private String jwtHeader;

    @Value("${jwt.prefix}")
    private String jwtPrefix;

    @BeforeEach
    public void setup() {
        UserEntity user = new UserEntity("testuser", passwordEncoder.encode("password"), Role.OFFICE);
        userRepository.save(user);
    }

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void testAuthenticationWithValidCredentials() throws Exception {
        String jsonBody = "{\"username\":\"testuser\",\"password\":\"password\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    public void testAuthenticationWithInvalidCredentials() throws Exception {
        String jsonBody = "{\"username\":\"wronguser\",\"password\":\"wrongpassword\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAccessProtectedEndpointWithValidToken() throws Exception {
        String validToken = JWT.create()
                .withSubject("testuser")
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtExpirationInMillis))
                .sign(Algorithm.HMAC512(jwtSecret));

        mockMvc.perform(get("/testProtectedEndpoint")
                        .header(jwtHeader, jwtPrefix + validToken))
                .andExpect(status().isOk());
    }

    @Test
    public void testAccessProtectedEndpointWithInvalidToken() throws Exception {
        String invalidToken = "thats.invalid.Token";

        mockMvc.perform(get("/testProtectedEndpoint")
                        .header(jwtHeader, jwtPrefix + invalidToken))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAccessProtectedEndpointWithExpiredToken() throws Exception {
        Date date = new Date(System.currentTimeMillis() - 2*jwtExpirationInMillis);
        Date expiredDate = new Date(System.currentTimeMillis() - jwtExpirationInMillis);
        String invalidToken = JWT.create()
                                .withSubject("testuser")
                                .withIssuedAt(date)
                                .withExpiresAt(expiredDate)
                                .sign(Algorithm.HMAC512(jwtSecret));

        mockMvc.perform(get("/testProtectedEndpoint")
                        .header(jwtHeader, jwtPrefix + invalidToken))
                .andExpect(status().isUnauthorized());
    }


}
