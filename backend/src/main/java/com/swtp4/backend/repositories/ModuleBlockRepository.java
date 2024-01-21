package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ModuleBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModuleBlockRepository extends JpaRepository<ModuleBlockEntity, Long> {

    ModuleBlockEntity findByCommentStudentAndApplicationEntity_ApplicationKeyClass_Creator(String commentStudent, String creator);
}
