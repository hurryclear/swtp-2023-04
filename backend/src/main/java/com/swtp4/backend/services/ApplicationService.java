package com.swtp4.backend.services;

import com.swtp4.backend.repositories.ApplicationsRepository;
import com.swtp4.backend.repositories.ModulesStudentRepository;
import com.swtp4.backend.repositories.entities.ApplicationsEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationsKeyClass;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private ApplicationsRepository applicationsRepository;
    private ModulesStudentRepository modulesStudentRepository;

    public ApplicationService(ApplicationsRepository applicationsRepository) {
        this.applicationsRepository = applicationsRepository;
    }

    public void save(ApplicationsEntity applicationsEntity) {

    }
}
