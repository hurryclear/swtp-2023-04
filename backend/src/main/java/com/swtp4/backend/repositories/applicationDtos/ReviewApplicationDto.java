package com.swtp4.backend.repositories.applicationDtos;

import java.util.List;

public record ReviewApplicationDto(
        ReviewApplicationDetails applicationData,
        List<ReviewBlock> moduleFormsData
) {
}
