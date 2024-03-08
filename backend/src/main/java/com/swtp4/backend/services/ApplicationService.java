package com.swtp4.backend.services;

import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.*;
import com.swtp4.backend.repositories.applicationDtos.*;
import com.swtp4.backend.repositories.dto.ApplicationDto;
import com.swtp4.backend.repositories.dto.ModuleBlockDto;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.repositories.entities.keyClasses.ModuleRelationKeyClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class ApplicationService {

    private UniqueNumberService uniqueNumberService;
    private ApplicationRepository applicationRepository;
    private ModuleStudentRepository moduleStudentRepository;
    private ModuleBlockRepository moduleBlockRepository;
    private ModuleRelationRepository moduleRelationRepository;
    private ModuleUniRepository moduleUniRepository;

    @Autowired
    public ApplicationService(
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

                for(Long uniModuleID : blockDto.uniModulesIDs()) {
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
                .status("offen")
                .formalRejectionReason("")
                .dateOfSubmission(applicationDto.dateOfSubmission())
                .dateLastEdited(applicationDto.dateLastEdited())
                .universityName(applicationDto.university())
                .studentMajor(applicationDto.oldCourseOfStudy())
                .uniMajor(applicationDto.newCourseOfStudy())
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
                .frontendKey(moduleDto.frontend_module_key())
                .approval("")
                .approvalReason("")
                .number(moduleDto.moduleNumber())
                .title(moduleDto.moduleName())
                .credits(moduleDto.credits())
                .university(moduleDto.university())
                .major(moduleDto.moduleMajor())
                .commentStudent(moduleDto.commentStudent())
                .commentEmployee("")
                .description_pdf(path)
                .creator(creator)
                .build());
    }

    private void saveModuleRelationEntity(ModuleBlockEntity block, ModuleStudentEntity studentModule, Long uniModuleID) {
        ModuleUniEntity uniModule = moduleUniRepository.findById(uniModuleID)
                .orElseThrow(() -> new ResourceNotFoundException("Major not found. The University Module with ID " + uniModuleID + " does not exist and therefore can't be credited."));
        if(!uniModule.getVisibleChoice()){
            throw new ResourceNotFoundException("Module outdated and can't be chosen.");
        }

        moduleRelationRepository.save(ModuleRelationEntity.builder()
                .moduleBlockEntity(block)
                .moduleRelationKeyClass(ModuleRelationKeyClass.builder()
                        .moduleStudentEntity(studentModule)
                        .moduleUniEntity(uniModule)
                        .build())
                .build());
    }

    public void updateApplication(EditedApplicationDto applicationDto) {
        ApplicationEntity applicationEmployee = updateApplicationEntity(applicationDto);

        for(EditedBlock blockDto : applicationDto.editedBlocks()) {
            ModuleBlockEntity blockEmployee = updateModuleBlockEntity(blockDto, applicationEmployee);

            for(EditedStudentModule moduleDto : blockDto.editedModules()) {
                ModuleStudentEntity moduleEmployee = updateModuleStudentEntity(moduleDto);

                List<ModuleRelationEntity> moduleRelationEntities = moduleRelationRepository
                        .findAllByModuleRelationKeyClass_ModuleStudentEntity(moduleEmployee);

                // delete ONLY relations which contain studentModules with changed blocks
                for (ModuleRelationEntity moduleRelation : moduleRelationEntities) {
                    Long oldBlockID = moduleRelation.getModuleBlockEntity().getId();
                    Long oldUniModuleID = moduleRelation.getModuleRelationKeyClass().getModuleUniEntity().getId();
                    if (!Objects.equals(oldBlockID, blockEmployee.getId()) ||
                            !blockDto.uniModuleIDs().contains(oldUniModuleID)) {
                        moduleRelationRepository.delete(moduleRelation);
                    }
                }

                for(Long newUniModuleID: blockDto.uniModuleIDs()){
                    updateModuleRelationEntity(blockEmployee, moduleEmployee, newUniModuleID);
                }
            }
        }
    }

    private void updateModuleRelationEntity(ModuleBlockEntity blockEmployee, ModuleStudentEntity moduleEmployee, Long newUniModuleID) {
        ModuleUniEntity moduleUni = moduleUniRepository.findById(newUniModuleID)
                .orElseThrow(() -> new ResourceNotFoundException("Major with ID " + newUniModuleID + "not found"));
        // TODO: vllt muss man erst suchen ob es das schon gibt und dann anpassen
        moduleRelationRepository.save(ModuleRelationEntity.builder()
                .moduleBlockEntity(blockEmployee)
                .moduleRelationKeyClass(ModuleRelationKeyClass.builder()
                        .moduleStudentEntity(moduleEmployee)
                        .moduleUniEntity(moduleUni)
                        .build())
                .build());
    }

    private ModuleStudentEntity updateModuleStudentEntity(EditedStudentModule moduleDto) {
        ModuleStudentEntity moduleEmployee = moduleStudentRepository.findByIdAndCreator(moduleDto.moduleID(), "Employee")
                .orElseThrow(() -> new ResourceNotFoundException("StundentModule with Id "
                        + moduleDto.moduleID() + " not found or creator isn't Employee"));

        moduleEmployee.setFrontendKey(moduleDto.frontendKey());
        moduleEmployee.setNumber(moduleDto.number());
        moduleEmployee.setTitle(moduleDto.title());
        moduleEmployee.setCredits(moduleDto.credits());
        moduleEmployee.setUniversity(moduleDto.university());
        moduleEmployee.setMajor(moduleDto.moduleMajor());
        moduleEmployee.setCommentStudent(moduleDto.commentStudent());
        moduleEmployee.setCommentEmployee(moduleDto.commentEmployee());
        return moduleStudentRepository.save(moduleEmployee);
    }

    private ModuleBlockEntity updateModuleBlockEntity(EditedBlock blockDto, ApplicationEntity applicationEmployee) {
        ModuleBlockEntity blockEmployee;
        if (blockDto.blockID() == null) {
            blockEmployee = ModuleBlockEntity.builder().applicationEntity(applicationEmployee).build();
        }
        else {
            blockEmployee = moduleBlockRepository.findById(blockDto.blockID())
                    .orElseThrow(() -> new ResourceNotFoundException("ModuleBlock with blockID "
                            + blockDto.blockID() + " not found, and therefore can't be updated."));
        }

        blockEmployee.setFrontendKey(blockDto.frontendKey());
        return moduleBlockRepository.save(blockEmployee);
    }

    private ApplicationEntity updateApplicationEntity(EditedApplicationDto applicationDto) {
        ApplicationEntity applicationEmployee = applicationRepository.findById(ApplicationKeyClass.builder()
                        .id(applicationDto.applicationID())
                        .creator("Employee")
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("Application with applicationID "
                        + applicationDto.applicationID() + " not found, and therefore can't be updated."));

        applicationEmployee.setDateLastEdited(applicationDto.dateLastEdited());
        applicationEmployee.setUniversityName(applicationDto.university());
        applicationEmployee.setStudentMajor(applicationDto.oldCourseOfStudy());
        return applicationRepository.save(applicationEmployee);
    }

















//
//    @Transactional
//    public void save(ApplicationDto applicationDto) {
//        //Save ApplicationEntities
//        //TODO: Implement processNumberGenerator
//        String processNumber = uniqueNumberService.generateUniqueNumber();
//        ApplicationEntity savedApplicationEntityStudent = saveApplicationEntity(applicationDto.getApplicationData(), processNumber, "Student");
//        log.info("SavedApplicationEntityStudent: {}", savedApplicationEntityStudent);
//        ApplicationEntity savedApplicationEntityEmployee = saveApplicationEntity(applicationDto.getApplicationData(), processNumber, "Employee");
//        log.info("SavedApplicationEntityEmployee: {}", savedApplicationEntityEmployee);
//
//        //Save ModulesBlocksEntities
//        List<ModuleBlockDto> moduleBlockDtos = applicationDto.getModuleFormsData();
//        for (ModuleBlockDto moduleBlockDto : moduleBlockDtos) {
//            ModuleBlockEntity savedModuleBlockEntityStudent = saveModuleBlockEntity(moduleBlockDto.getModuleBlockData(), savedApplicationEntityStudent);
//            log.info("SavedModuleBlockEntityStudent: {}", savedModuleBlockEntityStudent);
//            ModuleBlockEntity savedModuleBlockEntityEmployee = saveModuleBlockEntity(moduleBlockDto.getModuleBlockData(), savedApplicationEntityEmployee);
//            log.info("SavedModuleBlockEntityEmployee: {}", savedModuleBlockEntityEmployee);
//
//            //Save ModuleStudentEntities
//            List<ModuleStudentEntity> modulesStudentEntities = moduleBlockDto.getModulesStudent();
//            log.info("ModuleStudentEntities: {}", modulesStudentEntities);
//            List<String> modules2bCredited = moduleBlockDto.getModules2bCredited();
//            log.info("Modules2bCreditedStrings: {}", modules2bCredited);
//            List<ModuleUniEntity> modules2bCreditedEntities = getUniversityModulesByName(modules2bCredited);
//            log.info("Modules2bCreditedEntities: {}", modules2bCreditedEntities);
//            for (ModuleStudentEntity moduleStudentEntity : modulesStudentEntities) {
//                ModuleStudentEntity savedModuleStudentEntityStudent = saveModuleStudentEntity(moduleStudentEntity, "Student");
//                log.info("SavedModuleStudentEntityStudent: {}", savedModuleStudentEntityStudent);
//                ModuleStudentEntity savedModuleStudentEntityEmployee = saveModuleStudentEntity(moduleStudentEntity, "Employee");
//                log.info("SavedModuleStudentEntityEmployee: {}", savedModuleStudentEntityEmployee);
//
//                //Save ModulesRelationEntities
//                for (ModuleUniEntity moduleUniEntity : modules2bCreditedEntities) {
//                    ModuleRelationEntity savedModuleRelationEntityStudent = saveModuleRelationEntity(savedModuleBlockEntityStudent, moduleUniEntity, savedModuleStudentEntityStudent);
//                    log.info("SavedModuleRelationEntityStudent: {}", savedModuleRelationEntityStudent);
//                    ModuleRelationEntity savedModuleRelationEntityEmployee = saveModuleRelationEntity(savedModuleBlockEntityEmployee, moduleUniEntity, savedModuleStudentEntityEmployee);
//                    log.info("SavedModuleRelationEntityEmployee: {}", savedModuleRelationEntityEmployee);
//                }
//            }
//        }
//    }
//
//    //TODO: Entity mit DATes würde passen
//    public ApplicationEntity saveApplicationEntity(ApplicationEntity applicationEntity, String processNumber, String creator) {
//        ApplicationKeyClass applicationKeyClass = new ApplicationKeyClass();
//        applicationKeyClass.setCreator(creator);
//        applicationKeyClass.setId(processNumber);
//        applicationEntity.setApplicationKeyClass(applicationKeyClass);
//        return applicationRepository.save(applicationEntity);
//    }
//
//    public ModuleBlockEntity saveModuleBlockEntity(ModuleBlockEntity moduleBlockEntity, ApplicationEntity applicationEntity) {
//        ModuleBlockEntity newModuleBlockEntity = new ModuleBlockEntity();
//        newModuleBlockEntity.setApplicationEntity(applicationEntity);
////        newModuleBlockEntity.setApproval(moduleBlockEntity.getApproval());
////        newModuleBlockEntity.setCommentStudent(moduleBlockEntity.getCommentStudent());
////        newModuleBlockEntity.setCommentEmployee(moduleBlockEntity.getCommentEmployee());
//        log.info("ModuleBlockEntity in save-method: {}", newModuleBlockEntity);
//        return moduleBlockRepository.save(newModuleBlockEntity);
//    }
//
//    public ModuleStudentEntity saveModuleStudentEntity(ModuleStudentEntity moduleStudentEntity, String creator) {
//        ModuleStudentEntity newModuleStudentEntity = new ModuleStudentEntity();
//        newModuleStudentEntity.setNumber(moduleStudentEntity.getNumber());
//        newModuleStudentEntity.setTitle(moduleStudentEntity.getTitle());
//        newModuleStudentEntity.setDescription_pdf(moduleStudentEntity.getDescription_pdf());
//        newModuleStudentEntity.setCredits(moduleStudentEntity.getCredits());
//        newModuleStudentEntity.setUniversity(moduleStudentEntity.getUniversity());
//        newModuleStudentEntity.setMajor(moduleStudentEntity.getMajor());
//        newModuleStudentEntity.setCommentStudent(moduleStudentEntity.getCommentStudent());
//        newModuleStudentEntity.setCommentEmployee(moduleStudentEntity.getCommentEmployee());
//        newModuleStudentEntity.setCreator(creator);
//        return moduleStudentRepository.save(newModuleStudentEntity);
//    }
//
//    public ModuleRelationEntity saveModuleRelationEntity(ModuleBlockEntity moduleBlockEntity, ModuleUniEntity moduleUniEntity, ModuleStudentEntity moduleStudentEntity) {
//        ModuleRelationEntity moduleRelationEntity = new ModuleRelationEntity();
//        moduleRelationEntity.setModuleBlockEntity(moduleBlockEntity);
//        ModuleRelationKeyClass moduleRelationKeyClassEmployee = new ModuleRelationKeyClass();
//        moduleRelationKeyClassEmployee.setModuleStudentEntity(moduleStudentEntity);
//        moduleRelationKeyClassEmployee.setModuleUniEntity(moduleUniEntity);
//        moduleRelationEntity.setModuleRelationKeyClass(moduleRelationKeyClassEmployee);
//        return moduleRelationRepository.save(moduleRelationEntity);
//    }
//
//    public List<ModuleUniEntity> getUniversityModulesByName(List<String> moduleStrings) {
//        List<ModuleUniEntity> moduleUniEntities = new ArrayList<>();
//        for (String moduleString : moduleStrings) {
//            ModuleUniEntity moduleUni = moduleUniRepository.findByName(moduleString);
//            if (moduleUni == null)
//                throw new ResourceNotFoundException("Module "+ moduleString + " not found in Uni Modules");
//            moduleUniEntities.add(moduleUni);
////            List<ModuleUniEntity> allUniModules = moduleUniRepository.findAll();
////            log.info("this are all module entities: {}", allUniModules);
//        }
//        return moduleUniEntities;
//    }
//


}