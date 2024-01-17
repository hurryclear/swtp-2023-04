package com.swtp4.backend.services;

import com.swtp4.backend.controller.UniversityDataController;
import com.swtp4.backend.repositories.MajorsUniversityLeipzigRepository;
import com.swtp4.backend.repositories.ModulesUniversityLeipzigRepository;
import com.swtp4.backend.repositories.dto.MajorsUniversityLeipzigDto;
import com.swtp4.backend.repositories.dto.UniDataDto;
import com.swtp4.backend.repositories.entities.MajorsUniversityLeipzigEntity;
import com.swtp4.backend.repositories.entities.ModulesUniversityLeipzigEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniversityDataService {

    private ModulesUniversityLeipzigRepository modulesUniversityLeipzigRepository;
    private MajorsUniversityLeipzigRepository majorsUniversityLeipzigRepository;

    @Autowired
    public UniversityDataService(
            ModulesUniversityLeipzigRepository modulesUniversityLeipzigRepository,
            MajorsUniversityLeipzigRepository majorsUniversityLeipzigRepository) {
        this.modulesUniversityLeipzigRepository = modulesUniversityLeipzigRepository;
        this.majorsUniversityLeipzigRepository = majorsUniversityLeipzigRepository;
    }

    public void update(UniDataDto uniDataDto) {
        //iteration throught the courses of the JSON (majors)
        for (MajorsUniversityLeipzigDto majorsUniversityLeipzigDto : uniDataDto.getCourses()) {
            MajorsUniversityLeipzigEntity existingMajorsUniversityLeipzigEntity = majorsUniversityLeipzigRepository.findByName(majorsUniversityLeipzigDto.getName());
            if (existingMajorsUniversityLeipzigEntity == null) {
                //create MajorEntity
                MajorsUniversityLeipzigEntity majorsUniversityLeipzigEntity = new MajorsUniversityLeipzigEntity();
                majorsUniversityLeipzigEntity.setName(majorsUniversityLeipzigDto.getName());
                existingMajorsUniversityLeipzigEntity = majorsUniversityLeipzigRepository.save(majorsUniversityLeipzigEntity);
            }
            //iteration through the modules of the courses of the JSON
            for (ModulesUniversityLeipzigEntity modulesUniversityLeipzigEntity : majorsUniversityLeipzigDto.getModules()) {
                ModulesUniversityLeipzigEntity existingModulesUniversityLeipzigEntity = modulesUniversityLeipzigRepository.findById(modulesUniversityLeipzigEntity.getNumber());
                if (existingModulesUniversityLeipzigEntity == null) {
                    //create ModuleEntity
                    modulesUniversityLeipzigEntity.setMajorsUniversityLeipzigEntity(existingMajorsUniversityLeipzigEntity);
                    ModulesUniversityLeipzigEntity savedModuleUniversityLeipzigEntity = modulesUniversityLeipzigRepository.save(modulesUniversityLeipzigEntity);
                } else {
                    //update ModuleEntity
                    existingModulesUniversityLeipzigEntity.setName(modulesUniversityLeipzigEntity.getName());
                    existingModulesUniversityLeipzigEntity.setNumber(modulesUniversityLeipzigEntity.getNumber());
                    existingModulesUniversityLeipzigEntity.setMajorsUniversityLeipzigEntity(existingMajorsUniversityLeipzigEntity);
                    ModulesUniversityLeipzigEntity savedModulesUniversityLeipzigEntity = modulesUniversityLeipzigRepository.save(existingModulesUniversityLeipzigEntity);
                }
            }
        }
    }
}
