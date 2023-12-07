package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.UniversityModulesEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UniversityModulesRepository extends CrudRepository<UniversityModulesEntity, Long> {
}
