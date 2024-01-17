package com.swtp4.backend.controller;

import com.swtp4.backend.repositories.dto.UniDataDto;
import com.swtp4.backend.services.UniversityDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/unidata")
public class UniversityDataController {

    private UniversityDataService universityDataService;

    @Autowired
    public UniversityDataController(UniversityDataService universityDataService) {
        this.universityDataService = universityDataService;
    }

    @PutMapping(path = "/update")
    public void updateUniData(@RequestBody UniDataDto uniDataDto) {
        universityDataService.update(uniDataDto);
    }
}
