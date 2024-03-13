package com.swtp4.backend.ApplicationControllerTests;

import com.swtp4.backend.controller.ApplicationController;
import com.swtp4.backend.services.PDFService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class PDFControllerTests {

    PDFService pdfService;
    private MockMvc mockMvc;

    @Autowired
    public PDFControllerTests(PDFService pdfService,
                              MockMvc mockMvc) {
        this.pdfService = pdfService;
        this.mockMvc = mockMvc;
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void whenGetPdfGetsCorrectFilePath_ItSuccessfullyReturnsHttpStatus200OkAndAPDF() throws Exception {
        byte[] bytes = "I bims eine PDF".getBytes();
        Path path = Paths.get("/app/pdf-files/1/1");
        Files.createDirectories(path.getParent());
        Files.write(path, bytes);

        try {
            mockMvc.perform(
                    MockMvcRequestBuilders.get("/application/getModulePDF")
                            .param("filePath", "/1/1")
            )
                    .andExpect(
                            MockMvcResultMatchers.status().isOk())
                    .andExpect(
                            MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_PDF))
                    .andExpect(
                            MockMvcResultMatchers.header().string("Content-Disposition", "attachment; filepath = /1/1")
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Files.delete(path);
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"OFFICE"})
    public void whenGetPdfGetsWrongFilePath_ItReturnsHttpStatus404NotFound() throws Exception {
        try {
            mockMvc.perform(
                            MockMvcRequestBuilders.get("/application/getModulePDF")
                                    .param("filePath", "/biba/buba")
                    )
                    .andExpect(
                            MockMvcResultMatchers.status().isNotFound()
                    );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
