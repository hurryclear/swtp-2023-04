package com.swtp4.backend.repositories.entities.keyClasses;

import com.swtp4.backend.repositories.entities.ModulesStudentEntity;
import com.swtp4.backend.repositories.entities.ModulesUniversityLeipzigEntity;
import jakarta.persistence.JoinColumn;

public class ModulesRelationKeyClass {

    @JoinColumn(name = "modules_student_id")
    private ModulesStudentEntity modulesStudentEntity;

    @JoinColumn(name = "modules_university_leipzig_id")
    private ModulesUniversityLeipzigEntity modulesUniversityLeipzigEntity;
}
