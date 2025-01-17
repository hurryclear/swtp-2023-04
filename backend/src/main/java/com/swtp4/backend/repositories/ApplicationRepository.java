package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<ApplicationEntity, ApplicationKeyClass> {
    Optional<ApplicationEntity> findBy();

    ApplicationEntity findByApplicationKeyClass_IdAndApplicationKeyClass_Creator(String id, String creator);
    List<ApplicationEntity> findByApplicationKeyClass_Creator(String creator);
    List<ApplicationEntity> findByStatusAndApplicationKeyClass_Creator(String status, String creator);
    List<ApplicationEntity> findByUniMajorAndApplicationKeyClass_Creator(String major, String creator);

    List<ApplicationEntity> findByUniversityNameAndApplicationKeyClass_Creator(String universityName, String creator);
    // dateOfSubmission includes also time, but when we search we actually want to search only with date, that's enough, so how can I transfer
    ApplicationEntity findByDateOfSubmissionAndApplicationKeyClass_Creator(Date dateOfSubmission, String creator);

    // how can I find only by certain date without timing

    List<ApplicationEntity> findByDateOfSubmissionBeforeAndApplicationKeyClass_Creator(Date dateOfSubmission, String creator);
    List<ApplicationEntity> findByDateOfSubmissionAfterAndApplicationKeyClass_Creator(Date dateOfSubmission, String creator);


}