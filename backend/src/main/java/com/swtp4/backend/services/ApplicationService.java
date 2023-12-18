package com.swtp4.backend.services;

import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.UniversityModulesRepository;
import com.swtp4.backend.repositories.entities.ApplicationEntity;
import org.springframework.stereotype.Service;

@Service
public class ApplicationService {

    private ApplicationRepository applicationRepository;
    private UniversityModulesRepository universityModulesRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public void save(ApplicationEntity applicationEntity) {
        applicationRepository.save(applicationEntity);
    }
}
