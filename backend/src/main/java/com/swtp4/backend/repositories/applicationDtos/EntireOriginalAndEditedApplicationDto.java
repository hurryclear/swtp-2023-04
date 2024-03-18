package com.swtp4.backend.repositories.applicationDtos;

// this represents the original and edited application frontend will receive
public record EntireOriginalAndEditedApplicationDto(
        EntireApplication original,
        EntireApplication edited
) {
}
