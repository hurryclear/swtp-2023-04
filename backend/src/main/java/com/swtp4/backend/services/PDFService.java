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

    //saved Module description pdf files in Docker volume for persistence, used by submitApplication Endpoint
    public void saveModulePDFs(Map<String, MultipartFile> fileMap, HashMap<String, String> file_paths) {
        //fileMapEntry = filename, pdf-file
        //filePathEntry = filename, filePath where the pdf should be saved
        for (Map.Entry<String, MultipartFile> entry : fileMap.entrySet()) {
            String fileKey = entry.getKey();
            MultipartFile file = entry.getValue();
            String filePath = file_paths.get(fileKey);
            if (file != null && !file.isEmpty()) {
                try {
                    //write PDF file in volume (created parent directories, if they did not exist - every application has an own directory
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

    //method for getPDF Endpoint, retrieves PDF file from Docker volume
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

    //PDF Generator for Application summary for the student, used by getPDFSummary Endpoint
    public Resource generatePDFForApplication(String applicationId) throws IOException {
        //Get application entity
        ApplicationEntity applicationEntity = applicationRepository.findById(ApplicationKeyClass.builder()
                        .creator("Employee")
                        .id(applicationId).build())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found: " + applicationId));

        //Initialize PDF Document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        float currentHeight = 750; //used to control line-placement

        //Headline summary
        currentHeight = writeContentOneLine(contentStream, PDType1Font.HELVETICA_BOLD, 20, 100, currentHeight, 25, "Zusammenfassung Antrag");
        //Application ID
        currentHeight = writeContentOneLine(contentStream, PDType1Font.HELVETICA, 12, 100, currentHeight, 15,"Antragsnummer: " + applicationId);
        currentHeight -= 30;
        float lineHeight = 15; //used to control line-spacing

        //Application Data is written
        ArrayList<String> textLinesApplication = new ArrayList<>(Arrays.asList(
                "Status: " + applicationEntity.getStatus(),
                "Einreichungsdatum: " + applicationEntity.getDateOfSubmission().toString(),
                "Universitätsname: " + applicationEntity.getUniversityName(),
                "Studienfach: " + applicationEntity.getStudentMajor(),
                "Uni-Fach: " + applicationEntity.getUniMajor()));
        if (applicationEntity.getStatus().equals("formally rejected")) {
            textLinesApplication.add("Formaler Ablehnungsgrund: " + applicationEntity.getFormalRejectionReason());
        }
        currentHeight = writeContentMultipleLines(contentStream, PDType1Font.HELVETICA, 12, 100, currentHeight, lineHeight, textLinesApplication);

        //Module Blocks and Modules Height Spacing
        float blockSpacing = 20; //spacing between different sections
        float moduleSpacing = 15; //line-spacing
        currentHeight -= 2*blockSpacing;

        //Get all ModuleBlocks of the application
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
            currentHeight = writeContentOneLine(contentStream, PDType1Font.HELVETICA_BOLD, 14, 100, currentHeight, lineHeight, "Zuordnung " + (moduleBlockEntity.getFrontendKey() + 1) + ":");
            currentHeight -= moduleSpacing;

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
                //ModuleStudent Data is written
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
                currentHeight = writeContentMultipleLines(contentStream ,PDType1Font.HELVETICA, 12, 100, currentHeight, moduleSpacing, textLinesModuleStudent);
                currentHeight -= moduleSpacing;
            }

            //Check if Page is full
            if (currentHeight < 20) {
                contentStream.close();
                PDPage newPage = new PDPage();
                document.addPage(newPage);
                contentStream = new PDPageContentStream(document, newPage);
                currentHeight = 750;
            }
            currentHeight = writeContentOneLine(contentStream,PDType1Font.HELVETICA_BOLD, 12, 100, currentHeight, lineHeight, "Anzurechnende Module:");
            for (ModuleUniEntity moduleUniEntity : moduleUniEntityList) {
                //Check if Page is full
                if (currentHeight < 20) {
                    contentStream.close();
                    PDPage newPage = new PDPage();
                    document.addPage(newPage);
                    contentStream = new PDPageContentStream(document, newPage);
                    currentHeight = 750;
                }
                //Module Uni Data is written
                currentHeight = writeContentOneLine(contentStream, PDType1Font.HELVETICA, 12, 100, currentHeight, lineHeight, moduleUniEntity.getName());
            }
            currentHeight -= (2*blockSpacing);
        }
        contentStream.close();
        //Return PDF file as Resource
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        byte[] pdfBytes = outputStream.toByteArray();
        return new ByteArrayResource(pdfBytes);
    }

    //Method do write one Line in content-stream
    private float writeContentOneLine (PDPageContentStream contentStream, PDType1Font font, float fontSize, float tx, float currentHeight, float lineHeight, String text) throws IOException {
        contentStream.beginText();
        contentStream.setFont(font, fontSize);
        //Split textLine into words
        String[] words = text.split(" ");
        StringBuilder line = new StringBuilder();
        float ty = currentHeight;
        //Check if line is full
        for (String word : words) {
            float lineWidth = font.getStringWidth(line.toString() + " " + word) /  1000 * fontSize;
            if (lineWidth < (600 - tx)) {
                //no - append word to line
                if (!line.toString().isEmpty()) {
                    line.append(" ");
                }
                line.append(word);
            } else {
                //yes - write line in content-stream and start next line
                contentStream.newLineAtOffset(tx, ty);
                contentStream.showText(line.toString());
                ty -= lineHeight;
                contentStream.endText();
                contentStream.beginText();
                contentStream.setFont(font, fontSize);
                line = new StringBuilder();
                while (word.length() > 50) {
                    int midIndex = word.length() / 2;
                    line.append(word.substring(0, midIndex));
                    contentStream.newLineAtOffset(tx, ty);
                    contentStream.showText(line.toString());
                    ty -= lineHeight;
                    word = word.substring(midIndex);
                    contentStream.endText();
                    contentStream.beginText();
                    contentStream.setFont(font, fontSize);
                    line = new StringBuilder();
                }
                line.append(word);
            }
        }
        contentStream.newLineAtOffset(tx, ty);
        contentStream.showText(line.toString());
        ty -= lineHeight;
        contentStream.endText();
        return ty;
    }

    //method do write multiple lines in content-stream
    private float writeContentMultipleLines (PDPageContentStream contentStream, PDType1Font font, float fontSize, float tx1, float currentHeight, float lineHeight, ArrayList<String> textLines) throws IOException {
        float ty = currentHeight;
        //iterate over textLines and uses single method do write lines
        for (int i=0; i < textLines.size(); i++) {
            ty = writeContentOneLine(contentStream, font, fontSize, tx1, ty, lineHeight, textLines.get(i));
        }
        return ty;
    }
}
