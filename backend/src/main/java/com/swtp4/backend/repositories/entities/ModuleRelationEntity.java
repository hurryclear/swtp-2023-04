package com.swtp4.backend.repositories.entities;

import com.swtp4.backend.repositories.entities.keyClasses.ModuleRelationKeyClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// this Entity is the connection between the student and university modules and represents the n:m Relation
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "module_relation")
public class ModuleRelationEntity {

    @EmbeddedId
    private ModuleRelationKeyClass moduleRelationKeyClass;

    @ManyToOne
    @JoinColumn(name = "module_block_id")
    private ModuleBlockEntity moduleBlockEntity;
}
