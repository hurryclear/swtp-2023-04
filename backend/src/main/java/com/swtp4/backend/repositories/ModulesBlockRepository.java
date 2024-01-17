package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ModulesBlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface ModulesBlockRepository extends JpaRepository<ModulesBlockEntity, Long> {
}
