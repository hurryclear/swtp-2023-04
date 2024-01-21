package com.swtp4.backend.ApplicationControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swtp4.backend.controller.ApplicationController;
import com.swtp4.backend.repositories.MajorUniRepository;
import com.swtp4.backend.repositories.ModuleUniRepository;
import com.swtp4.backend.repositories.dto.ApplicationDto;
import com.swtp4.backend.repositories.entities.ModuleUniEntity;
import com.swtp4.backend.services.ApplicationService;
import lombok.With;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ApplicationControllerIntegrationTests {

    private ApplicationService applicationService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MajorUniRepository majorUniRepository;
    private ModuleUniRepository moduleUniRepository;

    @Autowired
    public ApplicationControllerIntegrationTests(
            ApplicationService applicationService,
            MockMvc mockMvc,
            MajorUniRepository majorUniRepository,
            ModuleUniRepository moduleUniRepository) {
        this.applicationService = applicationService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.majorUniRepository = majorUniRepository;
        this.moduleUniRepository = moduleUniRepository;
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void testThatSaveApplicationSuccessfullyReturnsHttp201Created() throws Exception {
        String testApplicationJson = TestData.createTestApplicationJsonA();
        ModuleUniEntity testModuleUniA = TestData.createTestModuleUniEntityA();
        ModuleUniEntity testModuleUniB = TestData.createTestModuleUniEntityB();
        ModuleUniEntity testModuleUniD = TestData.createTestModuleUniEntityD();
        ModuleUniEntity testModuleUniE = TestData.createTestModuleUniEntityE();
        majorUniRepository.saveAll(Arrays.asList(testModuleUniA.getMajorUniEntity(), testModuleUniB.getMajorUniEntity(), testModuleUniD.getMajorUniEntity(), testModuleUniE.getMajorUniEntity()));
        moduleUniRepository.saveAll(Arrays.asList(testModuleUniA, testModuleUniB, testModuleUniD, testModuleUniE));
        mockMvc.perform(
                MockMvcRequestBuilders.post("/application/saveApplication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testApplicationJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }
}
