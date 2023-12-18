package com.swtp4.backend.services;

import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.LoginCredentialsRepository;
import org.springframework.stereotype.Repository;

@Repository
public class AuthenticationService {

    private LoginCredentialsRepository loginCredentialsRepository;
    private ApplicationRepository applicationRepository;
}
