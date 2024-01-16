package com.swtp4.backend.repositories.entities;

import com.swtp4.backend.repositories.entities.keyClasses.ModulesRelationKeyClass;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

public class ModulesRelationEntity {

    @EmbeddedId
    private ModulesRelationKeyClass modulesRelationKeyClass;

    @ManyToOne
    @JoinColumn(name = "module_blocks_id")
    private ModulesBlockEntity modulesBlockEntity;
}
