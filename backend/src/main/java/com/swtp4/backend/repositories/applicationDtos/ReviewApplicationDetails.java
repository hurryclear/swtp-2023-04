package com.swtp4.backend.repositories.applicationDtos;

public record ReviewApplicationDetails(
    String applicationID,
    String status,
    String formalRejection,
    String dateOfSubmission,
    String dateLastEdited,
    String university,
    String oldCourseOfStudy,
    String newCourseOfStudy
){
}
