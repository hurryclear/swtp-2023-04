package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, ApplicationKeyClass> {
    ApplicationEntity findByDateOfSubmissionAndApplicationKeyClass_Creator(String dateOfSubmission, String creator);
    Optional<ApplicationEntity> findBy();
}