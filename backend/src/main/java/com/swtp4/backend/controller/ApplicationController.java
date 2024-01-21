package com.swtp4.backend.controller;

import com.swtp4.backend.repositories.dto.ApplicationDto;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
        applicationService.save(applicationDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
