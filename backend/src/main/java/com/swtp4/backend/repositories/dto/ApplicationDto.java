package com.swtp4.backend.repositories.dto;

import com.swtp4.backend.repositories.entities.ApplicationEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ApplicationDto {

    //applicationData from JSON mapped to applicationEntity
    private ApplicationEntity applicationData;

    //moduleFormsData List of ModuleBlocks from JSON Mapped to another Dto
    private List<ModuleBlockDto> moduleFormsData;
}
