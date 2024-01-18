package com.swtp4.backend.services;

import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.ModuleBlockRepository;
import com.swtp4.backend.repositories.ModuleRelationRepository;
import com.swtp4.backend.repositories.ModuleStudentRepository;
import com.swtp4.backend.repositories.dto.ApplicationDto;
import com.swtp4.backend.repositories.dto.ModuleBlockDto;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.repositories.entities.keyClasses.ModuleRelationKeyClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ApplicationService {

    private ApplicationRepository applicationRepository;
    private ModuleStudentRepository moduleStudentRepository;
    private ModuleBlockRepository moduleBlockRepository;
    private ModuleRelationRepository moduleRelationRepository;

    @Autowired
    public ApplicationService(
            ApplicationRepository applicationRepository,
            ModuleBlockRepository moduleBlockRepository,
            ModuleStudentRepository moduleStudentRepository,
            ModuleRelationRepository moduleRelationRepository) {
        this.applicationRepository = applicationRepository;
        this.moduleBlockRepository = moduleBlockRepository;
        this.moduleStudentRepository = moduleStudentRepository;
        this.moduleRelationRepository = moduleRelationRepository;
    }

    public void save(ApplicationDto applicationDto) {
        //Save ApplicationEntities
        //TODO: Implement processNumberGenerator
        UUID processNumber = UUID.randomUUID();
        ApplicationEntity savedApplicationEntityStudent = saveApplicationEntityStudent(applicationDto, processNumber);
        ApplicationEntity savedApplicationEntityEmployee = saveApplicationEntityEmployee(applicationDto, processNumber)

        //Save ModulesBlocksEntities
        List<ModuleBlockDto> moduleBlockDtos = applicationDto.getModuleFormsData();
        for (ModuleBlockDto moduleBlockDto : moduleBlockDtos) {
            ModuleBlockEntity savedModuleBlockEntityStudent = saveModuleBlockEntity(moduleBlockDto, savedApplicationEntityStudent);
            ModuleBlockEntity savedModuleBlockEntityEmployee = saveModuleBlockEntity(moduleBlockDto, savedApplicationEntityEmployee);

            //Save ModuleStudentEntities
            List<ModuleStudentEntity> modulesStudentEntities = moduleBlockDto.getModulesStudent();
            List<String> modules2bCredited = moduleBlockDto.getModules2bCredited();
            //TODO: Convert List of Strings with ModuleUniversityNames to List of ModulesUniversityLeipzigEntities
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

    private ApplicationEntity saveApplicationEntityStudent(ApplicationDto applicationDto, UUID processNumber) {
        ApplicationEntity applicationEntityStudent = applicationDto.getApplicationData();
        ApplicationKeyClass applicationKeyClassStudent = new ApplicationKeyClass();
        applicationKeyClassStudent.setCreator("Student");
        applicationKeyClassStudent.setId(processNumber);
        applicationEntityStudent.setApplicationKeyClass(applicationKeyClassStudent);
        return applicationRepository.save(applicationEntityStudent);
    }

    private ApplicationEntity saveApplicationEntityEmployee(ApplicationDto applicationDto, UUID processNumber) {
        ApplicationEntity applicationEntityEmployee = applicationDto.getApplicationData();
        ApplicationKeyClass applicationKeyClassEmployee = new ApplicationKeyClass();
        applicationKeyClassEmployee.setCreator("Employee");
        applicationKeyClassEmployee.setId(processNumber);
        applicationEntityEmployee.setApplicationKeyClass(applicationKeyClassEmployee);
        return applicationRepository.save(applicationEntityEmployee);
    }

    private ModuleBlockEntity saveModuleBlockEntity(ModuleBlockDto moduleBlockDto, ApplicationEntity applicationEntity) {
        ModuleBlockEntity moduleBlockEntity = moduleBlockDto.getModuleBlockData();
        moduleBlockEntity.setApplicationEntity(applicationEntity);
        return moduleBlockRepository.save(moduleBlockEntity);
    }

    private ModuleRelationEntity saveModuleRelationEntity(ModuleBlockEntity moduleBlockEntity, ModuleUniEntity moduleUniEntity, ModuleStudentEntity moduleStudentEntity) {
        ModuleRelationEntity moduleRelationEntity = new ModuleRelationEntity();
        moduleRelationEntity.setModuleBlockEntity(moduleBlockEntity);
        ModuleRelationKeyClass moduleRelationKeyClassEmployee = new ModuleRelationKeyClass();
        moduleRelationKeyClassEmployee.setModuleStudentEntity(moduleStudentEntity);
        moduleRelationKeyClassEmployee.setModuleUniEntity(moduleUniEntity);
        moduleRelationEntity.setModuleRelationKeyClass(moduleRelationKeyClassEmployee);
        return moduleRelationRepository.save(moduleRelationEntity);
    }
}
