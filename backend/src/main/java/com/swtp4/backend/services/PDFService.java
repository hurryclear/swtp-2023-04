package com.swtp4.backend.services;

import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.ModuleStudentRepository;
import org.springframework.stereotype.Service;

@Service
public class PDFService {

    private ModuleStudentRepository moduleStudentRepository;
    private ApplicationRepository applicationRepository;
}
