package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.MajorUniEntity;
import com.swtp4.backend.repositories.projections.MajorNameAndVisibilityProjection;
import com.swtp4.backend.repositories.projections.MajorNameProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MajorUniRepository extends JpaRepository<MajorUniEntity, String> {

    Optional<MajorUniEntity> findByName(String name);
    List<MajorNameProjection> findByVisibleChoiceTrue();
    List<MajorNameAndVisibilityProjection> findAllProjectedBy();

    Optional<MajorUniEntity> findByNameAndVisibleChoiceTrue(String majorName);
}
