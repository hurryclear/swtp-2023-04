package com.swtp4.backend.repositories.dto;

import com.swtp4.backend.repositories.entities.ModulesBlockEntity;
import com.swtp4.backend.repositories.entities.ModulesStudentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModulesBlockDto {

    //moduleBlock from moduleFormsData from JSON mapped to ModulesBlockEntity
    private ModulesBlockEntity modulesBlockEntity;

    //modulesStudent from JSON in moduleFormsData mapped to List of ModulesStudentEntities
    private List<ModulesStudentEntity> modulesStudent;

    //modules2bCredited from JSON
    private List<String> modules2bCredited;
}
