package com.swtp4.backend.controller;

import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.applicationDtos.EditedApplicationDto;
import com.swtp4.backend.repositories.applicationDtos.EntireOriginalAndEditedApplicationDto;
import com.swtp4.backend.repositories.applicationDtos.ReviewApplicationDto;
import com.swtp4.backend.repositories.applicationDtos.SubmittedApplicationDto;
import com.swtp4.backend.repositories.dto.ApplicationIDWithFilePaths;
import com.swtp4.backend.repositories.dto.ApplicationIdDto;
import com.swtp4.backend.repositories.dto.UniModuleDto;
import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.entities.ModuleBlockEntity;
import com.swtp4.backend.repositories.entities.ModuleStudentEntity;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.PDFService;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(path = "/student")
public class StudentController {
    private ApplicationService applicationService;
    private PDFService pdfService;

    @Autowired
    public StudentController(ApplicationService applicationService, PDFService pdfService){
        this.applicationService = applicationService;
        this.pdfService = pdfService;
    }

    /**
     *
     * @param fileMap
     * @param submittedApplicationDto
     * return ResponseEntity<?>
     * ResponseEntity in Spring includes status code, headers and body
     * ? in wildcard means the body could be any type, or just no body, i.e. just return status code
     *
     *
     */
    @RequestMapping(path = "/submitApplication", method = POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> submitApplication(
            @RequestParam Map<String, MultipartFile> fileMap,
            // Find the part which name is "form" in the multipart and deserialize it into a submittedApplicationDto (a object)
            @RequestPart("form") SubmittedApplicationDto submittedApplicationDto) {
        log.info("SUBMITTED_APPLICATION: {}", submittedApplicationDto);

        //save submitted application and receive applicationID and file paths
        ApplicationIDWithFilePaths applicationIDWithFilePaths = applicationService.saveSubmitted(submittedApplicationDto);
        ApplicationIdDto applicationID = new ApplicationIdDto(applicationIDWithFilePaths.getApplicationID());
        HashMap<String, String> file_paths = applicationIDWithFilePaths.getFilesAndPaths();

        // pdf file of each module is saved under right path
        pdfService.saveModulePDFs(fileMap, file_paths);
        return new ResponseEntity<>(applicationID, HttpStatus.CREATED); // applicationID will be returned to student, so can be used for tracking
    }

    // get all necessary details for reviewing an application as student
    @GetMapping("/reviewApplication")
    public ResponseEntity<?> reviewApplication(@RequestParam String applicationID) {
        ReviewApplicationDto applicationDto = applicationService.getReviewApplication(applicationID);
        return new ResponseEntity<>(applicationDto, HttpStatus.OK);
    }

    // get pdf summary for the student with all application details of the current edited version
    @GetMapping("/getPdfSummary")
    public ResponseEntity<Resource> downloadApplicationPDF(@RequestParam String applicationId) {
        try {
            Resource pdfResource = pdfService.generatePDFForApplication(applicationId);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + applicationId + ".pdf\"")
                    .body(pdfResource);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }
}
