package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, ApplicationKeyClass> {
    ApplicationEntity findByDateOfSubmissionAndApplicationKeyClass_Creator(String dateOfSubmission, String creator);

    ApplicationEntity findByApplicationKeyClass(UUID id, String creator);
    List<ApplicationEntity> findAllByStatus(String status);
    List<ApplicationEntity> findAllByMajor(String major);
    List<ApplicationEntity> findAllByUniversity(String university);
    List<ApplicationEntity> findAllByDateOfSubmission(String dateOfSubmission);
    List<ApplicationEntity> findAllByDateOfSubmissionBefore(String dateOfSubmission);
    List<ApplicationEntity> findAllByDateOfSubmissionAfter(String dateOfSubmission);

}