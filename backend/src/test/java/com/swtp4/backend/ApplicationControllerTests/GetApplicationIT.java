package com.swtp4.backend.ApplicationControllerTests;

import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.ModuleBlockRepository;
import com.swtp4.backend.repositories.ModuleRelationRepository;
import com.swtp4.backend.repositories.ModuleStudentRepository;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.PDFService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) //IMPORTANT, this resets Application Context, ID Sequences start at 1 again
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class GetApplicationIT {


    @MockBean
    private PDFService pdfService;
    private ApplicationService applicationService;
    private MockMvc mockMvc;
    private ApplicationTestData applicationTestData;
    private ApplicationRepository applicationRepository;
    private ModuleBlockRepository moduleBlockRepository;
    private ModuleStudentRepository moduleStudentRepository;
    private ModuleRelationRepository moduleRelationRepository;

    @Autowired
    public GetApplicationIT(ApplicationService applicationService,
                                      MockMvc mockMvc,
                                      ApplicationTestData applicationTestData,
                                      ApplicationRepository applicationRepository,
                                      ModuleBlockRepository moduleBlockRepository,
                                      ModuleStudentRepository moduleStudentRepository,
                                      ModuleRelationRepository moduleRelationRepository) {
        this.applicationService = applicationService;
        this.mockMvc = mockMvc;
        this.applicationTestData = applicationTestData;
        this.applicationRepository = applicationRepository;
        this.moduleBlockRepository = moduleBlockRepository;
        this.moduleStudentRepository = moduleStudentRepository;
        this.moduleRelationRepository = moduleRelationRepository;
    }

    @Test
    @Transactional
    public void testThatGetReviewApplicationAsExpected() throws Exception {
        applicationTestData.loadMajorAndModulesIntoDataBase();
        String testApplicationJson = ApplicationTestData.createSubmitApplicationJson();

        // Create a MockMultipartFile for the JSON content
        MockMultipartFile jsonPart = new MockMultipartFile("form", "", MediaType.APPLICATION_JSON_VALUE, testApplicationJson.getBytes());

        // Perform the request with multipart data, but no file part
        MvcResult result = mockMvc.perform(
                        multipart("/student/submitApplication")
                                .file(jsonPart) // specify your endpoint here
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn(); // Capture the result

        String contentAsString = result.getResponse().getContentAsString();
        JSONObject jsonResponse = new JSONObject(contentAsString);
        String applicationID = jsonResponse.getString("applicationID");

        mockMvc.perform(get("/student/reviewApplication").param("applicationID", applicationID))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.applicationData.applicationID").value(applicationID))
                .andExpect(jsonPath("$.applicationData.status").value("open"))
                .andExpect(jsonPath("$.applicationData.formalRejection").value(""))
                .andExpect(jsonPath("$.applicationData.dateOfSubmission").value("2023-12-31T22:30:42.103Z"))
                .andExpect(jsonPath("$.applicationData.dateLastEdited").value("2024-01-14T14:12:14.675Z"))
                .andExpect(jsonPath("$.applicationData.university").value("University of Regenbogenland"))
                .andExpect(jsonPath("$.applicationData.oldCourseOfStudy").value("B. Sc. Informatik"))
                .andExpect(jsonPath("$.applicationData.newCourseOfStudy").value("B.Sc. Informatik"))
                .andExpect(jsonPath("$.moduleFormsData[0].frontend_key").value(0))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[0].frontend_key").value(0))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[0].approval").value(""))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[0].reason").value(""))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[0].number").value("420"))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[0].title").value("AlgoDat 1.5"))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[0].credits").value(5))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[0].university").value("University of Regenbogenland"))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[0].major").value("B. Sc. Informatik"))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[0].commentStudent").value("War cool"))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[1].frontend_key").value(1))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[1].approval").value(""))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[1].reason").value(""))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[1].number").value("69"))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[1].title").value("AlgoDat 0.5"))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[1].credits").value(4))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[1].university").value("University of Regenbogenland"))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[1].major").value("B. Sc. Informatik"))
                .andExpect(jsonPath("$.moduleFormsData[0].modulesStudent[1].commentStudent").value("War nicht so cool"))
                .andExpect(jsonPath("$.moduleFormsData[0].modules2bCredited[0].name").value("Algorithmen und Datenstrukturen 1"))
                .andExpect(jsonPath("$.moduleFormsData[0].modules2bCredited[0].number").value("10-201-2001-1"))
                .andExpect(jsonPath("$.moduleFormsData[0].modules2bCredited[1].name").value("Algorithmen und Datenstrukturen 2"))
                .andExpect(jsonPath("$.moduleFormsData[0].modules2bCredited[1].number").value("10-201-2001-2"))
                .andExpect(jsonPath("$.moduleFormsData[1].frontend_key").value(1))
                .andExpect(jsonPath("$.moduleFormsData[1].modulesStudent[0].frontend_key").value(0))
                .andExpect(jsonPath("$.moduleFormsData[1].modulesStudent[0].approval").value(""))
                .andExpect(jsonPath("$.moduleFormsData[1].modulesStudent[0].reason").value(""))
                .andExpect(jsonPath("$.moduleFormsData[1].modulesStudent[0].number").value("81923"))
                .andExpect(jsonPath("$.moduleFormsData[1].modulesStudent[0].title").value("Das Alles-Modul"))
                .andExpect(jsonPath("$.moduleFormsData[1].modulesStudent[0].credits").value(20))
                .andExpect(jsonPath("$.moduleFormsData[1].modulesStudent[0].university").value("University of Regenbogenland"))
                .andExpect(jsonPath("$.moduleFormsData[1].modulesStudent[0].major").value("B. Sc. Informatik"))
                .andExpect(jsonPath("$.moduleFormsData[1].modulesStudent[0].commentStudent").value("Die Beschreibung ist lang, setzen Sie schon mal Kaffee auf"))
                .andExpect(jsonPath("$.moduleFormsData[1].modules2bCredited[0].name").value("Analysis"))
                .andExpect(jsonPath("$.moduleFormsData[1].modules2bCredited[0].number").value("10-201-1011"))
                .andExpect(jsonPath("$.moduleFormsData[1].modules2bCredited[1].name").value("Lineare Algebra"))
                .andExpect(jsonPath("$.moduleFormsData[1].modules2bCredited[1].number").value("10-201-1015"));
    }
}
