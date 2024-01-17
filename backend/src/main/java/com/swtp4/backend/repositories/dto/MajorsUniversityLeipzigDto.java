package com.swtp4.backend.repositories.dto;

import com.swtp4.backend.repositories.entities.ModulesUniversityLeipzigEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MajorsUniversityLeipzigDto {

    private String name;

    private List<ModulesUniversityLeipzigEntity> modules;
}
