package com.swtp4.backend.services;


import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.ModuleBlockRepository;
import com.swtp4.backend.repositories.ModuleRelationRepository;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


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
        ApplicationEntity applicationEntity = applicationRepository.findById(ApplicationKeyClass.builder()
                        .creator("Employee")
                        .id(applicationId).build())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + applicationId));


        // Initialize PDF Document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        float currentHeight = 750;

        // Headline: Zusammenfassung Antrag
        writeContentOneLine(contentStream, PDType1Font.HELVETICA_BOLD, 20, 100, currentHeight, "Zusammenfassung Antrag");

        // Application ID, now using the formatted UUID
        String formattedApplicationId = formatUUID(applicationId); // Verwendung der neuen Methode hier
        writeContentOneLine(contentStream, PDType1Font.HELVETICA, 12, 100, currentHeight-30, "Antragsnummer: " + formattedApplicationId);

        currentHeight = 700;
        float lineHeight = 15;

        //Get Application Entity
         //Application Data
        ArrayList<String> textLinesApplication = new ArrayList<>(Arrays.asList(
                "Status: " + applicationEntity.getStatus(),
                "Einreichungsdatum: " + applicationEntity.getDateOfSubmission().toString(),
                "Universitätsname: " + applicationEntity.getUniversityName(),
                "Studienfach: " + applicationEntity.getStudentMajor(),
                "Uni-Fach: " + applicationEntity.getUniMajor()));
        if (applicationEntity.getStatus().equals("formally rejected")) {
            textLinesApplication.add("Formaler Ablehnungsgrund: " + applicationEntity.getFormalRejectionReason());
        }
        writeContentMultipleLines(contentStream, PDType1Font.HELVETICA, 12, 100, 0, currentHeight, -lineHeight, textLinesApplication);

        //Module Blocks and Modules Height Spacing
        float blockSpacing = 20;
        float moduleSpacing = 15;
        currentHeight -= (lineHeight*textLinesApplication.size() + 2*blockSpacing);

        //Get ModuleBlocks
        List<ModuleBlockEntity> moduleBlockEntityList = moduleBlockRepository.findAllByApplicationEntity(applicationEntity);
        for (ModuleBlockEntity moduleBlockEntity : moduleBlockEntityList) {
            //Check if Page is full
            if (currentHeight < 20) {
                contentStream.close();
                PDPage newPage = new PDPage();
                document.addPage(newPage);
                contentStream = new PDPageContentStream(document, newPage);
                currentHeight = 750;
            }
            //Module Block Headline
            writeContentOneLine(contentStream, PDType1Font.HELVETICA_BOLD, 14, 100, currentHeight, "Mapping " + (moduleBlockEntity.getFrontendKey() + 1) + ":");
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

            for (ModuleStudentEntity moduleStudentEntity : moduleStudentEntityList) {
                //Check if Page is full
                if (currentHeight < 20) {
                    contentStream.close();
                    PDPage newPage = new PDPage();
                    document.addPage(newPage);
                    contentStream = new PDPageContentStream(document, newPage);
                    currentHeight = 750;
                }
                //ModuleStudent Data
                ArrayList<String> textLinesModuleStudent = new ArrayList<>(Arrays.asList(
                        "Modul " + (moduleStudentEntity.getFrontendKey() + 1) + ": " + moduleStudentEntity.getTitle(),
                        "Modulnummer: " + moduleStudentEntity.getNumber(),
                        "Universität: " + moduleStudentEntity.getUniversity(),
                        "Studiengang: " + moduleStudentEntity.getMajor(),
                        "Leistungspunkte: " + moduleStudentEntity.getCredits().toString(),
                        "Studentenkommentar: " + moduleStudentEntity.getCommentStudent()));
                if (moduleStudentEntity.getApproval().equals("accepted") || moduleStudentEntity.getApproval().equals("rejected") || moduleStudentEntity.getApproval().equals("formally rejected")) {
                    textLinesModuleStudent.add("Modul " + moduleStudentEntity.getApproval() + ": " + moduleStudentEntity.getApprovalReason());
                }
                writeContentMultipleLines(contentStream ,PDType1Font.HELVETICA, 12, 100, 0, currentHeight, -moduleSpacing, textLinesModuleStudent);
                currentHeight -= (lineHeight*textLinesModuleStudent.size() + moduleSpacing);
            }

            //Check if Page is full
            if (currentHeight < 20) {
                contentStream.close();
                PDPage newPage = new PDPage();
                document.addPage(newPage);
                contentStream = new PDPageContentStream(document, newPage);
                currentHeight = 750;
            }
            writeContentOneLine(contentStream,PDType1Font.HELVETICA, 12, 100, currentHeight, "Anzurechnende Module:");
            currentHeight -= (lineHeight);
            for (ModuleUniEntity moduleUniEntity : moduleUniEntityList) {
                //Check if Page is full
                if (currentHeight < 20) {
                    contentStream.close();
                    PDPage newPage = new PDPage();
                    document.addPage(newPage);
                    contentStream = new PDPageContentStream(document, newPage);
                    currentHeight = 750;
                }
                //Module Uni Data
                writeContentOneLine(contentStream, PDType1Font.HELVETICA, 12, 100, currentHeight, moduleUniEntity.getName());
                currentHeight -= (lineHeight);
            }
            currentHeight -= (2*blockSpacing);
        }
        contentStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        byte[] pdfBytes = outputStream.toByteArray();
        return new ByteArrayResource(pdfBytes);
    }

    private void writeContentOneLine (PDPageContentStream contentStream, PDType1Font font, float fontSize, float tx, float ty, String text) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        contentStream.newLineAtOffset(tx, ty); // Adjust this offset based on your layout
        contentStream.showText(text);
        contentStream.endText();
    }
    private void writeContentMultipleLines (PDPageContentStream contentStream, PDType1Font font, float fontSize, float tx1, float tx2, float ty1, float ty2, ArrayList<String> textLines) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        for (int i=0; i < textLines.size(); i++) {
            if (i == 0) {
                contentStream.newLineAtOffset(tx1, ty1); // Adjust this offset based on your layout
            } else {
                contentStream.newLineAtOffset(tx2, ty2); // Adjust this offset based on your layout
            }
            contentStream.showText(textLines.get(i));
        }
        contentStream.endText();
    }

    public static String formatUUID(String uuid) {
        if (uuid == null || uuid.length() != 36) {
            throw new IllegalArgumentException("Invalid UUID");
        }

        String cleanedUUID = uuid.replace("-", "");

        return cleanedUUID.substring(0, 4) + "-" +
                cleanedUUID.substring(4, 8) + "-" +
                cleanedUUID.substring(8, 12) + "-" +
                cleanedUUID.substring(12, 16) + "-" +
                cleanedUUID.substring(16, 20) + "-" +
                cleanedUUID.substring(20, 24) + "-" +
                cleanedUUID.substring(24, 28) + "-" +
                cleanedUUID.substring(28);
    }
}
