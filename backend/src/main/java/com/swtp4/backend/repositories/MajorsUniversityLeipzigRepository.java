package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.MajorsUniversityLeipzigEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorsUniversityLeipzigRepository extends CrudRepository<MajorsUniversityLeipzigEntity, Long> {
}
