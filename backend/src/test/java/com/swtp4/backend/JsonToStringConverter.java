package com.swtp4.backend;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class JsonToStringConverter {

    private JsonToStringConverter() {}

    public static String convertJsonToString(String filePath) throws Exception {
        URL resource = JsonToStringConverter.class.getClassLoader().getResource(filePath);
        if (resource == null) {
            System.out.println("Resource not found: " + filePath);
            System.out.println("Current working directory: " + System.getProperty("user.dir"));
            throw new IllegalArgumentException("File not found: " + filePath);
        }
        Path path = Paths.get(resource.toURI());
        byte[] bytes = Files.readAllBytes(path);
        return new String(bytes);
    }
}
