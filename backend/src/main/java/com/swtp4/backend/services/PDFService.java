package com.swtp4.backend.services;

import com.swtp4.backend.repositories.ApplicationsRepository;
import com.swtp4.backend.repositories.ModulesStudentRepository;
import org.springframework.stereotype.Service;

@Service
public class PDFService {

    private ModulesStudentRepository modulesStudentRepository;
    private ApplicationsRepository applicationsRepository;
}
