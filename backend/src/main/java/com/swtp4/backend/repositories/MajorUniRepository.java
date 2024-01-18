package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.MajorUniEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorUniRepository extends JpaRepository<MajorUniEntity, Long> {

    MajorUniEntity findByName(String name);
}
