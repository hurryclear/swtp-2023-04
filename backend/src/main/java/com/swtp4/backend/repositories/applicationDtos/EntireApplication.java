package com.swtp4.backend.repositories.applicationDtos;

import java.util.List;

public record EntireApplication(
    EntireApplicationDetails applicationData,
    List<EntireBlock> moduleFormsData
){
}
