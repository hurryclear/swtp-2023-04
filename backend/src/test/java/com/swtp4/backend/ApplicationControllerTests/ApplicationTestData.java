package com.swtp4.backend.ApplicationControllerTests;

import com.swtp4.backend.JsonToStringConverter;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.repositories.entities.keyClasses.ModuleRelationKeyClass;

import java.util.UUID;

public class ApplicationTestData {

    private ApplicationTestData(){}

    public static ApplicationEntity createTestApplicationEntityA() {
        return ApplicationEntity.builder()
                .applicationKeyClass(createTestApplicationKeyClassA())
                .status("")
                .dateOfSubmission("2023-12-31T22:30:42.103Z")
                .dateLastEdited("2024-01-14T14:12:14.675Z")
                .universityName("University of Regenbogenland")
                .major("B. Sc. Informatik")
                .commentStudent("Ich will alle CP valla")
                .commentEmployee("Der bekommt nicht alle CP yalla").build();
    }

    public static ApplicationKeyClass createTestApplicationKeyClassA() {
        return ApplicationKeyClass.builder()
                .id(UUID.randomUUID().toString())
                .creator("Student").build();
    }

    public static ModuleBlockEntity createTestModuleBlockEntityA() {
        return ModuleBlockEntity.builder()
                .id((long) 420)
                .applicationEntity(ApplicationTestData.createTestApplicationEntityA())
                .approval("")
                .commentStudent("War cool")
                .commentEmployee("Das nicht so cool").build();
    }

    public static ModuleUniEntity createTestModuleUniEntityA() {
        return ModuleUniEntity.builder()
                .number("10-201-2001-1")
                .name("Algorithmen und Datenstrukturen 1")
                .majorUniEntity(ApplicationTestData.createTestMajorUniEntityA()).build();
    }

    public static ModuleUniEntity createTestModuleUniEntityB() {
        return ModuleUniEntity.builder()
                .number("10-201-2001-2")
                .name("Algorithmen und Datenstrukturen 2")
                .majorUniEntity(ApplicationTestData.createTestMajorUniEntityA()).build();
    }

    public static ModuleUniEntity createTestModuleUniEntityC() {
        return ModuleUniEntity.builder()
                .number("10-201-2006-1")
                .name("Grundlagen der Technischen Informatik 1")
                .majorUniEntity(ApplicationTestData.createTestMajorUniEntityA()).build();
    }

    public static ModuleUniEntity createTestModuleUniEntityD() {
        return ModuleUniEntity.builder()
                .number("10-201-1015")
                .name("Lineare Algebra für Informatiker")
                .majorUniEntity(ApplicationTestData.createTestMajorUniEntityA()).build();
    }

    public static ModuleUniEntity createTestModuleUniEntityE() {
        return ModuleUniEntity.builder()
                .number("10-201-1011")
                .name("Analysis für Informatiker")
                .majorUniEntity(ApplicationTestData.createTestMajorUniEntityA()).build();
    }

    public static MajorUniEntity createTestMajorUniEntityA() {
        return MajorUniEntity.builder()
                //.id((long) 123)
                .name("B. Sc. Informatik").build();
    }

    public static ModuleStudentEntity createTestModuleStudentEnityA() {
        return ModuleStudentEntity.builder()
                .id((long) 123)
                .number("420")
                .title("AlgoDat 1.5")
                .description_pdf(null)
                .credits(5)
                .university("University of Regenbogenland")
                .major("B. Sc. Informatik")
                .commentStudent("War cool")
                .commentEmployee("Nee wars nicht").build();
    }

    public static ModuleRelationEntity createTestModuleRelationEntityA() {
        return ModuleRelationEntity.builder()
                .moduleRelationKeyClass(ApplicationTestData.createTestModuleRelationKeyClassA())
                .moduleBlockEntity(ApplicationTestData.createTestModuleBlockEntityA()).build();
    }

    public static ModuleRelationKeyClass createTestModuleRelationKeyClassA() {
        return ModuleRelationKeyClass.builder()
                .moduleStudentEntity(ApplicationTestData.createTestModuleStudentEnityA())
                .moduleUniEntity(ApplicationTestData.createTestModuleUniEntityA()).build();
    }

    public static String createTestApplicationJsonA() throws Exception {
        return JsonToStringConverter.convertJsonToString("TestApplicationJsonA.json");
    }
}
