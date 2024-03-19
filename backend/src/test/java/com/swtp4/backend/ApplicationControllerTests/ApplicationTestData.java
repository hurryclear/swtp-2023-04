package com.swtp4.backend.ApplicationControllerTests;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swtp4.backend.JsonToStringConverter;
import com.swtp4.backend.repositories.dto.UniDataDto;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.repositories.entities.keyClasses.ModuleRelationKeyClass;
import com.swtp4.backend.services.UniDataService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@Service
@Profile("integration")
public class ApplicationTestData {
    @Autowired
    private UniDataService uniDataService;

    private ApplicationTestData(){}

    public void loadMajorAndModulesIntoDataBase() throws Exception {
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

    public static String createSubmitApplicationJson() throws Exception {
        return JsonToStringConverter.convertJsonToString("TestSubmittedApplication.json");
    }

    public static String createEditedApplicationJson(String applicationID) throws Exception {
        // Your JSON string
        String json = JsonToStringConverter.convertJsonToString("TestEditedApplication.json");

        // Convert JSON string to JSONObject
        JSONObject jsonObject = new JSONObject(json);

        // Modify the edited ID field
        JSONObject editedObjectID = jsonObject.getJSONObject("edited").getJSONObject("applicationData");
        editedObjectID.put("applicationID", applicationID);

        // Get the modified JSON string
        String modifiedJson = jsonObject.toString();

        return modifiedJson;
    }

    public static String createTestApplicationJsonA() throws Exception {
        return JsonToStringConverter.convertJsonToString("TestApplicationJsonA.json");
    }

    public static String createTestReviewApplication() throws Exception {
        return JsonToStringConverter.convertJsonToString("TestReviewApplication.json");
    }

    public static String createOriginalSubmitApplicationJson() throws Exception {
        return JsonToStringConverter.convertJsonToString("TestOriginalSubmittedApplication.json");
    }


//    public static ApplicationEntity createTestApplicationEntityA() {
//        return ApplicationEntity.builder()
//                .applicationKeyClass(createTestApplicationKeyClassA())
//                .status("open")
//                .dateOfSubmission("2023-12-31T22:30:42.103Z")
//                .dateLastEdited("2024-01-14T14:12:14.675Z")
//                .universityName("University of Regenbogenland")
////                .oajor("B. Sc. Informatik")
//                //.commentStudent("Ich will alle CP valla")
//                //.commentEmployee("Der bekommt nicht alle CP yalla")
//                .build();
//    }
//
//    public static ApplicationEntity createTestApplicationEntityB() {
//        return ApplicationEntity.builder()
//                .applicationKeyClass(createTestApplicationKeyClassA())
//                .status("open")
//                .dateOfSubmission("2023-10-31T22:30:42.103Z")
//                .dateLastEdited("2024-01-14T14:12:14.675Z")
//                .universityName("University of Halle")
//                .studentMajor("B. Sc. Informatik")
//                .build();
//    }
//
//    public static ApplicationEntity createTestApplicationEntityC() {
//        return ApplicationEntity.builder()
//                .applicationKeyClass(createTestApplicationKeyClassA())
//                .status("close")
//                .dateOfSubmission("2023-07-31T22:30:42.103Z")
//                .dateLastEdited("2024-01-14T14:12:14.675Z")
//                .universityName("University of Halle")
//                .studentMajor("B. Sc. Informatik")
//                .build();
//    }
//
//    public static ApplicationEntity createTestApplicationEntityD() {
//        return ApplicationEntity.builder()
//                .applicationKeyClass(createTestApplicationKeyClassA())
//                .status("close")
//                .dateOfSubmission("2023-01-31T22:30:42.103Z")
//                .dateLastEdited("2024-01-14T14:12:14.675Z")
//                .universityName("University of Dresden")
//                .studentMajor("B. Sc. Informatik")
//                .build();
//    }
//
//    public static ApplicationKeyClass createTestApplicationKeyClassA() {
//        return ApplicationKeyClass.builder()
//                .id(UUID.randomUUID().toString())
//                .creator("Student").build();
//    }
//
//    public static ModuleBlockEntity createTestModuleBlockEntityA() {
//        return ModuleBlockEntity.builder()
//                .id((long) 420)
//                .applicationEntity(ApplicationTestData.createTestApplicationEntityA())
//                .build();
//    }
//
//    public static ModuleUniEntity createTestModuleUniEntityA() {
//        return ModuleUniEntity.builder()
//                .number("10-201-2001-1")
//                .name("Algorithmen und Datenstrukturen 1")
//                .majorUniEntity(ApplicationTestData.createTestMajorUniEntityA()).build();
//    }
//
//    public static ModuleUniEntity createTestModuleUniEntityB() {
//        return ModuleUniEntity.builder()
//                .number("10-201-2001-2")
//                .name("Algorithmen und Datenstrukturen 2")
//                .majorUniEntity(ApplicationTestData.createTestMajorUniEntityA()).build();
//    }
//
//    public static ModuleUniEntity createTestModuleUniEntityC() {
//        return ModuleUniEntity.builder()
//                .number("10-201-2006-1")
//                .name("Grundlagen der Technischen Informatik 1")
//                .majorUniEntity(ApplicationTestData.createTestMajorUniEntityA()).build();
//    }
//
//    public static ModuleUniEntity createTestModuleUniEntityD() {
//        return ModuleUniEntity.builder()
//                .number("10-201-1015")
//                .name("Lineare Algebra für Informatiker")
//                .majorUniEntity(ApplicationTestData.createTestMajorUniEntityA()).build();
//    }
//
//    public static ModuleUniEntity createTestModuleUniEntityE() {
//        return ModuleUniEntity.builder()
//                .number("10-201-1011")
//                .name("Analysis für Informatiker")
//                .majorUniEntity(ApplicationTestData.createTestMajorUniEntityA()).build();
//    }
//
//    public static MajorUniEntity createTestMajorUniEntityA() {
//        return MajorUniEntity.builder()
//                //.id((long) 123)
//                .name("B. Sc. Informatik").build();
//    }
//
//    public static ModuleStudentEntity createTestModuleStudentEnityA() {
//        return ModuleStudentEntity.builder()
//                .id((long) 123)
//                .number("420")
//                .title("AlgoDat 1.5")
//                .description_pdf(null)
//                .credits(5L)
//                .university("University of Regenbogenland")
//                .major("B. Sc. Informatik")
//                .commentStudent("War cool")
//                .commentEmployee("Nee wars nicht").build();
//    }
//
//    public static ModuleRelationEntity createTestModuleRelationEntityA() {
//        return ModuleRelationEntity.builder()
//                .moduleRelationKeyClass(ApplicationTestData.createTestModuleRelationKeyClassA())
//                .moduleBlockEntity(ApplicationTestData.createTestModuleBlockEntityA()).build();
//    }
//
//    public static ModuleRelationKeyClass createTestModuleRelationKeyClassA() {
//        return ModuleRelationKeyClass.builder()
//                .moduleStudentEntity(ApplicationTestData.createTestModuleStudentEnityA())
//                .moduleUniEntity(ApplicationTestData.createTestModuleUniEntityA()).build();

//    }
}
