package com.swtp4.backend.services;

import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.MajorUniRepository;
import com.swtp4.backend.repositories.ModuleUniRepository;
import com.swtp4.backend.repositories.dto.*;
import com.swtp4.backend.repositories.entities.MajorUniEntity;
import com.swtp4.backend.repositories.entities.ModuleUniEntity;
import com.swtp4.backend.repositories.projections.MajorNameAndVisibilityProjection;
import com.swtp4.backend.repositories.projections.MajorNameProjection;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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

    public UniMajorListResponse getVisibleMajorNames() {
        // Retrieve all visible majors using the projection
        List<MajorNameProjection> visibleMajors = majorUniRepository.findByVisibleChoiceTrue();

        // Extract the names of the majors
        List<UniMajorName> visibleMajorNames = visibleMajors.stream()
                .map(majorName -> new UniMajorName(majorName.getName()))
                .collect(Collectors.toList());

        return new UniMajorListResponse(visibleMajorNames);
    }


    public UniMajorsWithVisibilityResponse getAllMajors() {
        // Retrieve ALL majors using the projection
        List<MajorNameAndVisibilityProjection> allMajorsProjections = majorUniRepository.findAllProjectedBy();

        // Extract the names of the majors
        List<UniMajorWithVisibility> allMajors = allMajorsProjections.stream()
                .map(majorProjection -> new UniMajorWithVisibility(majorProjection.getName(), majorProjection.getVisibleChoice()))
                .collect(Collectors.toList());
        return new UniMajorsWithVisibilityResponse(allMajors);
    }

    @Transactional(readOnly = true)
    public UniModuleListResponse getVisibleMajorWithModules(String majorName) {
        MajorUniEntity major = majorUniRepository.findByNameAndVisibleChoiceTrue(majorName)
                .orElseThrow(() -> new ResourceNotFoundException("Major not found"));

        // get only modules students should see as choice
        List<UniModuleDto> visibleModules = major.getModules().stream()
                .filter(ModuleUniEntity::getVisibleChoice)
                .map(module -> UniModuleDto.builder()
                        .number(module.getNumber())
                        .name(module.getName())
                        .id(module.getId())
                        .build())
                .collect(Collectors.toList());

        return UniModuleListResponse.builder()
                .name(major.getName())
                .modules(visibleModules)
                .build();
    }

    @Transactional(readOnly = true)
    public UniModulesWithVisibilityResponse getAllModulesByMajor(String majorName) {
        MajorUniEntity major = majorUniRepository.findByName(majorName)
                .orElseThrow(() -> new ResourceNotFoundException("Major not found"));

        // get visible and invisible modules for employees
        List<UniModuleWithVisibility> allModules = major.getModules().stream()
                .map(module -> UniModuleWithVisibility.builder()
                        .number(module.getNumber())
                        .name(module.getName())
                        .id(module.getId())
                        .visibleForStudents(module.getVisibleChoice())
                        .build())
                .collect(Collectors.toList());

        return UniModulesWithVisibilityResponse.builder()
                .name(major.getName())
                .visibleForStudents(major.getVisibleChoice())
                .modules(allModules)
                .build();
    }

    @Transactional
    public void updateVisibilityBasedOnJson(UniDataDto uniDataDto) {
        // Set all existing majors and modules to not visible
        majorUniRepository.findAll().forEach(major -> {
            major.setVisibleChoice(false);
            majorUniRepository.save(major);
        });

        moduleUniRepository.findAll().forEach(module -> {
            module.setVisibleChoice(false);
            moduleUniRepository.save(module);
        });

        // Iterate over the majors and modules in the JSON, find them or create new ones
        for (UniMajorDto majorDto : uniDataDto.getCourses()) {
            MajorUniEntity major = majorUniRepository.findById(majorDto.getName())
                    .orElse(MajorUniEntity.builder()
                            .name(majorDto.getName())
                            .visibleChoice(true)
                            .modules(new ArrayList<>())
                            .build());

            major.setVisibleChoice(true);
            majorUniRepository.save(major);

            for (UniModuleDto moduleDto : majorDto.getModules()) {
                ModuleUniEntity module = moduleUniRepository.findByNumberAndNameAndMajorUniEntity(moduleDto.getNumber(), moduleDto.getName(), major)
                        .orElse(ModuleUniEntity.builder()
                                .number(moduleDto.getNumber())
                                .name(moduleDto.getName())
                                .visibleChoice(true)
                                .majorUniEntity(major)
                                .build());

                module.setVisibleChoice(true);
                module.setMajorUniEntity(major);
                moduleUniRepository.save(module);
            }
        }
    }

}
