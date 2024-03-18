package com.swtp4.backend.repositories.applicationDtos;

// this dto is used in the Overview tables of office and committee page
public record OverviewApplicationDto (
        String applicationID,
        String universityName,
        String dateOfSubmission,
        String dateLastEdited,
        String status
){ }

