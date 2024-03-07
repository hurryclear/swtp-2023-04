package com.swtp4.backend.services;

import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.*;
import com.swtp4.backend.repositories.applicationDtos.EditedApplicationDto;
import com.swtp4.backend.repositories.applicationDtos.EditedBlock;
import com.swtp4.backend.repositories.applicationDtos.EditedStudentModule;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.repositories.entities.keyClasses.ModuleRelationKeyClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class EditedService {

    private UniqueNumberService uniqueNumberService;
    private ApplicationRepository applicationRepository;
    private ModuleStudentRepository moduleStudentRepository;
    private ModuleBlockRepository moduleBlockRepository;
    private ModuleRelationRepository moduleRelationRepository;
    private ModuleUniRepository moduleUniRepository;

    @Autowired
    public EditedService(
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
        moduleEmployee.setApproval(moduleDto.approval());
        moduleEmployee.setApprovalReason(moduleDto.reason());
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
        applicationEmployee.setFormalRejectionReason(applicationDto.formalRejection());
        return applicationRepository.save(applicationEmployee);
    }
}
