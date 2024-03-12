package com.swtp4.backend.repositories.applicationDtos;

public record OverviewApplicationDto (
        String applicationID,
        String status,
        String dateOfSubmission,
        String dateLastEdited,
        String university,
        EntireApplication original,
        EntireApplication edited
){ }
