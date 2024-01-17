package com.swtp4.backend.services;

import com.swtp4.backend.controller.ApplicationController;
import com.swtp4.backend.repositories.ApplicationsRepository;
import com.swtp4.backend.repositories.ModulesBlockRepository;
import com.swtp4.backend.repositories.ModulesRelationRepository;
import com.swtp4.backend.repositories.ModulesStudentRepository;
import com.swtp4.backend.repositories.dto.ApplicationsDTO;
import com.swtp4.backend.repositories.dto.ModulesBlockDto;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationsKeyClass;
import com.swtp4.backend.repositories.entities.keyClasses.ModulesRelationKeyClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ApplicationService {

    private ApplicationsRepository applicationsRepository;
    private ModulesStudentRepository modulesStudentRepository;
    private ModulesBlockRepository modulesBlockRepository;
    private ModulesRelationRepository modulesRelationRepository;

    @Autowired
    public ApplicationService(
            ApplicationsRepository applicationsRepository,
            ModulesBlockRepository modulesBlockRepository,
            ModulesStudentRepository modulesStudentRepository) {
        this.applicationsRepository = applicationsRepository;
        this.modulesBlockRepository = modulesBlockRepository;
        this.modulesStudentRepository = modulesStudentRepository;
    }

    public void save(ApplicationsDTO applicationsDTO) {
        //Save ApplicationEntities
        ApplicationsEntity applicationsEntityStudent = applicationsDTO.getApplicationData();
        ApplicationsEntity applicationsEntityEmployee = applicationsDTO.getApplicationData();
        ApplicationsKeyClass applicationsKeyClassStudent = new ApplicationsKeyClass();
        ApplicationsKeyClass applicationsKeyClassEmployee = new ApplicationsKeyClass();
        applicationsKeyClassStudent.setCreator("Student");
        applicationsKeyClassEmployee.setCreator("Employee");
        UUID processNumber = UUID.randomUUID();
        applicationsKeyClassStudent.setId(processNumber);
        applicationsKeyClassEmployee.setId(processNumber);
        applicationsEntityStudent.setApplicationsKeyClass(applicationsKeyClassStudent);
        applicationsEntityEmployee.setApplicationsKeyClass(applicationsKeyClassEmployee);
        ApplicationsEntity savedApplicationEntityStudent = applicationsRepository.save(applicationsEntityStudent);
        ApplicationsEntity savesApplicationEntityEmployee = applicationsRepository.save(applicationsEntityEmployee);

        //Save ModulesBlocksEntities
        List<ModulesBlockDto> modulesBlockDtos = applicationsDTO.getModuleFormsData();
        for (ModulesBlockDto modulesBlockDto : modulesBlockDtos) {
            ModulesBlockEntity modulesBlockEntityStudent = modulesBlockDto.getModulesBlockEntity();
            ModulesBlockEntity modulesBlockEntityEmployee = modulesBlockDto.getModulesBlockEntity();
            modulesBlockEntityStudent.setApplicationsEntity(savedApplicationEntityStudent);
            modulesBlockEntityEmployee.setApplicationsEntity(savesApplicationEntityEmployee);
            ModulesBlockEntity savedModulesBlockEntityStudent = modulesBlockRepository.save(modulesBlockEntityStudent);
            ModulesBlockEntity savedModulesBlockEntityEmployee = modulesBlockRepository.save(modulesBlockEntityEmployee);

            //Save ModuleStudentEntities
            List<ModulesStudentEntity> modulesStudentEntities = modulesBlockDto.getModulesStudent();
            List<String> modules2bCredited = modulesBlockDto.getModules2bCredited();
            //TODO: Convert List of Strings with ModuleUniversityNames to List of ModulesUniversityLeipzigEntities
            List<ModulesUniversityLeipzigEntity> modules2bCreditedEntities = getUniversityModulesByName(modules2bCredited);
            for (ModulesStudentEntity modulesStudentEntity : modulesStudentEntities) {
                ModulesStudentEntity savedModulesStudentEntityStudent = modulesStudentRepository.save(modulesStudentEntity);
                ModulesStudentEntity savedModulesStudentEntityEmployee = modulesStudentRepository.save(modulesStudentEntity);

                //Save ModulesRelationEntitites
                for (ModulesUniversityLeipzigEntity modulesUniversityLeipzigEntity : modules2bCreditedEntities) {
                    ModulesRelationEntity modulesRelationEntityStudent = new ModulesRelationEntity();
                    ModulesRelationEntity modulesRelationEntityEmployee = new ModulesRelationEntity();
                    modulesRelationEntityStudent.setModulesBlockEntity(savedModulesBlockEntityStudent);
                    modulesRelationEntityEmployee.setModulesBlockEntity(savedModulesBlockEntityEmployee);
                    ModulesRelationKeyClass modulesRelationKeyClassStudent = new ModulesRelationKeyClass();
                    ModulesRelationKeyClass modulesRelationKeyClassEmployee = new ModulesRelationKeyClass();
                    modulesRelationKeyClassStudent.setModulesStudentEntity(savedModulesStudentEntityStudent);
                    modulesRelationKeyClassEmployee.setModulesStudentEntity(savedModulesStudentEntityEmployee);
                    modulesRelationKeyClassStudent.setModulesUniversityLeipzigEntity(modulesUniversityLeipzigEntity);
                    modulesRelationKeyClassEmployee.setModulesUniversityLeipzigEntity(modulesUniversityLeipzigEntity);
                    modulesRelationEntityStudent.setModulesRelationKeyClass(modulesRelationKeyClassStudent);
                    modulesRelationEntityEmployee.setModulesRelationKeyClass(modulesRelationKeyClassEmployee);
                }
            }
        }
    }
}
