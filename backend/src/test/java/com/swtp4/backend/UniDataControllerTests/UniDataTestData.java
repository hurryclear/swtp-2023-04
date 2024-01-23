package com.swtp4.backend.UniDataControllerTests;

import com.swtp4.backend.JsonToStringConverter;
import com.swtp4.backend.repositories.entities.MajorUniEntity;
import com.swtp4.backend.repositories.entities.ModuleUniEntity;

public class UniDataTestData {

    private UniDataTestData() {}

    public static String createTestUniDataJsonA() throws Exception{
        return JsonToStringConverter.convertJsonToString("UniMajorsAndModulesJsonA.json");
    }

    public static MajorUniEntity createTestMajorUniEntityA() {
        return MajorUniEntity.builder()
                //.id(null)
                .name("B.Sc. Informatik").build();
    }

    public static MajorUniEntity createTestMajorUniEntityB() {
        return MajorUniEntity.builder()
                //.id(null)
                .name("M.Sc. Informatik").build();
    }

    public static ModuleUniEntity createTestModuleUniEntityA() {
        return ModuleUniEntity.builder()
                .number("10-201-2001-1")
                .name("Datorithmen und Algenstrukturen 1")
                .majorUniEntity(UniDataTestData.createTestMajorUniEntityA()).build();
    }

    public static ModuleUniEntity createTestModuleUniEntityB() {
        return ModuleUniEntity.builder()
                .number("10-201-2006-1")
                .name("Techniklagen der grundlegenden Informatik 1")
                .majorUniEntity(UniDataTestData.createTestMajorUniEntityA()).build();
    }

    public static ModuleUniEntity createTestModuleUniEntityC() {
        return ModuleUniEntity.builder()
                .number("10-201-2004")
                .name("Betriebs- und Kommunikationssysteme")
                .majorUniEntity(UniDataTestData.createTestMajorUniEntityB()).build();
    }
}
