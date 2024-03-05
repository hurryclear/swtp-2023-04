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

//    ApplicationEntity findByIdAndCreator(UUID id,String creator);
    List<ApplicationEntity> findByStatus(String status);
    List<ApplicationEntity> findByMajor(String major);
    List<ApplicationEntity> findByUniversityName(String universityName);
    List<ApplicationEntity> findByDateOfSubmission(String dateOfSubmission);
    List<ApplicationEntity> findByDateOfSubmissionBefore(String dateOfSubmission);
    List<ApplicationEntity> findByDateOfSubmissionAfter(String dateOfSubmission);

}