package com.swtp4.backend.controller;

import com.swtp4.backend.repositories.dto.ApplicationDto;
import com.swtp4.backend.repositories.dto.UniModuleDto;
import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.PDFService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(path = "/application")
public class ApplicationController {

    private ApplicationService applicationService;
    private PDFService pdfService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, PDFService pdfService) {
        this.applicationService = applicationService;
        this.pdfService = pdfService;
    }

    //changed to student controler
    @PostMapping("/saveApplication")
    public ResponseEntity<?> saveApplication(@RequestBody ApplicationDto applicationDTO){
        log.info("Received ApplicationDto: {}", applicationDTO);
        applicationService.save(applicationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //test
    @PostMapping("/post")
    public String sayHello(String message) {
        return "Request accepted and message is: " + message;
    }

    //test
    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from my test get mapping";
    }

    // get all applications
    @GetMapping("/getApplication")
    public List<ApplicationEntity> getAllApplications() {
        return applicationService.getAllApplications();
    }


    @GetMapping("/getApplication/{status}")
    public List<ApplicationEntity> getApplicationsByStatus(@PathVariable("status") String status) {
        return applicationService.getApplicationsByStatus(status);
    }

    @GetMapping("/getApplication/{major}")
    public List<ApplicationEntity> getApplicationsByMajor(@PathVariable("major") String major) {
        return applicationService.getApplicationsByMajor(major);
    }

    @GetMapping("/getApplication/{universityName}")
    public List<ApplicationEntity> getApplicationsByUniversity(@PathVariable("universityName") String universityName) {
        return applicationService.getApplicationsByUniversityName(universityName);
    }

    @GetMapping("/getApplication/{dateOfSubmission}")
    public List<ApplicationEntity> getApplicationsByDateOfSubmission(@PathVariable("dateOfSubmission") String dateOfSubmission) {
       return  applicationService.getApplicationsByDateOfSubmission(dateOfSubmission);
    }

    @PostMapping("/test")
    public ResponseEntity<UniModuleDto> testOfficeEndpoint(@RequestBody UniModuleDto uniModuleDto){
        return new ResponseEntity<>(uniModuleDto, HttpStatus.OK);
    }

}
