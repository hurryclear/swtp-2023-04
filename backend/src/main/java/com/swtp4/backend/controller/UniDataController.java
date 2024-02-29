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

    private UniDataService uniDataService;

    @Autowired
    public UniDataController(UniDataService uniDataService) {
        this.uniDataService = uniDataService;
    }

    @PutMapping(path = "/update")
    public ResponseEntity<?> updateUniData(@RequestBody UniDataDto uniDataDto) {
        uniDataService.update(uniDataDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getMajors")
    public ResponseEntity<UniMajorListResponse> getMajors() {
        UniMajorListResponse majors = uniDataService.getAllMajors();
        return new ResponseEntity<>(majors, HttpStatus.OK);
    }

    @GetMapping("/getModules")
    public ResponseEntity<UniModuleListResponse> getModulesByMajor(@RequestParam String majorName) {
        UniModuleListResponse modules = uniDataService.getModulesByMajor(majorName);
        return new ResponseEntity<>(modules, HttpStatus.OK);
    }

}
