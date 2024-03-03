package com.swtp4.backend.DataInitializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swtp4.backend.repositories.dto.UniDataDto;
import com.swtp4.backend.services.UniDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@Component
@Profile({"dev", "prod"})
public class UniDataInitializer implements CommandLineRunner {

    private final UniDataService uniDataService;

    @Autowired
    public UniDataInitializer(UniDataService uniDataService){
        this.uniDataService = uniDataService;
    }

    @Override
    public void run(String... args) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        TypeReference<UniDataDto> typeReference = new TypeReference<UniDataDto>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/initialUniData.json");
        try {
            UniDataDto uniDataDto = mapper.readValue(inputStream,typeReference);
            uniDataService.updateVisibilityBasedOnJson(uniDataDto);
            System.out.println("University Majors and Modules initialized!");
        } catch (IOException e){
            System.out.println("Unable to initialize UniData: " + e.getMessage());
        }
    }
}
