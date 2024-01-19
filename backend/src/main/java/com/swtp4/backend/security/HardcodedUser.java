package com.swtp4.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class HardcodedUser implements CommandLineRunner {

    private final UserRepository userRepository;

    @Autowired
    public HardcodedUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Create a user and save to the database
        UserEntity office = new UserEntity();
        office.setUsername("office");
        office.setPassword(new BCryptPasswordEncoder().encode("pw_office"));
        office.setRole(Role.OFFICE);
        userRepository.save(office);

        UserEntity committee = new UserEntity();
        committee.setUsername("committee");
        committee.setPassword(new BCryptPasswordEncoder().encode("pw_committee"));
        committee.setRole(Role.COMMITTEE);
        userRepository.save(committee);
    }
}

