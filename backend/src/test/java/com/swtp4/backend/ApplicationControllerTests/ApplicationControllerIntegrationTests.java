package com.swtp4.backend.ApplicationControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swtp4.backend.controller.ApplicationController;
import com.swtp4.backend.services.ApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ApplicationControllerIntegrationTests {

    private ApplicationService applicationService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @Autowired
    public ApplicationControllerIntegrationTests(ApplicationService applicationService, MockMvc mockMvc) {
        this.applicationService = applicationService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @Test
    public void testThatSaveApplicationSuccessfullyReturnsHttp201Created() {

    }
}
