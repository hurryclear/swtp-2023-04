package com.swtp4.backend.controller;

import com.swtp4.backend.repositories.dto.ApplicationsDto;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.PDFService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public void saveApplication(@RequestBody ApplicationsDto applicationsDTO){
        applicationService.save(applicationsDTO);
    }
}
