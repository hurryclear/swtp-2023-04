package com.swtp4.backend.repositories.dto;

import com.swtp4.backend.repositories.entities.ApplicationsEntity;
import com.swtp4.backend.repositories.entities.ModulesBlockEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class ApplicationsDTO {

    //applicationData from JSON mapped to applicationEntity
    private ApplicationsEntity applicationData;

    //moduleFormsData List of ModuleBlocks from JSON Mapped to another Dto
    private List<ModulesBlockDto> moduleFormsData;
}
