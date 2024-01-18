package com.swtp4.backend.services;

import com.swtp4.backend.repositories.MajorUniRepository;
import com.swtp4.backend.repositories.ModuleUniRepository;
import com.swtp4.backend.repositories.dto.MajorUniDto;
import com.swtp4.backend.repositories.dto.UniDataDto;
import com.swtp4.backend.repositories.entities.MajorUniEntity;
import com.swtp4.backend.repositories.entities.ModuleUniEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UniversityDataService {

    private ModuleUniRepository moduleUniRepository;
    private MajorUniRepository majorUniRepository;

    @Autowired
    public UniversityDataService(
            ModuleUniRepository moduleUniRepository,
            MajorUniRepository majorUniRepository) {
        this.moduleUniRepository = moduleUniRepository;
        this.majorUniRepository = majorUniRepository;
    }

    public void update(UniDataDto uniDataDto) {
        //iteration throught the courses of the JSON (majors)
        for (MajorUniDto majorUniDto : uniDataDto.getCourses()) {
            MajorUniEntity existingMajorUniEntity = majorUniRepository.findByName(majorUniDto.getName());
            MajorUniEntity savedMajorUniEntity = existingMajorUniEntity;
            if (existingMajorUniEntity == null) {
                //create MajorEntity
                MajorUniEntity newMajorUniEntity = new MajorUniEntity();
                newMajorUniEntity.setName(majorUniDto.getName());
                savedMajorUniEntity = majorUniRepository.save(newMajorUniEntity);
            }
            //iteration through the modules of the courses of the JSON
            for (ModuleUniEntity moduleUniEntity : majorUniDto.getModules()) {
                ModuleUniEntity existingModuleUniEntity = moduleUniRepository.findByNumber(moduleUniEntity.getNumber());
                if (existingModuleUniEntity == null) {
                    //create ModuleEntity
                    moduleUniEntity.setMajorUniEntity(savedMajorUniEntity);
                    ModuleUniEntity savedModuleUniEntity = moduleUniRepository.save(moduleUniEntity);
                } else {
                    //update ModuleEntity
                    existingModuleUniEntity.setName(moduleUniEntity.getName());
                    existingModuleUniEntity.setNumber(moduleUniEntity.getNumber());
                    existingModuleUniEntity.setMajorUniEntity(savedMajorUniEntity);
                    ModuleUniEntity savedModuleUniEntity = moduleUniRepository.save(existingModuleUniEntity);
                }
            }
        }
    }
}
