package com.swtp4.backend.repositories.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniMajorWithVisibility {
    private String name;
    private Boolean visibleForStudents;
}