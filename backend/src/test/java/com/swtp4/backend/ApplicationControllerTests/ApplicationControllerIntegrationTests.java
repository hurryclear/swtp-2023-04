package com.swtp4.backend.ApplicationControllerTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.swtp4.backend.repositories.*;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.services.ApplicationService;
import net.bytebuddy.dynamic.DynamicType;
import org.assertj.core.api.OptionalAssert;
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
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class ApplicationControllerIntegrationTests {

    private ApplicationService applicationService;
    private MockMvc mockMvc;
    private MajorUniRepository majorUniRepository;
    private ModuleUniRepository moduleUniRepository;
    private ApplicationRepository applicationRepository;
    private ModuleBlockRepository moduleBlockRepository;
    private ModuleStudentRepository moduleStudentRepository;
    private ModuleRelationRepository moduleRelationRepository;

    @Autowired
    public ApplicationControllerIntegrationTests(
            ApplicationService applicationService,
            MockMvc mockMvc,
            MajorUniRepository majorUniRepository,
            ModuleUniRepository moduleUniRepository,
            ApplicationRepository applicationRepository,
            ModuleBlockRepository moduleBlockRepository,
            ModuleStudentRepository moduleStudentRepository,
            ModuleRelationRepository moduleRelationRepository) {
        this.applicationService = applicationService;
        this.mockMvc = mockMvc;
        this.majorUniRepository = majorUniRepository;
        this.moduleUniRepository = moduleUniRepository;
        this.applicationRepository = applicationRepository;
        this.moduleStudentRepository = moduleStudentRepository;
        this.moduleBlockRepository = moduleBlockRepository;
        this.moduleRelationRepository = moduleRelationRepository;
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void testThatSaveApplicationSuccessfullyReturnsHttp201CreatedAndCreatesEntities() throws Exception {
        String testApplicationJson = ApplicationTestData.createTestApplicationJsonA();
        ModuleUniEntity testModuleUniA = ApplicationTestData.createTestModuleUniEntityA();
        ModuleUniEntity testModuleUniB = ApplicationTestData.createTestModuleUniEntityB();
        ModuleUniEntity testModuleUniD = ApplicationTestData.createTestModuleUniEntityD();
        ModuleUniEntity testModuleUniE = ApplicationTestData.createTestModuleUniEntityE();
        majorUniRepository.saveAll(Arrays.asList(testModuleUniA.getMajorUniEntity(), testModuleUniB.getMajorUniEntity(), testModuleUniD.getMajorUniEntity(), testModuleUniE.getMajorUniEntity()));
        moduleUniRepository.saveAll(Arrays.asList(testModuleUniA, testModuleUniB, testModuleUniD, testModuleUniE));
        mockMvc.perform(
                MockMvcRequestBuilders.post("/application/saveApplication")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testApplicationJson)
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
        //ApplicationEntity Check
        Optional<ApplicationEntity> existingApplicationEntity = Optional.ofNullable(applicationRepository.findByDateOfSubmissionAndApplicationKeyClass_Creator("2023-12-31T22:30:42.103Z", "Student"));
        assertThat(existingApplicationEntity).isPresent();
        assertThat(existingApplicationEntity.get().getUniversityName()).isEqualTo("University of Regenbogenland");
        assertThat(existingApplicationEntity.get().getApplicationKeyClass().getCreator()).isEqualTo("Student");
        //ModuleBlockEntity Check
        Optional<ModuleBlockEntity> existingModuleBlockEntity = Optional.ofNullable(moduleBlockRepository.findByCommentStudentAndApplicationEntity_ApplicationKeyClass_Creator("Das ist mein Block", "Employee"));
        assertThat(existingModuleBlockEntity).isPresent();
        assertThat(existingModuleBlockEntity.get().getCommentEmployee()).isEqualTo("Das ist sein Block");
        assertThat(existingModuleBlockEntity.get().getApplicationEntity().getApplicationKeyClass().getCreator()).isEqualTo("Employee");
        //ModuleStudentEntity Check
        Optional<ModuleStudentEntity> existingModuleStudentEntity = Optional.ofNullable(moduleStudentRepository.findByNumberAndCreator("420", "Student"));
        assertThat(existingModuleStudentEntity).isPresent();
        assertThat(existingModuleStudentEntity.get().getTitle()).isEqualTo("AlgoDat 1.5");
        //ModuleRelationEntity Check

        List<ModuleRelationEntity> allModuleRelationalEntities = moduleRelationRepository.findAll();
        System.out.println("RelationRepository: " + allModuleRelationalEntities);

        Optional<ModuleRelationEntity> existingModuleRelationEntity = Optional.ofNullable(moduleRelationRepository.findByModuleRelationKeyClass_ModuleStudentEntity_NumberAndModuleRelationKeyClass_ModuleUniEntity_NameAndModuleBlockEntity_ApplicationEntity_ApplicationKeyClass_Creator("69", "Algorithmen und Datenstrukturen 1", "Student"));
        assertThat(existingModuleRelationEntity).isPresent();
        assertThat(existingModuleRelationEntity.get().getModuleRelationKeyClass().getModuleStudentEntity().getTitle()).isEqualTo("AlgoDat 0.5");
        assertThat(existingModuleRelationEntity.get().getModuleRelationKeyClass().getModuleUniEntity().getNumber()).isEqualTo("10-201-2001-1");
        assertThat(existingModuleRelationEntity.get().getModuleBlockEntity().getApplicationEntity().getApplicationKeyClass().getCreator()).isEqualTo("Student");
    }
}
