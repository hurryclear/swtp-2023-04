package com.swtp4.backend.repositories.applicationDtos;

import java.util.List;

public record SubmittedApplicationDto(
        String dateOfSubmission,
        String dateLastEdited,
        List<SubmittedBlock> blocks) {

}


