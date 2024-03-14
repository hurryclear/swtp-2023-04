package com.swtp4.backend.services;


import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
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
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@Service
public class PDFService {


    private ApplicationRepository applicationRepository;

    public PDFService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
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


    public ResponseEntity <byte[]>generatePDFForApplication(String applicationId) throws IOException {
        ApplicationEntity applicationEntity = applicationRepository.findById(ApplicationKeyClass.builder()
                .creator("Employee")
                .id(applicationId).build()).orElseThrow(()-> new ResourceNotFoundException("Application not found: " + applicationId));

        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);


        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(100, 700);

        ApplicationKeyClass applicationKeyClass = applicationEntity.getApplicationKeyClass();
        contentStream.showText("Antragsnummer: " + applicationKeyClass.toString());
        contentStream.newLine();
        contentStream.showText("Status: " + applicationEntity.getStatus());
        contentStream.newLine();
        contentStream.showText("Einreichungsdatum: " + applicationEntity.getDateOfSubmission().toString());
        contentStream.newLine();
        contentStream.showText("Letzte Bearbeitung: " + applicationEntity.getDateLastEdited().toString());
        contentStream.newLine();
        contentStream.showText("Universitätsname: " + applicationEntity.getUniversityName());
        contentStream.newLine();
        contentStream.showText("Studienfach: " + applicationEntity.getStudentMajor());
        contentStream.newLine();
        contentStream.showText("Uni-Fach: " + applicationEntity.getUniMajor());
        contentStream.newLine();
        contentStream.showText("Formaler Ablehnungsgrund: " + (applicationEntity.getFormalRejectionReason() != null ? applicationEntity.getFormalRejectionReason() : "Keine"));

        contentStream.endText();
        contentStream.close();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();
        byte [] pdfBytes = outputStream.toByteArray();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filepath = " + applicationId + ".pdf")
                .body(pdfBytes);
    }
}
