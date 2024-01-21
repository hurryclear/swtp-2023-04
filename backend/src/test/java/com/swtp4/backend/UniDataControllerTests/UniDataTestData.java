package com.swtp4.backend.UniDataControllerTests;

import com.swtp4.backend.JsonToStringConverter;

public class UniDataTestData {

    private UniDataTestData() {}

    public static String createTestUniDataJsonA() throws Exception{
        return JsonToStringConverter.convertJsonToString("UniMajorsAndModulesJsonA.json");
    }
}
