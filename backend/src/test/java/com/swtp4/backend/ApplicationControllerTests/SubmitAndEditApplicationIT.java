package com.swtp4.backend.ApplicationControllerTests;

import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.ModuleBlockRepository;
import com.swtp4.backend.repositories.ModuleRelationRepository;
import com.swtp4.backend.repositories.ModuleStudentRepository;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.services.ApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) //IMPORTANT, this resets Application Context, ID Sequences start at 1 again
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("integration")
public class SubmitAndEditApplicationIT {

    private ApplicationService applicationService;
    private MockMvc mockMvc;
    private ApplicationTestData applicationTestData;
    private ApplicationRepository applicationRepository;
    private ModuleBlockRepository moduleBlockRepository;
    private ModuleStudentRepository moduleStudentRepository;
    private ModuleRelationRepository moduleRelationRepository;

    @Autowired
    public SubmitAndEditApplicationIT(ApplicationService applicationService,
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
    public void testThatSubmitApplicationSuccessfullyReturnsHttp201CreatedAndCreatesEntities() throws Exception {
        applicationTestData.loadMajorAndModulesIntoDataBase();
        String testApplicationJson = ApplicationTestData.createSubmitApplicationJson();

        // Create a MockMultipartFile for the JSON content
        MockMultipartFile jsonPart = new MockMultipartFile("form", "", MediaType.APPLICATION_JSON_VALUE, testApplicationJson.getBytes());
        // Perform the request with multipart data, but no file part
        mockMvc.perform(
                multipart("/student/submitApplication")
                        .file(jsonPart)// specify your endpoint here
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.applicationID", not(is(emptyString()))));

        //ApplicationEntity Check
        Example<ApplicationEntity> application = Example.of(ApplicationEntity.builder()
                .status("offen")
                .dateOfSubmission("2023-12-31T22:30:42.103Z")
                .dateLastEdited("2024-01-14T14:12:14.675Z")
                .universityName("University of Regenbogenland")
                .studentMajor("B. Sc. Informatik")
                .uniMajor("B.Sc. Informatik")
                .formalRejectionReason("")
                .build());

        assertThat(applicationRepository.findAll(application))
                .hasSize(2)
                .extracting(savedApplication-> savedApplication.getApplicationKeyClass().getCreator())
                .containsExactlyInAnyOrder("Employee", "Student");

        //BlockEntity check
        Example<ModuleBlockEntity> block1 = Example.of(ModuleBlockEntity.builder()
                .frontendKey(0L).build());
        assertThat(moduleBlockRepository.findAll(block1)).hasSize(2);
        //BlockEntity check
        Example<ModuleBlockEntity> block2 = Example.of(ModuleBlockEntity.builder()
                .frontendKey(1L).build());
        assertThat(moduleBlockRepository.findAll(block2)).hasSize(2);

        //ModuleStudentEntity check
        Example<ModuleStudentEntity> module1 = Example.of(ModuleStudentEntity.builder()
                .frontendKey(0L)
                .approval("")
                .approvalReason("")
                .number("420")
                .title("AlgoDat 1.5")
                .credits(5L)
                .university("University of Regenbogenland")
                .major("B. Sc. Informatik")
                .commentStudent("War cool")
                .commentEmployee("")
                .build());
        assertThat(moduleStudentRepository.findAll(module1))
                .hasSize(2)
                .extracting(ModuleStudentEntity::getCreator)
                .containsExactlyInAnyOrder("Employee", "Student");

        //ModuleStudentEntity check
        Example<ModuleStudentEntity> module2 = Example.of(ModuleStudentEntity.builder()
                .frontendKey(1L)
                .approval("")
                .approvalReason("")
                .number("69")
                .title("AlgoDat 0.5")
                .credits(4L)
                .university("University of Regenbogenland")
                .major("B. Sc. Informatik")
                .commentStudent("War nicht so cool")
                .commentEmployee("")
                .build());
        assertThat(moduleStudentRepository.findAll(module2))
                .hasSize(2)
                .extracting(ModuleStudentEntity::getCreator)
                .containsExactlyInAnyOrder("Employee", "Student");

        //ModuleStudentEntity check
        Example<ModuleStudentEntity> module3 = Example.of(ModuleStudentEntity.builder()
                .frontendKey(0L)
                .approval("")
                .approvalReason("")
                .number("81923")
                .title("Das Alles-Modul")
                .credits(20L)
                .university("University of Regenbogenland")
                .major("B. Sc. Informatik")
                .commentStudent("Die Beschreibung ist lang, setzen Sie schon mal Kaffee auf")
                .commentEmployee("")
                .build());
        assertThat(moduleStudentRepository.findAll(module3))
                .hasSize(2)
                .extracting(ModuleStudentEntity::getCreator)
                .containsExactlyInAnyOrder("Employee", "Student");

        //Relation check 12 = 3 Modules * 2 UnimodulesEach * 2CreatorEach
        assertThat(moduleRelationRepository.findAll()).hasSize(12)
                .extracting(relation -> relation.getModuleBlockEntity().getId().toString()
                        +relation.getModuleRelationKeyClass().getModuleStudentEntity().getId().toString()
                        +relation.getModuleRelationKeyClass().getModuleUniEntity().getId().toString())
                .containsExactlyInAnyOrder("113", "114", "133", "134", "3515", "3516",
                        "223", "224", "243", "244", "4615", "4616");
    }

    @Test
    @Transactional
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void testThatEditApplicationSuccessfullyReturnsHttp201CreatedAndCreatesEntities() throws Exception {
        applicationTestData.loadMajorAndModulesIntoDataBase();
        String testApplicationJson = ApplicationTestData.createSubmitApplicationJson();

        // Create a MockMultipartFile for the JSON content
        MockMultipartFile jsonPart = new MockMultipartFile("form", "", MediaType.APPLICATION_JSON_VALUE, testApplicationJson.getBytes());
        // Perform the request with multipart data, but no file part
        mockMvc.perform(
                        multipart("/student/submitApplication")
                                .file(jsonPart)// specify your endpoint here
                                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isCreated());

        List<ApplicationEntity> savedSubmitted = applicationRepository.findAll();
        assertThat(savedSubmitted).isNotEmpty();
        String savedApplicationID = savedSubmitted.get(0).getApplicationKeyClass().getId();

        String editedApplicationJson = ApplicationTestData.createEditedApplicationJson(savedApplicationID);

        mockMvc.perform(post("/application/saveEdited")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(editedApplicationJson))
                .andExpect(status().isCreated());

        //ApplicationEntity Check
        Example<ApplicationEntity> application = Example.of(ApplicationEntity.builder()
                .applicationKeyClass(ApplicationKeyClass.builder()
                        .id(savedApplicationID)
                        .creator("Employee")
                        .build())
                .dateOfSubmission("2023-12-31T22:30:42.103Z")
                .dateLastEdited("2040-03-05T13:56:51.560Z")
                .universityName("edited")
                .studentMajor("edited")
                .uniMajor("B.Sc. Informatik")
                .formalRejectionReason("")
                .build());

        List<ApplicationEntity> allApplications = applicationRepository.findAll();
        assertThat(applicationRepository.findAll(application))
                .hasSize(1)
                .extracting(savedApplication-> savedApplication.getStatus())
                .doesNotContain("edited");

        //BlockEntity check
        Example<ModuleBlockEntity> block1 = Example.of(ModuleBlockEntity.builder()
                .frontendKey(0L).build());
        assertThat(moduleBlockRepository.findAll(block1)).hasSize(2);
        //BlockEntity check
        Example<ModuleBlockEntity> block2 = Example.of(ModuleBlockEntity.builder()
                .frontendKey(1L).build());
        assertThat(moduleBlockRepository.findAll(block2)).hasSize(2);

        //ModuleStudentEntity check
        Example<ModuleStudentEntity> module1 = Example.of(ModuleStudentEntity.builder()
                .id(2L)
                .frontendKey(0L)
                .approval("")
                .approvalReason("")
                .number("edited")
                .title("edited")
                .credits(0L)
                .university("edited")
                .major("edited")
                .commentStudent("edited")
                .commentEmployee("edited")
                .creator("Employee")
                .build());
        assertThat(moduleStudentRepository.findAll(module1))
                .hasSize(1)
                .extracting(ModuleStudentEntity::getDescription_pdf)
                .doesNotContain("edited");

        //ModuleStudentEntity check
        Example<ModuleStudentEntity> module2 = Example.of(ModuleStudentEntity.builder()
                .id(4L)
                .frontendKey(0L)
                .approval("")
                .approvalReason("")
                .number("edited")
                .title("edited")
                .credits(0L)
                .university("edited")
                .major("edited")
                .commentStudent("edited")
                .commentEmployee("edited")
                .creator("Employee")
                .build());
        assertThat(moduleStudentRepository.findAll(module2))
                .hasSize(1)
                .extracting(ModuleStudentEntity::getDescription_pdf)
                .doesNotContain("edited");

        //ModuleStudentEntity check
        Example<ModuleStudentEntity> module3 = Example.of(ModuleStudentEntity.builder()
                .id(6L)
                .frontendKey(1L)
                .approval("")
                .approvalReason("")
                .number("edited")
                .title("edited")
                .credits(0L)
                .university("edited")
                .major("edited")
                .commentStudent("edited")
                .commentEmployee("edited")
                .creator("Employee")
                .build());
        assertThat(moduleStudentRepository.findAll(module3))
                .hasSize(1)
                .extracting(ModuleStudentEntity::getDescription_pdf)
                .doesNotContain("edited");

        //Relation check 10 = 6 Original + (2 StudentModule * 1 UniModule) + (1 StudentModule * 2UniModules)
        assertThat(moduleRelationRepository.findAll()).hasSize(10)
                .extracting(relation -> relation.getModuleBlockEntity().getId().toString()
                        +relation.getModuleRelationKeyClass().getModuleStudentEntity().getId().toString()
                        +relation.getModuleRelationKeyClass().getModuleUniEntity().getId().toString())
                .containsExactlyInAnyOrder("113", "114", "133", "134", "3515", "3516",
                        "221", "261", "445", "446");

    }
}
