package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.entities.LoginCredentialsEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginCredentialsRepository extends CrudRepository<LoginCredentialsEntity, String> {
}
