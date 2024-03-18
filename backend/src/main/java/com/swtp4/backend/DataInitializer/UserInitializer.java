package com.swtp4.backend.DataInitializer;

import com.swtp4.backend.security.Role;
import com.swtp4.backend.security.UserEntity;
import com.swtp4.backend.security.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class UserInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${office.username}")
    private String officeUsername;
    @Value("${office.password}")
    private String officePassword;
    @Value("${committee.username}")
    private String committeeUsername;
    @Value("${committee.password}")
    private String committeePassword;

    @Autowired
    public UserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // this initializes the accounts of Office and Committee
    @Override
    public void run(String... args) throws Exception {
        // Create a user and save in the database
        UserEntity office = new UserEntity();
        office.setUsername(officeUsername);
        office.setPassword(passwordEncoder.encode(officePassword));
        office.setRole(Role.OFFICE);
        userRepository.save(office);

        UserEntity committee = new UserEntity();
        committee.setUsername(committeeUsername);
        committee.setPassword(passwordEncoder.encode(committeePassword));
        committee.setRole(Role.COMMITTEE);
        userRepository.save(committee);
    }
}

