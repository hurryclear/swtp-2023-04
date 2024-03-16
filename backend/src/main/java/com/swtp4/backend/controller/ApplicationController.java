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

    @PutMapping("/saveEdited")
    @Transactional
    public ResponseEntity<?> saveApplication(@RequestBody EditedApplicationDto applicationDto){
        log.info("Received applicationDto: {}", applicationDto);
        applicationService.updateStatus(applicationDto.applicationID(), "edited");
        applicationService.updateApproval(applicationDto, List.of("formally rejected"));
        applicationService.updateApplication(applicationDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/formalRejection")
    @Transactional
    public ResponseEntity<?> formalRejectApplication(@RequestBody EditedApplicationDto applicationDto){
        log.info("Received ApplicationDto: {}", applicationDto);
        applicationService.statusFormalRejection(applicationDto);
        applicationService.updateApplication(applicationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/readyForApproval")
    @Transactional
    public ResponseEntity<?> readyForApproval(@RequestBody EditedApplicationDto applicationDto){
        log.info("Received applicationDto: {}", applicationDto);
        applicationService.updateStatus(applicationDto.applicationID(), "ready for approval");
        applicationService.updateApproval(applicationDto, List.of("formally rejected"));
        applicationService.updateApplication(applicationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasRole('COMMITTEE')")
    @PutMapping("/saveApproval")
    @Transactional
    public ResponseEntity<?> saveApproval(@RequestBody EditedApplicationDto applicationDto){
        applicationService.updateStatus(applicationDto.applicationID(), "edited approval");
        applicationService.updateApproval(applicationDto, List.of("accepted", "rejected"));
        applicationService.updateApplication(applicationDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // "/getApplication?filters=__&sortBy=__&sortOrder=__&page=__&size=__"
    // from here is sorting, filtering and paging
    @GetMapping("/overviewOffice")
    public ResponseEntity<Page<OverviewApplicationDto>> getOverviewOffice(ApplicationPage applicationPage,
                                                                                ApplicationSearchCriteria applicationSearchCriteria) {
        Page<OverviewApplicationDto> resultPage = applicationService.getOverviewOffice(applicationPage, applicationSearchCriteria);
        return new ResponseEntity<>(resultPage, HttpStatus.OK);
    }

    @GetMapping("/overviewCommittee")
    public ResponseEntity<Page<OverviewApplicationDto>> getOverviewCommittee(ApplicationPage applicationPage,
                                                                             ApplicationSearchCriteria applicationSearchCriteria) {
        Page<OverviewApplicationDto> resultPage = applicationService.getOverviewCommittee(applicationPage, applicationSearchCriteria);
        return new ResponseEntity<>(resultPage, HttpStatus.OK);
    }

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


    @GetMapping(path = "/getModulePDF", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<Resource> getPDF(@RequestParam String filePath,
                                           @RequestParam String moduleName) {
        try {
            Resource pdfResource = pdfService.getModulePDF("/app/pdf-files" + filePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + moduleName + ".pdf\"")
                    .body(pdfResource);
        } catch (FileNotFoundException | MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


//    @PostMapping("/test")
//    public ResponseEntity<UniModuleDto> testOfficeEndpoint(@RequestBody UniModuleDto uniModuleDto){
//        return new ResponseEntity<>(uniModuleDto, HttpStatus.OK);
//    }
//
//    @GetMapping("/get-applications-with-sorting/{field}")
//    public List<ApplicationEntity> getAllApplicationsWithSorting(@PathVariable String field) {
//        return applicationService.getAllApplicationsWithSorting(field);
//    }
//
//    // the following gets are for employee, it will only return applications with creator "Employee"
//    @GetMapping("/get-all-applications")
//    public List<ApplicationEntity> getAllApplications() {
//        return applicationService.getAllApplications();
//    }
//
//    @GetMapping("/get-applications-by-status")
//    public List<ApplicationEntity> getApplicationsByStatus(@RequestParam("status") String status) {
//        return applicationService.getApplicationsByStatus(status);
//    }
//
//    @GetMapping("/get-applications-by-major")
//    public List<ApplicationEntity> getApplicationsByMajor(@RequestParam("major") String major) {
//        return applicationService.getApplicationsByMajor(major);
//    }
//
//    @GetMapping("/get-applications-by-university")
//    public List<ApplicationEntity> getApplicationsByUniversity(@RequestParam("universityName") String universityName) {
//        return applicationService.getApplicationsByUniversityName(universityName);
//    }
//
//    @GetMapping("/get-applications-by-date-of-submission")
//    public ApplicationEntity getApplicationsByDateOfSubmission(@RequestParam("dateOfSubmission") String dateOfSubmission) {
//        return applicationService.getApplicationsByDateOfSubmission(dateOfSubmission);
//    }
//
//    // not complete
//    @GetMapping("/get-applications-before-date-of-submission")
//    public List<ApplicationEntity> getApplicationsByDateOfSubmissionBefore(@RequestParam("dateOfSubmission") String dateOfSubmission) {
//        return applicationService.getApplicationsByDateOfSubmissionBefore(dateOfSubmission);
//    }
//
//    // not complete
//    @GetMapping("/get-applications-after-date-of-submission")
//    public List<ApplicationEntity> getApplicationsByDateOfSubmissionAfter(@RequestParam("dateOfSubmission") String dateOfSubmission) {
//        return applicationService.getApplicationsByDateOfSubmissionAfter(dateOfSubmission);
//    }

}
