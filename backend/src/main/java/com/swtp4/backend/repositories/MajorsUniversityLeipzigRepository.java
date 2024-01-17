package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.MajorsUniversityLeipzigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorsUniversityLeipzigRepository extends JpaRepository<MajorsUniversityLeipzigEntity, Long> {

    MajorsUniversityLeipzigEntity findByName(String name);
}
