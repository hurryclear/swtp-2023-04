package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ModuleUniEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModuleUniRepository extends JpaRepository<ModuleUniEntity, String> {

    ModuleUniEntity findByNumber(String number);
}
