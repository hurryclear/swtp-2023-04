package com.swtp4.backend.UniDataControllerTests;

import com.swtp4.backend.repositories.MajorUniRepository;
import com.swtp4.backend.repositories.ModuleUniRepository;
import com.swtp4.backend.repositories.entities.MajorUniEntity;
import com.swtp4.backend.repositories.entities.ModuleUniEntity;
import com.swtp4.backend.services.UniDataService;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UniDataControllerIT {

    private UniDataService uniDataService;
    private MockMvc mockMvc;
    private MajorUniRepository majorUniRepository;
    private ModuleUniRepository moduleUniRepository;

    @Autowired
    public UniDataControllerIT(
            UniDataService uniDataService,
            MockMvc mockMvc,
            MajorUniRepository majorUniRepository,
            ModuleUniRepository moduleUniRepository
    ) {
        this.uniDataService = uniDataService;
        this.mockMvc = mockMvc;
        this.majorUniRepository = majorUniRepository;
        this.moduleUniRepository = moduleUniRepository;
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void testThatUpdateUniDataSuccessfullyReturnsHttpStatus420AndSavesMissingUniData() throws Exception{
        String testUniDataJson = UniDataTestData.createTestUniDataJsonA();
        mockMvc.perform(
                MockMvcRequestBuilders.put("/unidata/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUniDataJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
        //MajorUniEntity Check
        Optional<MajorUniEntity> resultMajorEntity = Optional.ofNullable(majorUniRepository.findByName("B.Sc. Informatik"));
        assertThat(resultMajorEntity).isPresent();
        assertThat(resultMajorEntity.get().getName()).isEqualTo("B.Sc. Informatik");
        //ModuleUniEntity Check
        Optional<ModuleUniEntity> resultModuleEntity = Optional.ofNullable(moduleUniRepository.findByName("Programmierparadigmen"));
        assertThat(resultModuleEntity).isPresent();
        assertThat(resultModuleEntity.get().getNumber()).isEqualTo("10-201-2005-2");
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void testThatUpdateUniDataSuccessfullyReturnsHttpStatus420AndUpdatesExistingUniData() throws Exception {
        ModuleUniEntity testModuleA = UniDataTestData.createTestModuleUniEntityA();
        ModuleUniEntity testModuleB = UniDataTestData.createTestModuleUniEntityB();
        ModuleUniEntity testModuleC = UniDataTestData.createTestModuleUniEntityC();
        majorUniRepository.saveAll(Arrays.asList(testModuleA.getMajorUniEntity(), testModuleC.getMajorUniEntity()));
        moduleUniRepository.saveAll(Arrays.asList(testModuleA, testModuleB, testModuleC));
        String testUniDataJson = UniDataTestData.createTestUniDataJsonA();
        mockMvc.perform(
                MockMvcRequestBuilders.put("/unidata/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUniDataJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
        //ModuleUniEntityA - Name should change
        Optional<ModuleUniEntity> existingModuleUniEntityA = Optional.ofNullable(moduleUniRepository.findByNumber("10-201-2001-1"));
        assertThat(existingModuleUniEntityA).isPresent();
        assertThat(existingModuleUniEntityA.get().getName()).isEqualTo("Algorithmen und Datenstrukturen 1");
        //ModuleUniEntityB - Name should change
        Optional<ModuleUniEntity> existingModuleUniEntityB = Optional.ofNullable(moduleUniRepository.findByNumber("10-201-2006-1"));
        assertThat(existingModuleUniEntityB).isPresent();
        assertThat(existingModuleUniEntityB.get().getName()).isEqualTo("Grundlagen der Technischen Informatik 1");
        //ModuleUniEntityC - Major should change
        Optional<ModuleUniEntity> existingModuleUniEntityC = Optional.ofNullable(moduleUniRepository.findByNumber("10-201-2004"));
        assertThat(existingModuleUniEntityC).isPresent();
        assertThat(existingModuleUniEntityC.get().getMajorUniEntity().getName()).isEqualTo("B.Sc. Informatik");
    }
}
