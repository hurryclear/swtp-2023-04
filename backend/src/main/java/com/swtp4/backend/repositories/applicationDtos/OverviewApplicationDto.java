package com.swtp4.backend.repositories.applicationDtos;

public record OverviewApplicationDto (
        String applicationID,
        String universityName,
        String dateOfSubmission,
        String dateLastEdited,
        String status
){ }

