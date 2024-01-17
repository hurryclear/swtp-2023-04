package com.swtp4.backend.repositories.entities.keyClasses;

import com.swtp4.backend.repositories.entities.ModulesStudentEntity;
import com.swtp4.backend.repositories.entities.ModulesUniversityLeipzigEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModulesRelationKeyClass {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "modules_student_id")
    private ModulesStudentEntity modulesStudentEntity;

    @ManyToOne
    @JoinColumn(name = "modules_university_leipzig_id")
    private ModulesUniversityLeipzigEntity modulesUniversityLeipzigEntity;
}
