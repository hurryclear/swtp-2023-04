package com.swtp4.backend.repositories.entities.keyClasses;

import com.swtp4.backend.repositories.entities.ModuleStudentEntity;
import com.swtp4.backend.repositories.entities.ModuleUniEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleRelationKeyClass implements Serializable {

    @ManyToOne
    @JoinColumn(name = "module_student_id")
    private ModuleStudentEntity moduleStudentEntity;

    @ManyToOne
    @JoinColumn(name = "module_uni_id")
    private ModuleUniEntity moduleUniEntity;
}
