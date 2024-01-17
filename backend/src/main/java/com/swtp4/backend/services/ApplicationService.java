package com.swtp4.backend.services;

import com.swtp4.backend.repositories.ApplicationsRepository;
import com.swtp4.backend.repositories.ModulesBlockRepository;
import com.swtp4.backend.repositories.ModulesStudentRepository;
import com.swtp4.backend.repositories.dto.ApplicationsDTO;
import com.swtp4.backend.repositories.entities.ApplicationsEntity;
import com.swtp4.backend.repositories.entities.ModulesBlockEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationService {

    private ApplicationsRepository applicationsRepository;
    private ModulesStudentRepository modulesStudentRepository;
    private ModulesBlockRepository modulesBlockRepository;

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

    }
}
