package com.swtp4.backend.controller;

import com.swtp4.backend.repositories.dto.ApplicationDto;
import com.swtp4.backend.repositories.dto.UniModuleDto;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.PDFService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@Slf4j
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

    @PostMapping("/submitApplication")
    public ResponseEntity<?> saveApplication(@RequestBody ApplicationDto applicationDTO){
//        log.info("Received ApplicationDto: {}", applicationDTO);
        applicationService.save(applicationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/test")
    public ResponseEntity<UniModuleDto> testStudentEndpoint(@RequestBody UniModuleDto uniModuleDto){
        return new ResponseEntity<>(uniModuleDto, HttpStatus.OK);
    }

}