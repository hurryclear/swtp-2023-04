package com.swtp4.backend.services;

import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.MajorUniRepository;
import com.swtp4.backend.repositories.ModuleUniRepository;
import com.swtp4.backend.repositories.dto.*;
import com.swtp4.backend.repositories.entities.MajorUniEntity;
import com.swtp4.backend.repositories.entities.ModuleUniEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UniDataService {

    private ModuleUniRepository moduleUniRepository;
    private MajorUniRepository majorUniRepository;

    @Autowired
    public UniDataService(
            ModuleUniRepository moduleUniRepository,
            MajorUniRepository majorUniRepository) {
        this.moduleUniRepository = moduleUniRepository;
        this.majorUniRepository = majorUniRepository;
    }

    public void update(UniDataDto uniDataDto) {
        //iteration through the courses of the JSON (majors)
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
                ModuleUniEntity existingModuleUniEntity = moduleUniRepository.findByNumber(moduleUniEntity.getNumber()); //findByNumber or findByName? What would be rather changed by the university
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

    public UniMajorListResponse getAllMajors() {
        List<MajorUniEntity> majorEntities = majorUniRepository.findAll();
        return new UniMajorListResponse(majorEntities);
    }

    public UniModuleListResponse getModulesByMajor(String majorName) {
        MajorUniEntity major = majorUniRepository.findByName(majorName);
        if (major == null) {
            throw new ResourceNotFoundException("Major not found");
        }
        List<ModuleUniEntity> modules = moduleUniRepository.findByMajorUniEntity(major);
        List<UniModuleDto> moduleDtos = modules.stream().map(module -> new UniModuleDto(module.getNumber(), module.getName())).toList();
        return new UniModuleListResponse(major.getName(), moduleDtos);

    }
}
