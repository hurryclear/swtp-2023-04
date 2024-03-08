package com.swtp4.backend.controller;

import com.swtp4.backend.repositories.applicationDtos.SubmittedApplicationDto;
import com.swtp4.backend.repositories.dto.UniModuleDto;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.PDFService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

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

    @PostMapping("/submitApplication")
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

        HashMap<String, String> file_paths = applicationService.saveSubmitted(submittedApplicationDto);

        // TODO: here you can implement pdf saving by using multipart field names and provided paths in file_paths

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
