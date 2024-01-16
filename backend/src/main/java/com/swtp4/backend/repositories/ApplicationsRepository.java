package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ApplicationsEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationsKeyClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApplicationsRepository extends JpaRepository<ApplicationsEntity, ApplicationsKeyClass> {

}