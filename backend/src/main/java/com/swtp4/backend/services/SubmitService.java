package com.swtp4.backend.services;

import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.*;
import com.swtp4.backend.repositories.applicationDtos.SubmittedApplicationDto;
import com.swtp4.backend.repositories.applicationDtos.SubmittedBlock;
import com.swtp4.backend.repositories.applicationDtos.SubmittedStudentModule;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.repositories.entities.keyClasses.ModuleRelationKeyClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Slf4j
@Service
public class SubmitService {

    private UniqueNumberService uniqueNumberService;
    private ApplicationRepository applicationRepository;
    private ModuleStudentRepository moduleStudentRepository;
    private ModuleBlockRepository moduleBlockRepository;
    private ModuleRelationRepository moduleRelationRepository;
    private ModuleUniRepository moduleUniRepository;

    @Autowired
    public SubmitService(
            UniqueNumberService uniqueNumberService,
            ApplicationRepository applicationRepository,
            ModuleBlockRepository moduleBlockRepository,
            ModuleStudentRepository moduleStudentRepository,
            ModuleRelationRepository moduleRelationRepository,
            ModuleUniRepository moduleUniRepository) {
        this.uniqueNumberService = uniqueNumberService;
        this.applicationRepository = applicationRepository;
        this.moduleBlockRepository = moduleBlockRepository;
        this.moduleStudentRepository = moduleStudentRepository;
        this.moduleRelationRepository = moduleRelationRepository;
        this.moduleUniRepository = moduleUniRepository;
    }

    public HashMap<String, String> saveSubmitted(SubmittedApplicationDto applicationDto) {
        String applicationID = uniqueNumberService.generateUniqueNumber();
        HashMap<String, String> file_paths = new HashMap<>();

        ApplicationEntity applicationStudent = saveApplicationEntity(applicationID, "Student", applicationDto);
        ApplicationEntity applicationEmployee = saveApplicationEntity(applicationID, "Employee", applicationDto);

        for(SubmittedBlock blockDto : applicationDto.blocks()) {
            ModuleBlockEntity blockStudent = saveModuleBlockEntity(applicationStudent, blockDto);
            ModuleBlockEntity blockEmployee = saveModuleBlockEntity(applicationEmployee, blockDto);

            for (SubmittedStudentModule moduleDto : blockDto.studentModules()) {
                ModuleStudentEntity moduleStudent = saveModuleStudentEntity("Student", moduleDto, "");

                // paths "/applicationID/S-moduleStudentID"
                String pdf_path = "/" + applicationID + "/S-" + moduleStudent.getId().toString();
                moduleStudent.setDescription_pdf(pdf_path);
                moduleStudentRepository.save(moduleStudent);
                // multipart file fields file-0:0, file-0:1...
                file_paths.put("file-" + blockDto.frontend_block_key() + ":" + moduleDto.frontend_module_key(), pdf_path);

                ModuleStudentEntity moduleEmployee = saveModuleStudentEntity("Employee", moduleDto, pdf_path);

                for(Integer uniModuleID : blockDto.uniModulesIDs()) {
                    saveModuleRelationEntity(blockStudent, moduleStudent, uniModuleID);
                    saveModuleRelationEntity(blockEmployee, moduleEmployee, uniModuleID);
                }

            }
        }

        return file_paths;
    }

    private ApplicationEntity saveApplicationEntity(String applicationID, String creator, SubmittedApplicationDto applicationDto) {
        return applicationRepository.save(ApplicationEntity.builder()
                .applicationKeyClass(ApplicationKeyClass.builder()
                        .id(applicationID)
                        .creator(creator).build())
                .dateOfSubmission(applicationDto.dateOfSubmission())
                .dateLastEdited(applicationDto.dateLastEdited())
                .build());
    }

    private ModuleBlockEntity saveModuleBlockEntity(ApplicationEntity applicationStudent, SubmittedBlock blockDto) {
        return moduleBlockRepository.save(ModuleBlockEntity.builder()
                .applicationEntity(applicationStudent)
                .frontendKey(blockDto.frontend_block_key())
                .build());
    }

    private ModuleStudentEntity saveModuleStudentEntity(String creator, SubmittedStudentModule moduleDto, String path) {
        return moduleStudentRepository.save(ModuleStudentEntity.builder()
                .creator(creator)
                .number(moduleDto.moduleNumber())
                .title(moduleDto.moduleName())
                .credits(moduleDto.credits())
                .university(moduleDto.university())
                .major(moduleDto.moduleMajor())
                .commentStudent(moduleDto.commentStudent())
                .frontendKey(moduleDto.frontend_module_key())
                .description_pdf(path)
                .build());
    }

    private void saveModuleRelationEntity(ModuleBlockEntity block, ModuleStudentEntity studentModule, Integer uniModuleID) {
        ModuleUniEntity uniModule = moduleUniRepository.findById(uniModuleID.longValue())
                .orElseThrow(() -> new ResourceNotFoundException("Major not found. The University Module with ID " + uniModuleID + " does not exist and therefore can't be credited."));
        moduleRelationRepository.save(ModuleRelationEntity.builder()
                .moduleBlockEntity(block)
                .moduleRelationKeyClass(ModuleRelationKeyClass.builder()
                        .moduleStudentEntity(studentModule)
                        .moduleUniEntity(uniModule)
                        .build())
                .build());
    }

}
