package com.swtp4.backend.controller;

import com.swtp4.backend.repositories.dto.*;
import com.swtp4.backend.services.UniDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping(path = "/unidata")
public class UniDataController {

    private final UniDataService uniDataService;

    @Autowired
    public UniDataController(UniDataService uniDataService) {
        this.uniDataService = uniDataService;
    }

    @PutMapping(path = "/update")
    public ResponseEntity<?> updateUniData(@RequestBody UniDataDto uniDataDto) {
        uniDataService.updateVisibilityBasedOnJson(uniDataDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/getMajors")
    public ResponseEntity<UniMajorListResponse> getMajors() {
        UniMajorListResponse majors = uniDataService.getVisibleMajorNames();
        return new ResponseEntity<>(majors, HttpStatus.OK);
    }

    @GetMapping("/getModules")
    public ResponseEntity<UniModuleListResponse> getModulesByMajor(@RequestParam String majorName) {
        UniModuleListResponse modules = uniDataService.getVisibleMajorWithModules(majorName);
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    @GetMapping("/getAllMajors") //unvisible inluded
    public ResponseEntity<UniMajorsWithVisibilityResponse> getAllMajors() {
        UniMajorsWithVisibilityResponse majors = uniDataService.getAllMajors();
        return new ResponseEntity<>(majors, HttpStatus.OK);
    }

    @GetMapping("/getAllModules") //unvisible inluded
    public ResponseEntity<UniModulesWithVisibilityResponse> getAllModulesByMajor(@RequestParam String majorName) {
        UniModulesWithVisibilityResponse modules = uniDataService.getAllModulesByMajor(majorName);
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

}
