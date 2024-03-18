package com.swtp4.backend.ApplicationControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swtp4.backend.controller.StudentController;
import com.swtp4.backend.repositories.ModuleUniRepository;
import com.swtp4.backend.repositories.applicationDtos.ReviewApplicationDto;
import com.swtp4.backend.services.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GetReviewApplicationIT {

    private final MockMvc mockMvc;
    private final ApplicationTestData applicationTestData;

    @MockBean
    private final ApplicationService applicationService;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private final ModuleUniRepository moduleUniRepository;


    @Autowired
    public GetReviewApplicationIT(
            MockMvc mockMvc,
            ApplicationTestData applicationTestData,
            ApplicationService applicationService,
            ModuleUniRepository moduleUniRepository,
            ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.applicationTestData = applicationTestData;
        this.applicationService = applicationService;
        this.moduleUniRepository = moduleUniRepository;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    public void setupReviewApplication() throws Exception {
//        applicationTestData.loadMajorAndModulesIntoDataBase();
        String testReviewApplicationJson = ApplicationTestData.createTestReviewApplication();
        ReviewApplicationDto mockReviewApplicationDto = objectMapper.readValue(testReviewApplicationJson, ReviewApplicationDto.class);
        when(applicationService.getReviewApplication(anyString())).thenReturn(mockReviewApplicationDto);
    }

    @Test
    public void testThatGetReviewApplication_ReturnsOriginalApplicationJson() throws Exception {

        applicationTestData.loadMajorAndModulesIntoDataBase();

//
//        String applicationID = "23-23-23-23-23-23-23-23-23-23-23";
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/student/reviewApplication")
//                        .param("applicationID", applicationID)
//                        .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.applicationData.applicationID").value("23-23-23-23-23-23-23-23-23-23-23")
//                .andExpect(MockMvcResultMatchers.jsonPath("$.applicationData.status").value(mockReviewApplicationDto.applicationData().status()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.applicationData.formalRejection").value(mockReviewApplicationDto.applicationData().formalRejection()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.applicationData.dateOfSubmission").value(mockReviewApplicationDto.applicationData().dateOfSubmission()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.applicationData.dateLastEdited").value(mockReviewApplicationDto.applicationData().dateLastEdited()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.applicationData.University").value(mockReviewApplicationDto.applicationData().university()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.applicationData.oldCourseOfStudy").value(mockReviewApplicationDto.applicationData().oldCourseOfStudy()))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.applicationData.newCourseOfStudy").value(mockReviewApplicationDto.applicationData().newCourseOfStudy())
//        );
    }
}
