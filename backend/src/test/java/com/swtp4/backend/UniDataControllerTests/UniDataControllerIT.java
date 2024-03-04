package com.swtp4.backend.UniDataControllerTests;

import com.swtp4.backend.repositories.MajorUniRepository;
import com.swtp4.backend.repositories.ModuleUniRepository;
import com.swtp4.backend.repositories.entities.MajorUniEntity;
import com.swtp4.backend.repositories.entities.ModuleUniEntity;
import com.swtp4.backend.services.UniDataService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("integration")
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

    @BeforeTestClass
    public void setupTestData() {
        // Clear the database before each test
        majorUniRepository.deleteAll();
        moduleUniRepository.deleteAll();
    }

    @Test
    @Sql("/MultipleMajorsAndModules.sql")
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void updateWithValidData_shouldUpdateVisibility() throws Exception {
        String jsonBody = UniDataTestData.createUniDataUpdateJsonBody();

        mockMvc.perform(put("/unidata/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated());

        // MajorUniEntity Check
        Optional<MajorUniEntity> updatedMaster = majorUniRepository.findByName("M.Sc. Informatik");
        assertThat(updatedMaster).isPresent();
        assertThat(updatedMaster.get().getVisibleChoice()).isTrue();

        // ModuleUniEntity Check
        Optional<ModuleUniEntity> updatedMasterModule = moduleUniRepository.findByNumberAndNameAndMajorUniEntity("", "Kermodul (Wahlpflichtfach)", updatedMaster.get());
        assertThat(updatedMasterModule).isPresent();
        assertThat(updatedMasterModule.get().getVisibleChoice()).isTrue();

        // MajorUniEntity Check
        Optional<MajorUniEntity> updatedBachelor = majorUniRepository.findByName("B.Sc. Informatik");
        assertThat(updatedBachelor).isPresent();
        assertThat(updatedBachelor.get().getVisibleChoice()).isFalse();

        // ModuleUniEntity Check
        Optional<ModuleUniEntity> updatedBachelorModule = moduleUniRepository.findByNumberAndNameAndMajorUniEntity("10-201-2012", "Einführung Programmierung", updatedBachelor.get());
        assertThat(updatedBachelorModule).isPresent();
        assertThat(updatedBachelorModule.get().getVisibleChoice()).isFalse();

        // ModuleUniEntity Check
        Optional<ModuleUniEntity> inactiveBachelorModule = moduleUniRepository.findByNumberAndNameAndMajorUniEntity("10-201-2012", "Einführung Programmierung", updatedBachelor.get());
        assertThat(inactiveBachelorModule).isPresent();
        assertThat(inactiveBachelorModule.get().getVisibleChoice()).isFalse();
    }

    @Test
    @Sql("/MultipleMajorsAndModules.sql")
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void updateNonExisting_shouldCreateNewEntities() throws Exception {
        String jsonBody = UniDataTestData.createUniDataUpdateJsonBody();

        mockMvc.perform(put("/unidata/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonBody))
                .andExpect(status().isCreated());

        // MajorUniEntity Check
        Optional<MajorUniEntity> updatedMaster = majorUniRepository.findByName("M.Sc. Informatik");
        assertThat(updatedMaster).isPresent();
        assertThat(updatedMaster.get().getVisibleChoice()).isTrue();

        // ModuleUniEntity Check
        Optional<ModuleUniEntity> newMasterModule = moduleUniRepository.findByNumberAndNameAndMajorUniEntity("187-123-123-2", "Master-Coding", updatedMaster.get());
        assertThat(newMasterModule).isPresent();
        assertThat(newMasterModule.get().getVisibleChoice()).isTrue();

        // MajorUniEntity Check
        Optional<MajorUniEntity> newBachelor = majorUniRepository.findByName("B.Sc. Bio-Informatik");
        assertThat(newBachelor).isPresent();
        assertThat(newBachelor.get().getVisibleChoice()).isTrue();

        // ModuleUniEntity Check
        Optional<ModuleUniEntity> newBachelorModule = moduleUniRepository.findByNumberAndNameAndMajorUniEntity("123-13-132", "Evolutionstheorie", newBachelor.get());
        assertThat(newBachelorModule).isPresent();
        assertThat(newBachelorModule.get().getVisibleChoice()).isTrue();
    }
}