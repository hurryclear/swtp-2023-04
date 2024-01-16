package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ModulesUniversityLeipzigEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModulesUniversityLeipzigRepository extends CrudRepository<ModulesUniversityLeipzigEntity, String> {
}
