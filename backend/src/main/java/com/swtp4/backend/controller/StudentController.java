package com.swtp4.backend.controller;

import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.PDFService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/student")
public class StudentController {
    private ApplicationService applicationService;
    private PDFService pdfService;

    @Autowired
    public StudentController(ApplicationService applicationService, PDFService pdfService){
        this.applicationService = applicationService;
        this.pdfService = pdfService;
    }



}
