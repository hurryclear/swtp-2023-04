package com.swtp4.backend.services;

import com.swtp4.backend.repositories.*;
import com.swtp4.backend.repositories.dto.ApplicationDto;
import com.swtp4.backend.repositories.dto.ModuleBlockDto;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.repositories.entities.keyClasses.ModuleRelationKeyClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public void save(ApplicationDto applicationDto) {
        //Save ApplicationEntities
        //TODO: Implement processNumberGenerator
        UUID processNumber = UUID.randomUUID();
        ApplicationEntity savedApplicationEntityStudent = saveApplicationEntity(applicationDto.getApplicationData(), processNumber, "Student");
        ApplicationEntity savedApplicationEntityEmployee = saveApplicationEntity(applicationDto.getApplicationData(), processNumber, "Creator");

        //Save ModulesBlocksEntities
        List<ModuleBlockDto> moduleBlockDtos = applicationDto.getModuleFormsData();
        for (ModuleBlockDto moduleBlockDto : moduleBlockDtos) {
            ModuleBlockEntity savedModuleBlockEntityStudent = saveModuleBlockEntity(moduleBlockDto.getModuleBlockData(), savedApplicationEntityStudent);
            ModuleBlockEntity savedModuleBlockEntityEmployee = saveModuleBlockEntity(moduleBlockDto.getModuleBlockData(), savedApplicationEntityEmployee);

            //Save ModuleStudentEntities
            List<ModuleStudentEntity> modulesStudentEntities = moduleBlockDto.getModulesStudent();
            List<String> modules2bCredited = moduleBlockDto.getModules2bCredited();
            List<ModuleUniEntity> modules2bCreditedEntities = getUniversityModulesByName(modules2bCredited);
            for (ModuleStudentEntity moduleStudentEntity : modulesStudentEntities) {
                ModuleStudentEntity savedModuleStudentEntityStudent = moduleStudentRepository.save(moduleStudentEntity);
                ModuleStudentEntity savedModuleStudentEntityEmployee = moduleStudentRepository.save(moduleStudentEntity);

                //Save ModulesRelationEntities
                for (ModuleUniEntity moduleUniEntity : modules2bCreditedEntities) {
                    ModuleRelationEntity savedModuleRelationEntityStudent = saveModuleRelationEntity(savedModuleBlockEntityStudent, moduleUniEntity, savedModuleStudentEntityStudent);
                    ModuleRelationEntity savedModuleRelationEntityEmployee = saveModuleRelationEntity(savedModuleBlockEntityEmployee, moduleUniEntity, savedModuleStudentEntityEmployee);
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
        moduleBlockEntity.setApplicationEntity(applicationEntity);
        return moduleBlockRepository.save(moduleBlockEntity);
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
            moduleUniEntities.add(moduleUniRepository.findByName(moduleString));
        }
        return moduleUniEntities;
    }
}