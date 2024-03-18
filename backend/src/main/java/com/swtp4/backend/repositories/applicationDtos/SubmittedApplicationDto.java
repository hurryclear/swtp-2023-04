package com.swtp4.backend.repositories.applicationDtos;

import java.util.Date;
import java.util.List;

// this is the Dto of the application the student submits
public record SubmittedApplicationDto(
        Date dateOfSubmission,
        Date dateLastEdited,
        String university,
        String oldCourseOfStudy,
        String newCourseOfStudy,
        List<SubmittedBlock> blocks) {

}


