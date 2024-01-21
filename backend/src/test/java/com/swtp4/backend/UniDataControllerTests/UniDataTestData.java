package com.swtp4.backend.UniDataControllerTests;

import com.swtp4.backend.JsonToStringConverter;
import com.swtp4.backend.repositories.entities.MajorUniEntity;

public class UniDataTestData {

    private UniDataTestData() {}

    public static String createTestUniDataJsonA() throws Exception{
        return JsonToStringConverter.convertJsonToString("UniMajorsAndModulesJsonA.json");
    }

    public static MajorUniEntity createTestMajorUniEntityA() {
        return MajorUniEntity.builder()
                .id((long) 123)
                .name("B. Sc. Informatik").build();
    }
}
