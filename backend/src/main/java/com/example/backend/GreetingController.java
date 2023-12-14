package com.example.backend;

import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public Object greeting() {
        return new University("Universität Bochum", "B. Sc. Informatik", "Deutschland");
    }



    // zum posten: http://backend:8080/STUDENTEN_ID_ALSO_ZAHL_HIER_HINSCHREIBEN
    // axios.post(http://backend:8080/STUDENTEN_ID_ALSO_ZAHL_HIER_HINSCHREIBEN)
    @PostMapping(path = "{studentId}")
    public String returnID(@PathVariable("studentId") Long studentID) {return studentID.toString();}


}