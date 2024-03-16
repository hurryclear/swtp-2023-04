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
        //Initialize PDF Document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);

        //Page height
        float maxHeight = 750;
        float currentHeight = maxHeight;

        //Headline: Summary
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
        contentStream.newLineAtOffset(100, currentHeight); // Adjust coordinates for positioning
        contentStream.showText("Zusammenfassung Antrag");
        contentStream.endText();

        //Application ID
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(100, currentHeight - 30); // Adjust coordinates for positioning
        contentStream.showText("Antragsnummer: " + applicationId);
        contentStream.endText();

        //Get Application Entity
        ApplicationEntity applicationEntity = applicationRepository.findById(ApplicationKeyClass.builder()
                .creator("Employee")
                .id(applicationId).build()).orElseThrow(() -> new ResourceNotFoundException("Application not found: " + applicationId));

        //Application Information Styling and Data
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        currentHeight = 680; // Starting y-coordinate for general information section
        float lineHeight = 15;

        //Application Data
        contentStream.newLineAtOffset(100, currentHeight);
        contentStream.showText("Status: " + applicationEntity.getStatus());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Einreichungsdatum: " + applicationEntity.getDateOfSubmission().toString());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Letzte Bearbeitung: " + applicationEntity.getDateLastEdited().toString());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Universitätsname: " + applicationEntity.getUniversityName());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Studienfach: " + applicationEntity.getStudentMajor());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Uni-Fach: " + applicationEntity.getUniMajor());
        contentStream.newLineAtOffset(0, -lineHeight);
        contentStream.showText("Formaler Ablehnungsgrund: " + (applicationEntity.getFormalRejectionReason() != null ? applicationEntity.getFormalRejectionReason() : "Keine"));
        contentStream.endText();

        //Module Blocks and Modules Height Spacing
        float blockSpacing = 20;
        float moduleSpacing = 15;
        currentHeight -= (lineHeight * 8 + blockSpacing);

        //Get ModuleBlocks
        List<ModuleBlockEntity> moduleBlockEntityList = moduleBlockRepository.findAllByApplicationEntity(applicationEntity);
        for (ModuleBlockEntity moduleBlockEntity : moduleBlockEntityList) {
            //Module Block Headline
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
            contentStream.newLineAtOffset(100, currentHeight);
            contentStream.showText("Mapping " + (moduleBlockEntity.getFrontendKey() + 1) + ":");
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.endText();

            currentHeight -= (lineHeight + moduleSpacing);

            //Get ModuleRelations from ModuleBlock and all associated ModuleStudentEntities and ModuleUniEntities
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
                }
                ModuleUniEntity moduleUniEntity = moduleRelationEntity.getModuleRelationKeyClass().getModuleUniEntity();
                boolean isNewUniModule = !moduleUniEntityList.stream()
                        .map(ModuleUniEntity::getId)
                        .toList()
                        .contains(moduleUniEntity.getId());
                if (isNewUniModule) {
                    moduleUniEntityList.add(moduleUniEntity);
                }
            }

            //Write Data in the PDF
            for (ModuleStudentEntity moduleStudentEntity : moduleStudentEntityList) {
                //Check if Page is full
                if (currentHeight < 0) {
                    PDPage newPage = new PDPage();
                    document.addPage(page);
                    contentStream.close();
                    contentStream = new PDPageContentStream(document, newPage);
                }
                //ModuleStudent Data
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, currentHeight);
                contentStream.showText("Modul " + (moduleStudentEntity.getFrontendKey() + 1) + ": " + moduleStudentEntity.getTitle());
                contentStream.newLineAtOffset(0, -moduleSpacing);
                contentStream.showText("Modulnummer: " + moduleStudentEntity.getNumber());
                contentStream.newLineAtOffset(0, -moduleSpacing);
                contentStream.showText("Universität: " + moduleStudentEntity.getUniversity());
                contentStream.newLineAtOffset(0, -moduleSpacing);
                contentStream.showText("Studiengang: " + moduleStudentEntity.getMajor());
                contentStream.newLineAtOffset(0, -moduleSpacing);
                contentStream.showText("Leistungspunkte: " + moduleStudentEntity.getCredits().toString());
                contentStream.newLineAtOffset(0, -moduleSpacing);
                contentStream.showText("Studentenkommentar: " + moduleStudentEntity.getCommentStudent());
                contentStream.endText();
                currentHeight -= (lineHeight * 6 + moduleSpacing);
            }
            //Check if Page is full
            if (currentHeight < 0) {
                PDPage newPage = new PDPage();
                document.addPage(page);
                contentStream.close();
                contentStream = new PDPageContentStream(document, newPage);
            }
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, currentHeight); // Adjust this offset based on your layout
            contentStream.showText("Anzurechnende Module:");
            contentStream.endText();
            currentHeight -= (lineHeight);

            for (ModuleUniEntity moduleUniEntity : moduleUniEntityList) {
                //Check if Page is full
                if (currentHeight < 0) {
                    PDPage newPage = new PDPage();
                    document.addPage(page);
                    contentStream.close();
                    contentStream = new PDPageContentStream(document, newPage);
                }
                //Module Uni Data
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, currentHeight); // Adjust this offset based on your layout
                contentStream.showText(moduleUniEntity.getName());
                contentStream.endText();
                currentHeight -= (lineHeight);
            }
            currentHeight -= (blockSpacing);
        }
        contentStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        byte[] pdfBytes = outputStream.toByteArray();
        return new ByteArrayResource(pdfBytes);
    }
}
