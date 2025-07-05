package com.swtp4.backend.controller;

import com.swtp4.backend.repositories.applicationDtos.EntireOriginalAndEditedApplicationDto;
import com.swtp4.backend.repositories.applicationDtos.OverviewApplicationDto;
import com.swtp4.backend.repositories.applicationDtos.EditedApplicationDto;
import com.swtp4.backend.repositories.dto.UniModuleDto;
import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.model.ApplicationPage;
import com.swtp4.backend.repositories.model.ApplicationSearchCriteria;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.PDFService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.util.List;

@Slf4j
@RestController // marks the class as a REST API controller, which returns JSON/XML files
@CrossOrigin
@RequestMapping(path = "/application")
public class ApplicationController {
    private ApplicationService applicationService;
    private PDFService pdfService;

    /**
     * Constructor injection through Autowired
     * this class has two dependencies: ApplicationService and PDFService
     * Without @Autowired you have to write new ApplicationService and new PDFService
     * but @Autowired will help us to create ApplicationService and PDFService bean and pass them to the constructor
     * so no need to create them manually
     */
    @Autowired
    public ApplicationController(ApplicationService applicationService, PDFService pdfService) {
        this.applicationService = applicationService;
        this.pdfService = pdfService;
    }

    // save the edited application in the database and update status of application and approval of modules properly
    @PutMapping("/saveEdited")
    @Transactional // If updating the status or approvals fails, all changes (e.g., status, approvals, application data) are rolled back.
    public ResponseEntity<?> saveApplication(@RequestBody EditedApplicationDto applicationDto){
        log.info("Received applicationDto: {}", applicationDto);
        applicationService.updateStatus(applicationDto.applicationID(), "edited");
        applicationService.updateApproval(applicationDto, List.of("formally rejected"));
        applicationService.updateApplication(applicationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // entire application gets formally rejected, updates application and module approvals properly
    @PutMapping("/formalRejection")
    @Transactional
    public ResponseEntity<?> formalRejectApplication(@RequestBody EditedApplicationDto applicationDto){
        log.info("Received ApplicationDto: {}", applicationDto);
        applicationService.statusFormalRejection(applicationDto);
        applicationService.updateApplication(applicationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // updates application and updates status so that Office Members cant edit it any longer, only Committee is able to edit it now.
    @PutMapping("/readyForApproval")
    @Transactional
    public ResponseEntity<?> readyForApproval(@RequestBody EditedApplicationDto applicationDto){
        log.info("Received applicationDto: {}", applicationDto);
        applicationService.updateStatus(applicationDto.applicationID(), "ready for approval");
        applicationService.updateApproval(applicationDto, List.of("formally rejected"));
        applicationService.updateApplication(applicationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // changes of Committee Members are saved and status and approvals of modules are updated
    @PreAuthorize("hasRole('COMMITTEE')")
    @PutMapping("/saveApproval")
    @Transactional
    public ResponseEntity<?> saveApproval(@RequestBody EditedApplicationDto applicationDto){
        applicationService.updateStatus(applicationDto.applicationID(), "edited approval");
        applicationService.updateApproval(applicationDto, List.of("accepted", "rejected"));
        applicationService.updateApplication(applicationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    // sends paged applications which are open or edited to the office overview page
    // frontend specifies sorting (default is after DateOfSubmission ASC)
    @GetMapping("/overviewOffice")
    public ResponseEntity<Page<OverviewApplicationDto>> getOverviewOffice(ApplicationPage applicationPage,
                                                                                ApplicationSearchCriteria applicationSearchCriteria) {
        Page<OverviewApplicationDto> resultPage = applicationService.getOverviewOffice(applicationPage, applicationSearchCriteria);
        return new ResponseEntity<>(resultPage, HttpStatus.OK);
    }

    // sends paged application which are open, edited, edited approval or ready for approval
    // frontend specifies sorting (default is after DateOfSubmission ASC)
    @GetMapping("/overviewCommittee")
    public ResponseEntity<Page<OverviewApplicationDto>> getOverviewCommittee(ApplicationPage applicationPage,
                                                                             ApplicationSearchCriteria applicationSearchCriteria) {
        Page<OverviewApplicationDto> resultPage = applicationService.getOverviewCommittee(applicationPage, applicationSearchCriteria);
        return new ResponseEntity<>(resultPage, HttpStatus.OK);
    }

    // sends paged applications which are already closed (approval finished)
    // they can be also filtered and sorted
    @GetMapping("/searchApplication")
    public ResponseEntity<Page<OverviewApplicationDto>> getSearchApplication(ApplicationPage applicationPage,
                                                  ApplicationSearchCriteria applicationSearchCriteria) {
        Page<OverviewApplicationDto> resultPage =applicationService.searchApplications(applicationPage, applicationSearchCriteria);
        return new ResponseEntity<>(resultPage, HttpStatus.OK);
    }

    // endpoint for employee to get application by id
    @GetMapping("/getApplication")
    public ResponseEntity<?> getApplicationByID(@RequestParam("applicationID") String applicationID) {
        EntireOriginalAndEditedApplicationDto entireOriginalAndEditedApplicationDto = applicationService.getApplicationByID(applicationID);
        return new ResponseEntity<>(entireOriginalAndEditedApplicationDto, HttpStatus.OK);
    }

    // get the pdf to one specific module which the student has uploaded
    @GetMapping(path = "/getModulePDF", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> getPDF(@RequestParam String filePath) {
        try {
            Resource pdfResource = pdfService.getModulePDF("/app/pdf-files" + filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filepath=" + filePath)
                    .body(pdfResource);
        } catch (FileNotFoundException | MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
