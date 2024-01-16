package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ModulesRelationEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ModulesRelationKeyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModulesRelationRepository extends JpaRepository<ModulesRelationEntity, ModulesRelationKeyClass> {
}
