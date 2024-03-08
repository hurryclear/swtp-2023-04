package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ModuleStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModuleStudentRepository extends JpaRepository<ModuleStudentEntity, Long> {

    ModuleStudentEntity findByNumberAndCreator(String number, String creator);

    Optional<ModuleStudentEntity> findByIdAndCreator(Long id, String creator);
}
