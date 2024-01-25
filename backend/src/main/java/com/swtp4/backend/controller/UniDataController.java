package com.swtp4.backend.controller;

import com.swtp4.backend.repositories.dto.UniDataDto;
import com.swtp4.backend.services.UniDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
}
