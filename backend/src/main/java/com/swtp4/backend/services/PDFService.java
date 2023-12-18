package com.swtp4.backend.services;

import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.UniversityModulesRepository;
import org.springframework.stereotype.Service;

@Service
public class PDFService {

    private UniversityModulesRepository universityModulesRepository;
    private ApplicationRepository applicationRepository;
}
