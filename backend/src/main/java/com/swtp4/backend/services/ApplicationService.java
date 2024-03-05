package com.swtp4.backend.services;

import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.*;
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
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ApplicationService {

    private ApplicationRepository applicationRepository;
    private ModuleStudentRepository moduleStudentRepository;
    private ModuleBlockRepository moduleBlockRepository;
    private ModuleRelationRepository moduleRelationRepository;
    private ModuleUniRepository moduleUniRepository;

    @Autowired
    public ApplicationService(
            ApplicationRepository applicationRepository,
            ModuleBlockRepository moduleBlockRepository,
            ModuleStudentRepository moduleStudentRepository,
            ModuleRelationRepository moduleRelationRepository,
            ModuleUniRepository moduleUniRepository) {
        this.applicationRepository = applicationRepository;
        this.moduleBlockRepository = moduleBlockRepository;
        this.moduleStudentRepository = moduleStudentRepository;
        this.moduleRelationRepository = moduleRelationRepository;
        this.moduleUniRepository = moduleUniRepository;
    }

    @Transactional
    public void save(ApplicationDto applicationDto) {
        //Save ApplicationEntities
        //TODO: Implement processNumberGenerator
        UUID processNumber = UUID.randomUUID();
        ApplicationEntity savedApplicationEntityStudent = saveApplicationEntity(applicationDto.getApplicationData(), processNumber, "Student");
        log.info("SavedApplicationEntityStudent: {}", savedApplicationEntityStudent);
        ApplicationEntity savedApplicationEntityEmployee = saveApplicationEntity(applicationDto.getApplicationData(), processNumber, "Employee");
        log.info("SavedApplicationEntityEmployee: {}", savedApplicationEntityEmployee);

        //Save ModulesBlocksEntities
        List<ModuleBlockDto> moduleBlockDtos = applicationDto.getModuleFormsData();
        for (ModuleBlockDto moduleBlockDto : moduleBlockDtos) {
            ModuleBlockEntity savedModuleBlockEntityStudent = saveModuleBlockEntity(moduleBlockDto.getModuleBlockData(), savedApplicationEntityStudent);
            log.info("SavedModuleBlockEntityStudent: {}", savedModuleBlockEntityStudent);
            ModuleBlockEntity savedModuleBlockEntityEmployee = saveModuleBlockEntity(moduleBlockDto.getModuleBlockData(), savedApplicationEntityEmployee);
            log.info("SavedModuleBlockEntityEmployee: {}", savedModuleBlockEntityEmployee);

            //Save ModuleStudentEntities
            List<ModuleStudentEntity> modulesStudentEntities = moduleBlockDto.getModulesStudent();
            log.info("ModuleStudentEntities: {}", modulesStudentEntities);
            List<String> modules2bCredited = moduleBlockDto.getModules2bCredited();
            log.info("Modules2bCreditedStrings: {}", modules2bCredited);
            List<ModuleUniEntity> modules2bCreditedEntities = getUniversityModulesByName(modules2bCredited);
            log.info("Modules2bCreditedEntities: {}", modules2bCreditedEntities);
            for (ModuleStudentEntity moduleStudentEntity : modulesStudentEntities) {
                ModuleStudentEntity savedModuleStudentEntityStudent = saveModuleStudentEntity(moduleStudentEntity, "Student");
                log.info("SavedModuleStudentEntityStudent: {}", savedModuleStudentEntityStudent);
                ModuleStudentEntity savedModuleStudentEntityEmployee = saveModuleStudentEntity(moduleStudentEntity, "Employee");
                log.info("SavedModuleStudentEntityEmployee: {}", savedModuleStudentEntityEmployee);

                //Save ModulesRelationEntities
                for (ModuleUniEntity moduleUniEntity : modules2bCreditedEntities) {
                    ModuleRelationEntity savedModuleRelationEntityStudent = saveModuleRelationEntity(savedModuleBlockEntityStudent, moduleUniEntity, savedModuleStudentEntityStudent);
                    log.info("SavedModuleRelationEntityStudent: {}", savedModuleRelationEntityStudent);
                    ModuleRelationEntity savedModuleRelationEntityEmployee = saveModuleRelationEntity(savedModuleBlockEntityEmployee, moduleUniEntity, savedModuleStudentEntityEmployee);
                    log.info("SavedModuleRelationEntityEmployee: {}", savedModuleRelationEntityEmployee);
                }
            }
        }
    }

    public ApplicationEntity saveApplicationEntity(ApplicationEntity applicationEntity, UUID processNumber, String creator) {
        ApplicationKeyClass applicationKeyClass = new ApplicationKeyClass();
        applicationKeyClass.setCreator(creator);
        applicationKeyClass.setId(processNumber);
        applicationEntity.setApplicationKeyClass(applicationKeyClass);
        return applicationRepository.save(applicationEntity);
    }

    public ModuleBlockEntity saveModuleBlockEntity(ModuleBlockEntity moduleBlockEntity, ApplicationEntity applicationEntity) {
        ModuleBlockEntity newModuleBlockEntity = new ModuleBlockEntity();
        newModuleBlockEntity.setApplicationEntity(applicationEntity);
        newModuleBlockEntity.setApproval(moduleBlockEntity.getApproval());
        newModuleBlockEntity.setCommentStudent(moduleBlockEntity.getCommentStudent());
        newModuleBlockEntity.setCommentEmployee(moduleBlockEntity.getCommentEmployee());
        log.info("ModuleBlockEntity in save-method: {}", newModuleBlockEntity);
        return moduleBlockRepository.save(newModuleBlockEntity);
    }

    public ModuleStudentEntity saveModuleStudentEntity(ModuleStudentEntity moduleStudentEntity, String creator) {
        ModuleStudentEntity newModuleStudentEntity = new ModuleStudentEntity();
        newModuleStudentEntity.setNumber(moduleStudentEntity.getNumber());
        newModuleStudentEntity.setTitle(moduleStudentEntity.getTitle());
        newModuleStudentEntity.setDescription_pdf(moduleStudentEntity.getDescription_pdf());
        newModuleStudentEntity.setCredits(moduleStudentEntity.getCredits());
        newModuleStudentEntity.setUniversity(moduleStudentEntity.getUniversity());
        newModuleStudentEntity.setMajor(moduleStudentEntity.getMajor());
        newModuleStudentEntity.setCommentStudent(moduleStudentEntity.getCommentStudent());
        newModuleStudentEntity.setCommentEmployee(moduleStudentEntity.getCommentEmployee());
        newModuleStudentEntity.setCreator(creator);
        return moduleStudentRepository.save(newModuleStudentEntity);
    }

    public ModuleRelationEntity saveModuleRelationEntity(ModuleBlockEntity moduleBlockEntity, ModuleUniEntity moduleUniEntity, ModuleStudentEntity moduleStudentEntity) {
        ModuleRelationEntity moduleRelationEntity = new ModuleRelationEntity();
        moduleRelationEntity.setModuleBlockEntity(moduleBlockEntity);
        ModuleRelationKeyClass moduleRelationKeyClassEmployee = new ModuleRelationKeyClass();
        moduleRelationKeyClassEmployee.setModuleStudentEntity(moduleStudentEntity);
        moduleRelationKeyClassEmployee.setModuleUniEntity(moduleUniEntity);
        moduleRelationEntity.setModuleRelationKeyClass(moduleRelationKeyClassEmployee);
        return moduleRelationRepository.save(moduleRelationEntity);
    }

    public List<ModuleUniEntity> getUniversityModulesByName(List<String> moduleStrings) {
        List<ModuleUniEntity> moduleUniEntities = new ArrayList<>();
        for (String moduleString : moduleStrings) {
            ModuleUniEntity moduleUni = moduleUniRepository.findByName(moduleString);
            if (moduleUni == null)
                throw new ResourceNotFoundException("Module "+ moduleString + " not found in Uni Modules");
            moduleUniEntities.add(moduleUni);
//            List<ModuleUniEntity> allUniModules = moduleUniRepository.findAll();
//            log.info("this are all module entities: {}", allUniModules);
        }
        return moduleUniEntities;
    }
}