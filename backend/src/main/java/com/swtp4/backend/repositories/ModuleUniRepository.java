package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.MajorUniEntity;
import com.swtp4.backend.repositories.entities.ModuleUniEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleUniRepository extends JpaRepository<ModuleUniEntity, String> {

    ModuleUniEntity findByNumber(String number);
    ModuleUniEntity findByName(String name);

    List<ModuleUniEntity> findByMajorUniEntity(MajorUniEntity majorUniEntity);

    ModuleUniEntity findByNameAndMajorUniEntity(String name, MajorUniEntity majorUniEntity);
}
