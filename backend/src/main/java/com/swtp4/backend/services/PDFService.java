package com.swtp4.backend.services;

import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.ModuleStudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

@Service
public class PDFService {

    private ModuleStudentRepository moduleStudentRepository;
    private ApplicationRepository applicationRepository;
    
    public void saveModulePDFs(Map<String, MultipartFile> fileMap, HashMap<String, String> file_paths) {
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String fileKey = entry.getKey();
            MultipartFile file = entry.getValue();
            String filePath = file_paths.get(fileKey);

            if (file != null && !file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    Path path = Paths.get("/app/pdf-files" + filePath);
                    Files.createDirectories(path.getParent());
                    Files.write(path, bytes);
                    System.out.println("File saved successfully: " + filePath);
                } catch (IOException e) {
                    System.out.println("Failed to save file: " + filePath);
                    throw new RuntimeException();
                }
            } else {
                System.out.println("File is empty or null for key: " + fileKey);
            }
        }
    }
}
