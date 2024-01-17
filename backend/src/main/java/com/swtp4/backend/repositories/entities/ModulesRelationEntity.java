package com.swtp4.backend.repositories.entities;

import com.swtp4.backend.repositories.entities.keyClasses.ModulesRelationKeyClass;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModulesRelationEntity {

    @EmbeddedId
    private ModulesRelationKeyClass modulesRelationKeyClass;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_blocks_id")
    private ModulesBlockEntity modulesBlockEntity;
}
