package com.swtp4.backend.UniDataControllerTests;

import com.swtp4.backend.repositories.MajorUniRepository;
import com.swtp4.backend.repositories.ModuleUniRepository;
import com.swtp4.backend.repositories.entities.MajorUniEntity;
import com.swtp4.backend.repositories.entities.ModuleUniEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UniDataIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MajorUniRepository majorUniRepository;

    @Autowired
    private ModuleUniRepository moduleUniRepository;

    @BeforeEach
    public void setupTestData() {
        // Create and save test data
        MajorUniEntity majorA = UniDataTestData.createTestMajorUniEntityA();
        MajorUniEntity majorB = UniDataTestData.createTestMajorUniEntityB();
        majorUniRepository.save(majorA);
        majorUniRepository.save(majorB);

        ModuleUniEntity moduleA = UniDataTestData.createTestModuleUniEntityA();
        ModuleUniEntity moduleB = UniDataTestData.createTestModuleUniEntityB();
        ModuleUniEntity moduleC = UniDataTestData.createTestModuleUniEntityC();
        moduleUniRepository.save(moduleA);
        moduleUniRepository.save(moduleB);
        moduleUniRepository.save(moduleC);
    }

    @AfterEach
    public void cleanup(){
        majorUniRepository.deleteAll();
        moduleUniRepository.deleteAll();
    }
    @Test
    public void getMajors_ShouldReturnListOfMajors() throws Exception {
        mockMvc.perform(get("/unidata/getMajors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courses", hasSize(2)))
                .andExpect(jsonPath("$.courses[*].name", containsInAnyOrder("B.Sc. Informatik", "M.Sc. Informatik")));
    }

    @Test
    public void getModulesByMajor_ShouldReturnModulesForGivenMajor() throws Exception {
        String majorName = "B.Sc. Informatik";
        mockMvc.perform(get("/unidata/getModules").param("majorName", majorName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(majorName)))
                .andExpect(jsonPath("$.modules", hasSize(2)))
                .andExpect(jsonPath("$.modules[*].name", containsInAnyOrder(
                        "Datorithmen und Algenstrukturen 1",
                        "Techniklagen der grundlegenden Informatik 1"
                )));
    }

    @Test
    public void getModulesByMajor_ShouldReturnNotFoundWhenMajorDoesNotExist() throws Exception {
        String majorName = "Nonexistent Major";
        mockMvc.perform(get("/unidata/getModules").param("majorName", majorName))
                .andExpect(status().isNotFound());
    }
}
