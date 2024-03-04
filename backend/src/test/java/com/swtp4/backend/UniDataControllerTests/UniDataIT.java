package com.swtp4.backend.UniDataControllerTests;

import com.swtp4.backend.repositories.MajorUniRepository;
import com.swtp4.backend.repositories.ModuleUniRepository;
import com.swtp4.backend.repositories.entities.MajorUniEntity;
import com.swtp4.backend.repositories.entities.ModuleUniEntity;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class UniDataIT {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MajorUniRepository majorUniRepository;

    @Autowired
    private ModuleUniRepository moduleUniRepository;

    @BeforeTestClass
    public void setupTestData() {
        // Clear the database before each test
        majorUniRepository.deleteAll();
        moduleUniRepository.deleteAll();
    }

    @Test
    @Sql("/MultipleMajorsAndModules.sql")
    public void getVisibleMajors_ShouldReturnOnlyVisibleMajors() throws Exception {
        mockMvc.perform(get("/unidata/getMajors"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.*", hasSize(1)))
                .andExpect(jsonPath("$.courses[0].name", is("B.Sc. Informatik")));
    }

    @Test
    @Sql("/MultipleMajorsAndModules.sql")
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void getAllMajors_ShouldReturnAllMajorsAndVisibility() throws Exception {
        mockMvc.perform(get("/unidata/getAllMajors"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.courses.*", hasSize(2)))
                .andExpect(jsonPath("$.courses[*].name", containsInAnyOrder("B.Sc. Informatik", "M.Sc. Informatik")))
                .andExpect(jsonPath("$.courses[*].visibleForStudents", containsInAnyOrder(true, false)));
    }

    @Test
    @Sql("/MultipleMajorsAndModules.sql")
    public void getModulesByMajor_ShouldReturnVisibleModules() throws Exception {
        String majorName = "B.Sc. Informatik";
        mockMvc.perform(get("/unidata/getModules").param("majorName", majorName))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(majorName)))
                .andExpect(jsonPath("$.modules", hasSize(1)))
                .andExpect(jsonPath("$.modules[0].name", is("Programmierparadigmen")));
    }

    @Test
    @Sql("/MultipleMajorsAndModules.sql")
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void getAllModulesByMajor_ShouldReturnAllModulesAndVisibility() throws Exception {
        String majorName = "B.Sc. Informatik";
        mockMvc.perform(get("/unidata/getAllModules").param("majorName", majorName))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(majorName)))
                .andExpect(jsonPath("$.modules", hasSize(2)))
                .andExpect(jsonPath("$.modules[*].name", containsInAnyOrder("Einführung Programmierung", "Programmierparadigmen")))
                .andExpect(jsonPath("$.modules[*].visibleForStudents", containsInAnyOrder(false, true)));
    }

    @Test
    @Sql("/MultipleMajorsAndModules.sql")
    public void getModulesByNonExistentMajor_ShouldReturn404AndErrorMessage() throws Exception {
        String majorName = "invalid_MajorName";
        mockMvc.perform(get("/unidata/getModules").param("majorName", majorName))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage", is("Major not found")));
    }

    @Test
    @Sql("/MultipleMajorsAndModules.sql")
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void getAllModulesByNonExistentMajor_ShouldReturn404AndErrorMessage() throws Exception {
        String majorName = "invalid_MajorName";
        mockMvc.perform(get("/unidata/getAllModules").param("majorName", majorName))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.errorMessage", is("Major not found")));
    }
}
