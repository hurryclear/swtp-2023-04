package com.swtp4.backend.services;


import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.ModuleBlockRepository;
import com.swtp4.backend.repositories.ModuleRelationRepository;
import com.swtp4.backend.repositories.applicationDtos.EntireBlock;
import com.swtp4.backend.repositories.applicationDtos.EntireStudentModule;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class PDFService {


    private ApplicationRepository applicationRepository;
    private ModuleBlockRepository moduleBlockRepository;
    private ModuleRelationRepository moduleRelationRepository;

    public PDFService(ApplicationRepository applicationRepository,
                      ModuleBlockRepository moduleBlockRepository,
                      ModuleRelationRepository moduleRelationRepository) {
        this.applicationRepository = applicationRepository;
        this.moduleBlockRepository = moduleBlockRepository;
        this.moduleRelationRepository = moduleRelationRepository;
    }

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
                    throw new RuntimeException("Failed to save file: " + filePath);
                }
            } else {
                System.out.println("File is empty or null for key: " + fileKey);
            }
        }
    }

    public Resource getModulePDF(String filePathString) throws MalformedURLException, FileNotFoundException {
        Path filePath = Paths.get(filePathString);
        Resource resource = new UrlResource(filePath.toUri());
        if (resource.exists()) {
            if (resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("File not readable");
            }
        } else {
            throw new FileNotFoundException("File not found");
        }
    }


    public Resource generatePDFForApplication(String applicationId) throws IOException {
        //Initialize PDF Document and format
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        // Starte am oberen Rand der Seite
        float margin = 100;
        float yStart = 700;
        float lineSpacing = 20; // Stelle hier den gewünschten Zeilenabstand ein
        contentStream.newLineAtOffset(margin, yStart);

        //Initialize textLines
        ArrayList<String> textLines = new ArrayList<>();

        //Get Application Entity and write Data
        ApplicationEntity applicationEntity = applicationRepository.findById(ApplicationKeyClass.builder()
                .creator("Employee")
                .id(applicationId).build()).orElseThrow(() -> new ResourceNotFoundException("Application not found: " + applicationId));
        textLines.add("Antragsnummer: " + applicationId);
        textLines.add("Status: " + applicationEntity.getStatus());
        textLines.add("Einreichungsdatum: " + applicationEntity.getDateOfSubmission().toString());
        textLines.add("Letzte Bearbeitung: " + applicationEntity.getDateLastEdited().toString());
        textLines.add("Universitätsname: " + applicationEntity.getUniversityName());
        textLines.add("Studienfach: " + applicationEntity.getStudentMajor());
        textLines.add("Uni-Fach: " + applicationEntity.getUniMajor());
        textLines.add("Formaler Ablehnungsgrund: " + (applicationEntity.getFormalRejectionReason() != null ? applicationEntity.getFormalRejectionReason() : "Keine"));

        List<ModuleBlockEntity> moduleBlockEntityList = moduleBlockRepository.findAllByApplicationEntity(applicationEntity);
        for (ModuleBlockEntity moduleBlockEntity : moduleBlockEntityList) {
            ArrayList<String> textLinesModulesStudent = new ArrayList<>();
            ArrayList<String> textLinesModulesUni = new ArrayList<>();
            textLines.add("Mapping " + moduleBlockEntity.getFrontendKey()+1 + ":");
            List<ModuleRelationEntity> moduleRelationEntityList = moduleRelationRepository.findByModuleBlockEntity(moduleBlockEntity);
            List<ModuleStudentEntity> moduleStudentEntityList = new ArrayList<>();
            List<ModuleUniEntity> moduleUniEntityList = new ArrayList<>();
            for (ModuleRelationEntity moduleRelationEntity : moduleRelationEntityList) {
                ModuleStudentEntity moduleStudentEntity = moduleRelationEntity.getModuleRelationKeyClass().getModuleStudentEntity();
                boolean isNewStudentModule = !moduleStudentEntityList.stream()
                        .map(ModuleStudentEntity::getId)
                        .toList()
                        .contains(moduleStudentEntity.getId());
                if (isNewStudentModule) {
                    moduleStudentEntityList.add(moduleStudentEntity);
                    textLinesModulesStudent.add("Modul " + moduleStudentEntity.getFrontendKey()+1 + " : " + moduleStudentEntity.getTitle());
                    textLinesModulesStudent.add("Modulnummer: " + moduleStudentEntity.getNumber());
                    textLinesModulesStudent.add("Universität: " + moduleStudentEntity.getUniversity());
                    textLinesModulesStudent.add("Studiengang: " + moduleStudentEntity.getMajor());
                    textLinesModulesStudent.add("Leistungspunkte: " + moduleStudentEntity.getCredits().toString());
                    textLinesModulesStudent.add("Studentenkommentar: " + moduleStudentEntity.getCommentStudent());
                }
                ModuleUniEntity moduleUniEntity = moduleRelationEntity.getModuleRelationKeyClass().getModuleUniEntity();
                boolean isNewUniModule = !moduleUniEntityList.stream()
                        .map(ModuleUniEntity::getId)
                        .toList()
                        .contains(moduleUniEntity.getId());
                if (isNewUniModule) {
                    moduleUniEntityList.add(moduleUniEntity);
                    textLinesModulesUni.add(moduleUniEntity.getName());
                }
                textLines.addAll(textLinesModulesStudent);
                textLines.add("Anrechnen für:");
                textLines.addAll(textLinesModulesUni);
            }
        }

        //Write textLines in the PDF Document
        for (String line : textLines) {
            contentStream.showText(line);
            contentStream.newLineAtOffset(0, -lineSpacing);
        }

        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        byte[] pdfBytes = outputStream.toByteArray();
        ByteArrayResource pdfResource = new ByteArrayResource(pdfBytes);
        return pdfResource;
    }
}
