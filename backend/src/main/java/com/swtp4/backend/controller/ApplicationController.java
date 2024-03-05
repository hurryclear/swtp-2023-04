package com.swtp4.backend.controller;

import ch.qos.logback.core.read.ListAppender;
import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.dto.ApplicationDto;
import com.swtp4.backend.repositories.dto.UniModuleDto;
import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.PDFService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.weaver.ast.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@CrossOrigin
@RequestMapping(path = "/application")
public class ApplicationController {
    private ApplicationRepository applicationRepository; // test, delete later
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
    public String sayHello1(String message) {
        return "Request accepted and message is: " + message;
    }

    @GetMapping("/getApplications")
    public List<ApplicationEntity> getAllApplications() {
        return applicationService.getAllApplications();
    }
//    @GetMapping("/getApplicationByIdAndCreator")
//    public ApplicationEntity getApplicationByIdAndCreator(@RequestParam("id") UUID id, @RequestParam("creator") String creator){
//        return applicationService.getApplicationByIdAndCreator(id, creator);
//    }

    @GetMapping("/getApplicationsByStatus")
    public List<ApplicationEntity> getApplicationsByStatus(@RequestParam("status") String status) {
        return applicationService.getApplicationsByStatus(status);
    }

    @GetMapping("/getApplicationsByMajor")
    public List<ApplicationEntity> getApplicationsByMajor(@RequestParam("major") String major) {
        return applicationService.getApplicationsByMajor(major);
    }

    @GetMapping("/getApplicationsByUniversity")
    public List<ApplicationEntity> getApplicationsByUniversity(@RequestParam("universityName") String universityName) {
        return applicationService.getApplicationsByUniversityName(universityName);
    }

    @GetMapping("/getApplicationsByDateOfSubmission")
    public List<ApplicationEntity> getApplicationsByDateOfSubmission(@RequestParam("dateOfSubmission") String dateOfSubmission) {
       return  applicationService.getApplicationsByDateOfSubmission(dateOfSubmission);
    }

    // still have to adjust
    @GetMapping("/getApplicationsByDateOfSubmissionBefore")
    public List<ApplicationEntity> getApplicationsByDateOfSubmissionBefore(@RequestParam("dateOfSubmission") String dateOfSubmission) {
        return  applicationService.getApplicationsByDateOfSubmissionBefore(dateOfSubmission);
    }
    @GetMapping("/getApplicationsByDateOfSubmissionAfter")
    public List<ApplicationEntity> getApplicationsByDateOfSubmissionAfter(@RequestParam("dateOfSubmission") String dateOfSubmission) {
        return applicationService.getApplicationsByDateOfSubmissionAfter(dateOfSubmission);
    }
    @PostMapping("/test")
    public ResponseEntity<UniModuleDto> testOfficeEndpoint(@RequestBody UniModuleDto uniModuleDto){
        return new ResponseEntity<>(uniModuleDto, HttpStatus.OK);
    }

}
