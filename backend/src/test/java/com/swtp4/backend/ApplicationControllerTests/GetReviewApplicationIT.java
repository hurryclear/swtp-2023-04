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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("integration")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GetReviewApplicationIT {

    private  MockMvc mockMvc;
    private  ApplicationTestData applicationTestData;

    @MockBean
    private  ApplicationService applicationService;

    @MockBean
    private  ModuleUniRepository moduleUniRepository;


    @Autowired
    public GetReviewApplicationIT(
            MockMvc mockMvc,
            ApplicationTestData applicationTestData,
            ApplicationService applicationService,
            ModuleUniRepository moduleUniRepository) {
        this.mockMvc = mockMvc;
        this.applicationTestData = applicationTestData;
        this.applicationService = applicationService;
        this.moduleUniRepository = moduleUniRepository;
    }

    @Test
    public void testThatGetReviewApplication_ReturnsOriginalApplicationJson() throws Exception {

        applicationTestData.loadMajorAndModulesIntoDataBase();
        String testOriginalSubmittedApplication = ApplicationTestData.createOriginalSubmitApplicationJson();

        MockMultipartFile jsonPart = new MockMultipartFile(
                "form",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                testOriginalSubmittedApplication.getBytes()
        );

        // Create a MockMultipartFile for a PDF file
        InputStream pdfInputStream = getClass().getResourceAsStream("/pdf_example.pdf");
        MockMultipartFile pdfPart = new MockMultipartFile(
                "file-0:0",
                "pdf_example.pdf",
                MediaType.APPLICATION_PDF_VALUE,
                pdfInputStream);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.multipart("/student/submitApplication")
                        .file(jsonPart)
                        .file(pdfPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        // Capture the result;
        String contentAsString = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(contentAsString);
        String applicationID = jsonResponse.getString("applicationID");

        String testReviewApplication = ApplicationTestData.createTestReviewApplication();
        JSONObject jsonExpected = new JSONObject(testReviewApplication);
        String path1 = "/" + applicationID + "/S-1";
        String path2 = "/" + applicationID + "/S-3";
        String path3 = "/" + applicationID + "/S-5";
        JSONObject editedObjectID = jsonExpected.getJSONObject("applicationData");
        editedObjectID.put("applicationID", applicationID);

        mockMvc.perform(MockMvcRequestBuilders.get("/student/reviewApplication")
                        .param("applicationID", applicationID)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(jsonExpected.toString()))
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}
