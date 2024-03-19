package com.swtp4.backend.ApplicationControllerTests;

import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.ModuleBlockRepository;
import com.swtp4.backend.repositories.ModuleRelationRepository;
import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.services.PDFService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
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

@ExtendWith(MockitoExtension.class)
class PDFServiceTests {

    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private ModuleBlockRepository moduleBlockRepository;
    @Mock
    private ModuleRelationRepository moduleRelationRepository;
    @InjectMocks
    private PDFService pdfService;


    @Test
    public void whenSaveModulePDFsGetsEmptyFile_ItDoesNotSave_NotThrowsException() {
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
    public void whenSaveModulePDFsGetsNullFile_itDoesNotSave_NotThrowsException() {
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
    public void whenGetModulePDF_NonExistingFilePath_ThrowsFileNotFoundException() {
        String nonExistingFilePath = "/path/to/non/existing/file.pdf";

        FileNotFoundException exception = assertThrows(FileNotFoundException.class, () -> {
            Resource resource = pdfService.getModulePDF(nonExistingFilePath);
        });

        assertEquals("File not found", exception.getMessage(), "Exception message should indicate file not found");
    }


    @Test
    public void whenGeneratePDFForApplication_GetsExistingApplicationID_ReturnsPDFResource() throws IOException {
        // Mock application entity
        ApplicationEntity mockEntity = new ApplicationEntity();
        mockEntity.setApplicationKeyClass(new ApplicationKeyClass("123", "Employee"));
        mockEntity.setStatus("Pending");
        mockEntity.setUniversityName("Test University");
        mockEntity.setStudentMajor("Computer Science");
        mockEntity.setUniMajor("Software Engineering");
        mockEntity.setDateOfSubmission(new Date()); // Set a non-null submission date
        mockEntity.setDateLastEdited(new Date()); // Set a non-null last edited date

        // Provide a valid UUID string instead of "123"
        String validUUID = "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6";
        mockEntity.setApplicationKeyClass(new ApplicationKeyClass(validUUID, "Employee"));

        // Mock application repository behavior
        when(applicationRepository.findById(any())).thenReturn(Optional.of(mockEntity));

        // Generate PDF with the valid UUID
        Resource pdfResource = pdfService.generatePDFForApplication(validUUID);

        // Check if PDF bytes are generated
        assertNotNull(pdfResource, "PDF-Resource should not be null");
    }



    @Test
    public void whenGeneratePDFForApplication_NonExistingApplicationId_ThrowsResourceNotFoundException() {
        // Mocking behavior for application not found
        when(applicationRepository.findById(any())).thenReturn(Optional.empty());

        // Check if ResourceNotFoundException is thrown
        assertThrows(ResourceNotFoundException.class, () -> pdfService.generatePDFForApplication("123"));
    }



    @Test
    public void whenSaveModulePDFsEncountersError_ThrowsRuntimeException() {
        // Preparation: Simulating a file whose saving encounters an error
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        try {
            when(file.getBytes()).thenThrow(IOException.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Map<String, MultipartFile> fileMap = new HashMap<>();
        fileMap.put("file-2:2", file);

        HashMap<String, String> filePaths = new HashMap<>();
        filePaths.put("file-2:2", "/invalid/path/file.pdf");

        // Action and Assertion: Ensure a RuntimeException is thrown
        assertThrows(RuntimeException.class, () -> pdfService.saveModulePDFs(fileMap, filePaths), "Expected to throw RuntimeException due to save error");
    }

//    @Test
//    void formatId_ValidUUID_ReturnsFormattedUUID() {
//        // Arrange
//        String inputUUID = "71dbb8b7-7085-4416-a2c0-4f1c65c8b166";
//        String expected = "71db-b8b7-7085-4416-a2c0-4f1c-65c8-b166";
//
//        // Act
//        String result = PDFService.formatUUID(inputUUID);
//
//        // Assert
//        assertEquals(expected, result, "Die formatierte UUID entspricht nicht dem erwarteten Format.");
//    }
//
//    @Test
//    void formatId_InvalidUUID_ThrowsIllegalArgumentException() {
//        // Arrange
//        String invalidUUID = "123"; // Ungültige UUID-Länge
//
//        // Assert
//        assertThrows(IllegalArgumentException.class, () -> {
//            // Act
//            PDFService.formatUUID(invalidUUID);
//        }, "Eine ungültige UUID sollte eine IllegalArgumentException auslösen.");
//    }
//
//    @Test
//    void formatId_NullUUID_ThrowsIllegalArgumentException() {
//        // Assert
//        assertThrows(IllegalArgumentException.class, () -> {
//            // Act
//            PDFService.formatUUID(null);
//        }, "Eine null UUID sollte eine IllegalArgumentException auslösen.");
//    }
}
