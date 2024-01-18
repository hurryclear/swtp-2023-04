package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ModuleStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleStudentRepository extends JpaRepository<ModuleStudentEntity, Long> {
}
