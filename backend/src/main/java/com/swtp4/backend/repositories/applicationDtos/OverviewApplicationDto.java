package com.swtp4.backend.repositories.applicationDtos;

public record OverviewApplicationDto (
        String applicationID,
        String university,
        String dateOfSubmission,
        String dateLastEdited,
        String status
){ }
