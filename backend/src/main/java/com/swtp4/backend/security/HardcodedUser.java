package com.swtp4.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class HardcodedUser implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public HardcodedUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create a user and save to the database
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword(new BCryptPasswordEncoder().encode("testpw"));
        user.setRole(Role.OFFICE);
        userRepository.save(user);
    }
}

