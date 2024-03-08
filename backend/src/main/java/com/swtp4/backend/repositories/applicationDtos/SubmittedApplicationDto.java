package com.swtp4.backend.repositories.applicationDtos;

import java.util.List;

public record SubmittedApplicationDto(
        String dateOfSubmission,
        String dateLastEdited,
        String university,
        String oldCourseOfStudy,
        String newCourseOfStudy,
        List<SubmittedBlock> blocks) {

}


