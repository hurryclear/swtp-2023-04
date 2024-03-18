package com.swtp4.backend.repositories.applicationDtos;

import java.util.List;

// this represents the Application the frontend receives for the review of a student
public record ReviewApplicationDto(
        ReviewApplicationDetails applicationData,
        List<ReviewBlock> moduleFormsData
) {
}
