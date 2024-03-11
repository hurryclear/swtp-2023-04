package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.entities.ModuleBlockEntity;
import com.swtp4.backend.repositories.entities.ModuleRelationEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ModuleBlockRepository extends JpaRepository<ModuleBlockEntity, Long> {

    ModuleBlockEntity findByApplicationEntity_ApplicationKeyClass_Creator( String creator);

    List<ModuleBlockEntity> findAllByApplicationEntity(ApplicationEntity applicationEntity);
}
