package com.swtp4.backend.repositories.applicationDtos;

public record EntireOriginalAndEditedApplicationDto(
        EntireApplication original,
        EntireApplication edited
) {
}
