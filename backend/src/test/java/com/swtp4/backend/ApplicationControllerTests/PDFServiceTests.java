package com.swtp4.backend.ApplicationControllerTests;

import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.services.PDFService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PDFServiceTests {


    @Mock
    private ApplicationRepository applicationRepository;

    private PDFService pdfService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        pdfService = new PDFService(applicationRepository);
    }


    @Test
    public void whenSaveModulePDFsGetsEmptyFile_ItDoesThrowException() {
        // Mocked MultipartFiles (empty file)
        Map<String, MultipartFile> fileMap = new HashMap<>();
        MultipartFile file1 = new MockMultipartFile("file-0:0", "filename1.pdf", "application/pdf", new byte[0]);
        fileMap.put("file-0:0", file1);

        // Mocked filePaths
        HashMap<String, String> filePaths = new HashMap<>();
        filePaths.put("file-0:0", "/applicationID/S-moduleStudentID1");

        // Test FileSave
        assertThrows(Exception.class, () -> pdfService.saveModulePDFs(fileMap, filePaths), "Empty file should throw a RuntimeException");
    }

    @Test
    public void whenSaveModulePDFsGetsNullFile_itDoesThrowException() {
        // Mocked MultipartFiles (null file)
        Map<String, MultipartFile> fileMap = new HashMap<>();
        fileMap.put("file-0:0", null);

        // Mocked filePaths
        HashMap<String, String> filePaths = new HashMap<>();
        filePaths.put("file-0:0", "/applicationID/S-moduleStudentID1");

        // Test FileSave
        assertThrows(Exception.class, () -> pdfService.saveModulePDFs(fileMap, filePaths), "Null file should cause an exception");
    }

    @Test
    public void whenGetModulePDFGetsExistingFilePath_ReturnsResourceSuccessfully() throws IOException {
        // Temporary file
        Path filePath = Files.createTempFile("testFile", ".pdf");
        Files.write(filePath, "pdf file content".getBytes());

        assertDoesNotThrow(() -> {
            Resource resource = pdfService.getModulePDF(filePath.toString());
            assertNotNull(resource, "Resource should not be null");
        }, "No exceptions should be thrown");

        // Clean up
        Files.delete(filePath);
    }

    @Test
    public void whenGetModulePDF_NonExistingFilePath_ThrowsFileNotFoundException() {
        String nonExistingFilePath = "/path/to/non/existing/file.pdf";

        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> {
            Resource resource = pdfService.getModulePDF(nonExistingFilePath);
        });

        assertEquals("File not found", exception.getMessage(), "Exception message should indicate file not found");
    }


//     @Test
//    void generatePDFForApplication() throws IOException {
//        // Mock application entity
//        ApplicationEntity mockEntity = new ApplicationEntity();
//        mockEntity.setApplicationKeyClass(new ApplicationKeyClass("Employee", "123"));
//        mockEntity.setStatus("Pending");
//        mockEntity.setUniversityName("Test University");
//        mockEntity.setStudentMajor("Computer Science");
//        mockEntity.setUniMajor("Software Engineering");
//        mockEntity.setDateOfSubmission(new Date()); // Set a non-null submission date
//        mockEntity.setDateLastEdited(new Date()); // Set a non-null last edited date
//
//        // Mock application repository behavior
//        when(applicationRepository.findById(any())).thenReturn(Optional.of(mockEntity));
//
//        byte[] pdfBytes = pdfService.generatePDFForApplication("123");
//
//        // Check if PDF bytes are generated
//        assertNotNull(pdfBytes);
//
//        // Check if PDF bytes are not empty
//        assertTrue(pdfBytes.length > 0);
//    }
//
//
//
//    @Test
//    void generatePDFForApplication_NotFound() {
//        // Mocking behavior for application not found
//        when(applicationRepository.findById(any())).thenReturn(Optional.empty());
//
//        // Check if ResourceNotFoundException is thrown
//        assertThrows(ResourceNotFoundException.class, () -> pdfService.generatePDFForApplication("123"));
//    }


}
