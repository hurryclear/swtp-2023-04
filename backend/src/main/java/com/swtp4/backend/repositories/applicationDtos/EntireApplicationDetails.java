package com.swtp4.backend.repositories.applicationDtos;

public record EntireApplicationDetails(
        String applicationID,
        String status,
        String formalReject,
        String dateOfSubmission,
        String dateLastEdited,
        String university,
        String oldCourseOfStudy,
        String newCourseOfStudy
) {
}
