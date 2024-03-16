package com.swtp4.backend.DataInitializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.swtp4.backend.Deserializer.SubmittedApplicationDeserializer;
import com.swtp4.backend.repositories.applicationDtos.SubmittedApplicationDto;
import com.swtp4.backend.repositories.dto.ApplicationDto;
import com.swtp4.backend.repositories.dto.ApplicationIDWithFilePaths;
import com.swtp4.backend.repositories.dto.ApplicationIdDto;
import com.swtp4.backend.repositories.dto.UniDataDto;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.UniDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.Map;

@Component
@Order(3)
@Profile({"dev"})
@Slf4j
public class ApplicationInitializer implements CommandLineRunner {

    private final ApplicationService applicationService;

    @Autowired
    public ApplicationInitializer(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    @Override
    public void run(String... args) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(SubmittedApplicationDto.class, new SubmittedApplicationDeserializer());
        mapper.registerModule(module);
        TypeReference<SubmittedApplicationDto> typeReference = new TypeReference<SubmittedApplicationDto>(){};
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/initialApplicationData.json");
        try {
            SubmittedApplicationDto applicationDto = mapper.readValue(inputStream,typeReference);

            ApplicationIDWithFilePaths applicationIDWithFilePaths = applicationService.saveSubmitted(applicationDto);
            String applicationID = applicationIDWithFilePaths.getApplicationID();
            Map<String, String> filePaths =applicationIDWithFilePaths.getFilesAndPaths();

            //save mock pdfs
            byte[] bytes = new ClassPathResource("/json/Mock Modulebeschreibung Modul 1.pdf").getContentAsByteArray();
            Path path = Paths.get("/app/pdf-files" + filePaths.get("file-0:0"));
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
            bytes = new ClassPathResource("/json/Mock Modulebeschreibung Modul 2.pdf").getContentAsByteArray();
            path = Paths.get("/app/pdf-files" + filePaths.get("file-0:1"));
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
            bytes = new ClassPathResource("/json/Mock Modulebeschreibung Modul 3.pdf").getContentAsByteArray();
            path = Paths.get("/app/pdf-files" + filePaths.get("file-1:0"));
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);

            System.out.println("ApplicationID: "+applicationID);
            System.out.println("Application with Pdfs initialized!");
        } catch (IOException e){
            System.out.println("Unable to initialize Application: " + e.getMessage());
        }
    }
}
