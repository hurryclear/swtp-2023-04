package com.swtp4.backend.ApplicationControllerTests;

import com.swtp4.backend.services.PDFService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PDFServiceTests {
    @InjectMocks
    private PDFService pdfService;

    @Test
    public void whenSaveModulePDFsHaveValidData_ItSuccessfulSaves() throws IOException {
        // Mocked MultipartFiles
        Map<String, MultipartFile> fileMap = new HashMap<>();
        MultipartFile file1 = new MockMultipartFile("file-0:0", "filename1.pdf", "application/pdf", "pdf file content".getBytes());
        fileMap.put("file-0:0", file1);

        // Mocked filePaths
        HashMap<String, String> filePaths = new HashMap<>();
        filePaths.put("file-0:0", "/applicationID/S-moduleStudentID1");

        // Test FileSave
        assertDoesNotThrow(() -> pdfService.saveModulePDFs(fileMap, filePaths), "File should be saved successfully");

        // Test if file is locally saved at specified path
        Path path = Paths.get("/app/pdf-files" + "/applicationID/S-moduleStudentID1");
        assertTrue(Files.exists(path), "File should exist at the specified path: " + path);
    }

    @Test
    public void whenSaveModulePDFsGetsEmptyFile_ItDoesNotSaveWithoutRuntimeException() {
        // Mocked MultipartFiles (empty file)
        Map<String, MultipartFile> fileMap = new HashMap<>();
        MultipartFile file1 = new MockMultipartFile("file-0:0", "filename1.pdf", "application/pdf", new byte[0]);
        fileMap.put("file-0:0", file1);

        // Mocked filePaths
        HashMap<String, String> filePaths = new HashMap<>();
        filePaths.put("file-0:0", "/applicationID/S-moduleStudentID1");

        // Test FileSave
        assertDoesNotThrow(() -> pdfService.saveModulePDFs(fileMap, filePaths), "Empty file should not throw a RuntimeException");
    }

    @Test
    public void whenSaveModulePDFsGetsNullFile_itDoesNotSaveWithoutRuntimeException() {
        // Mocked MultipartFiles (null file)
        Map<String, MultipartFile> fileMap = new HashMap<>();
        fileMap.put("file-0:0", null);

        // Mocked filePaths
        HashMap<String, String> filePaths = new HashMap<>();
        filePaths.put("file-0:0", "/applicationID/S-moduleStudentID1");

        // Test FileSave
        assertDoesNotThrow(() -> pdfService.saveModulePDFs(fileMap, filePaths), "Null file should not cause an exception");
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
    public void whenGetModulePDF_NonExistingFilePath_ThrowsRuntimeException() {
        String nonExistingFilePath = "/path/to/non/existing/file.pdf";

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Resource resource = pdfService.getModulePDF(nonExistingFilePath);
        });

        assertEquals("File not found", exception.getMessage(), "Exception message should indicate file not found");
    }
}
