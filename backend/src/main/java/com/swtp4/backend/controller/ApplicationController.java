package com.swtp4.backend.controller;

import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/application")
public class ApplicationController {

    private ApplicationService applicationService;
    private PDFService pdfService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, PDFService pdfService) {
        this.applicationService = applicationService;
        this.pdfService = pdfService;
    }

    @PostMapping
    public void addApplication(@RequestBody ApplicationEntity application){
        //JSOn cut - application attribute zur applicationEntity
        applicationService.save(application);
        //Rest - Module, PDF-Dateien
    }
}