package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.ApplicationEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ApplicationRepository extends CrudRepository<ApplicationEntity, UUID>{

}