package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ModuleBlockEntity;
import com.swtp4.backend.repositories.entities.ModuleRelationEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ModuleRelationKeyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleRelationRepository extends JpaRepository<ModuleRelationEntity, ModuleRelationKeyClass> {

    ModuleRelationEntity findByModuleRelationKeyClass_ModuleStudentEntity_NumberAndModuleRelationKeyClass_ModuleUniEntity_NameAndModuleBlockEntity_ApplicationEntity_ApplicationKeyClass_Creator(String moduleStudentNumber, String moduleUniName, String creator);
}