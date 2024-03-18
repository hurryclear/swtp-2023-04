package com.swtp4.backend.DataInitializer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.swtp4.backend.Deserializer.EditedApplicationDeserializer;
import com.swtp4.backend.Deserializer.SubmittedApplicationDeserializer;
import com.swtp4.backend.repositories.applicationDtos.EditedApplicationDto;
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
import java.util.List;
import java.util.Map;

@Component
@Order(4)
@Profile({"dev"})
@Slf4j
public class ApprovedApplicationInitializer implements CommandLineRunner {

    private final ApplicationService applicationService;

    @Autowired
    public ApprovedApplicationInitializer(ApplicationService applicationService){
        this.applicationService = applicationService;
    }

    // this initializes a approved application for testing reasons
    @Override
    public void run(String... args) throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(SubmittedApplicationDto.class, new SubmittedApplicationDeserializer());
        module.addDeserializer(EditedApplicationDto.class, new EditedApplicationDeserializer());
        mapper.registerModule(module);
        TypeReference<SubmittedApplicationDto> typeReferenceSubmitted = new TypeReference<SubmittedApplicationDto>(){};
        InputStream inputStreamSubmitted = TypeReference.class.getResourceAsStream("/json/initialApplicationData.json");
        try {
            log.info("before submit");
            // first submit new application
            SubmittedApplicationDto applicationDto = mapper.readValue(inputStreamSubmitted,typeReferenceSubmitted);
            // get ID and file paths
            ApplicationIDWithFilePaths applicationIDWithFilePaths = applicationService.saveSubmitted(applicationDto);
            String applicationID = applicationIDWithFilePaths.getApplicationID();
            Map<String, String> filePaths = applicationIDWithFilePaths.getFilesAndPaths();

            //save pdfs of this application
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
            log.info("before edited and approved");

            //paste in ID from above into edited Application
            InputStream inputStream = getClass().getResourceAsStream("/json/initialEditedApplicationData.json");
            String editedJsonString = new String(inputStream.readAllBytes());
            log.info("read all bytes");
            JsonNode editedJsonNode = mapper.readTree(editedJsonString);
            ((ObjectNode) editedJsonNode.path("edited").path("applicationData")).put("applicationID", applicationID);
            log.info("put applicationID");
            String modifiedJsonString = mapper.writeValueAsString(editedJsonNode);

            //simulate Application being edited and approved
            EditedApplicationDto editedApplicationDto = mapper.readValue(modifiedJsonString, EditedApplicationDto.class);
            log.info("updateStatus1");
            applicationService.updateStatus(editedApplicationDto.applicationID(), "ready for approval");
            log.info("updateApproval1");
            applicationService.updateApproval(editedApplicationDto, List.of("formally rejected"));
            log.info("updateStatus2");
            applicationService.updateStatus(editedApplicationDto.applicationID(), "edited approval");
            log.info("updateApproval2");
            applicationService.updateApproval(editedApplicationDto, List.of("accepted", "rejected"));
            log.info("updateApplication");
            applicationService.updateApplication(editedApplicationDto);

            System.out.println("ApplicationID: "+applicationID);
            System.out.println("Approved Application with Pdfs initialized!");
        } catch (IOException e){
            System.out.println("Unable to initialize Application: " + e.getMessage());
        }
    }
}