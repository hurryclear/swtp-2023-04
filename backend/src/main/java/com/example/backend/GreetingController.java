package com.example.backend;

import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public String greeting() {
        return "Hello from Spring Boot!";
    }



    // zum posten: http://backend:8080/STUDENTEN_ID_ALSO_ZAHL_HIER_HINSCHREIBEN
    // axios.post(http://backend:8080/STUDENTEN_ID_ALSO_ZAHL_HIER_HINSCHREIBEN)
    @PostMapping(path = "{studentId}")
    public String returnID(@PathVariable("studentId") Long studentID) {return studentID.toString();}


}