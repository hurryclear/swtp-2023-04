package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ModulesStudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModulesStudentRepository extends JpaRepository<ModulesStudentEntity, Long> {
}
