package com.swtp4.backend.controller;

import com.swtp4.backend.repositories.dto.ApplicationDto;
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

@Slf4j
@RestController
@RequestMapping(path = "/application")
public class ApplicationController {

    private ApplicationService applicationService;
    private PDFService pdfService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, PDFService pdfService) {
        this.applicationService = applicationService;
        this.pdfService = pdfService;
    }

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

    @GetMapping("/getApplication")
    public List<ApplicationEntity> getAllApplication() {
        return applicationService.getAllApplications();
    }

    @GetMapping("/getApplication/{id}")
    public void getApplicationByID(@PathVariable("id") ApplicationKeyClass id) {
        applicationService.getApplicationByID(id);
    }

//    @GetMapping
//    public Iterable<ApplicationEntity> findAllApplication() {
//        return applicationService.findAllApplication();
//    }
//
//    @GetMapping("/{university}")
//    public void findByUniversity(@PathVariable("university") String university) {
//        applicationService.findAllByUniversity(university);
//    }
//    @GetMapping("/{major}")
//    public void findByMajor(@PathVariable("major") String major) {
//        applicationService.findAllByMajor(major);
//    }
}
