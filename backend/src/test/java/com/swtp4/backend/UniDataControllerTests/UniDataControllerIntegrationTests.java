package com.swtp4.backend.UniDataControllerTests;

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

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UniDataControllerIntegrationTests {

    private UniDataService uniDataService;
    private MockMvc mockMvc;

    @Autowired
    public UniDataControllerIntegrationTests(
            UniDataService uniDataService,
            MockMvc mockMvc
    ) {
        this.uniDataService = uniDataService;
        this.mockMvc = mockMvc;
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void testThatUpdateUniDataSuccessfullyReturnsHttpStatus420WhenAuthorExists() throws Exception{
        String testUniDataJson = UniDataTestData.createTestUniDataJsonA();
        mockMvc.perform(
                MockMvcRequestBuilders.put("/unidata/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testUniDataJson)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }
}
