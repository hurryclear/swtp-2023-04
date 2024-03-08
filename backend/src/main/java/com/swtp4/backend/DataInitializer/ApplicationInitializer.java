package com.swtp4.backend.DataInitializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swtp4.backend.repositories.applicationDtos.SubmittedApplicationDto;
import com.swtp4.backend.repositories.dto.ApplicationDto;
import com.swtp4.backend.repositories.dto.UniDataDto;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.UniDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Profile({"dev"})
public class ApplicationInitializer implements CommandLineRunner {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationInitializer(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    @Override
    public void run(String... args) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<SubmittedApplicationDto> typeReference = new TypeReference<SubmittedApplicationDto>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/.json");
        try {
            SubmittedApplicationDto applicationDto = mapper.readValue(inputStream,typeReference);
            applicationService.saveSubmitted(applicationDto);
            System.out.println("Application initialized!");
        } catch (IOException e){
            System.out.println("Unable to initialize Application: " + e.getMessage());
        }
    }
}
