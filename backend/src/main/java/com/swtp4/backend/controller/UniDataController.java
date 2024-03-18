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

    //update which majors and modules of university of leipzig are shown to the students
    @PutMapping(path = "/update")
    public ResponseEntity<?> updateUniData(@RequestBody UniDataDto uniDataDto) {
        uniDataService.updateVisibilityBasedOnJson(uniDataDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //get all study programs that can be selected by students
    @GetMapping("/getMajors")
    public ResponseEntity<UniMajorListResponse> getMajors() {
        UniMajorListResponse majors = uniDataService.getVisibleMajorNames();
        return new ResponseEntity<>(majors, HttpStatus.OK);
    }

    //get all modules of one selected study program which can be selected as modulesToBeCredited
    @GetMapping("/getModules")
    public ResponseEntity<UniModuleListResponse> getModulesByMajor(@RequestParam String majorName) {
        UniModuleListResponse modules = uniDataService.getVisibleMajorWithModules(majorName);
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

    //get all study programs saved, this includes study programs which are invisible to students
    @GetMapping("/getAllMajors") //unvisible inluded
    public ResponseEntity<UniMajorsWithVisibilityResponse> getAllMajors() {
        UniMajorsWithVisibilityResponse majors = uniDataService.getAllMajors();
        return new ResponseEntity<>(majors, HttpStatus.OK);
    }

    //get all modules of one study program, this includes modules that are invisible to students
    @GetMapping("/getAllModules") //unvisible inluded
    public ResponseEntity<UniModulesWithVisibilityResponse> getAllModulesByMajor(@RequestParam String majorName) {
        UniModulesWithVisibilityResponse modules = uniDataService.getAllModulesByMajor(majorName);
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

}
