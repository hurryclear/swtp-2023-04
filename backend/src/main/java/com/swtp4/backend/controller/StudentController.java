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

//    @PostMapping("/submitApplication")
//    public ResponseEntity<?> saveApplication(@RequestBody ApplicationDto applicationDTO){
////        log.info("Received ApplicationDto: {}", applicationDTO);
//        applicationService.save(applicationDTO);
//        return new ResponseEntity<>(HttpStatus.CREATED);
//    }

    @PostMapping("/test")
    public ResponseEntity<UniModuleDto> testStudentEndpoint(@RequestBody UniModuleDto uniModuleDto){
        return new ResponseEntity<>(uniModuleDto, HttpStatus.OK);
    }

    //@PostMapping("/submitApplication")
    @RequestMapping(path = "/submitApplication", method = POST, consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<?> submitApplication(
            @RequestParam Map<String, MultipartFile> fileMap,
            @RequestPart("form") SubmittedApplicationDto submittedApplicationDto) {
        log.info("SUBMITTED_APPLICATION: {}", submittedApplicationDto);

        // Log file field names
        fileMap.forEach((key, value) -> {
            try {
                System.out.println("Received file with key: " + key + "\n" + new String(value.getBytes(), StandardCharsets.UTF_8));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        //System.out.println("Received JSON: " + submittedApplicationDto);
        ApplicationIDWithFilePaths applicationIDWithFilePaths = applicationService.saveSubmitted(submittedApplicationDto);

        ApplicationIdDto applicationID = new ApplicationIdDto(applicationIDWithFilePaths.getApplicationID());
        HashMap<String, String> file_paths = applicationIDWithFilePaths.getFilesAndPaths();

        // TODO: here you can implement pdf saving by using multipart field names and provided paths in file_paths
        pdfService.saveModulePDFs(fileMap, file_paths);
        return new ResponseEntity<>(applicationID, HttpStatus.CREATED);
    }

    @GetMapping("/reviewApplication")
    public ResponseEntity<?> reviewApplication(@RequestParam String applicationID) {
        ReviewApplicationDto applicationDto = applicationService.getReviewApplication(applicationID);
        return new ResponseEntity<>(applicationDto, HttpStatus.OK);
    }

    @GetMapping("/getPdfSummary")
    public ResponseEntity<?> downloadApplicationPDF(@RequestParam String formId) {
        try {
            return pdfService.generatePDFForApplication(formId);
        } catch (IOException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }
}
