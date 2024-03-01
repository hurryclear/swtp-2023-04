package com.swtp4.backend.repositories.dto;

import com.swtp4.backend.repositories.entities.ModuleBlockEntity;
import com.swtp4.backend.repositories.entities.ModuleStudentEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleBlockDto {

    //moduleBlock from moduleFormsData from JSON mapped to ModulesBlockEntity
    private ModuleBlockEntity moduleBlockData;

    //modulesStudent from JSON in moduleFormsData mapped to List of ModulesStudentEntities
    private List<ModuleStudentEntity> modulesStudent;

    //modules2bCredited from JSON
    private List<String> modules2bCredited;
}
