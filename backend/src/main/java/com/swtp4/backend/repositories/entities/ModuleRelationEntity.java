package com.swtp4.backend.repositories.entities;

import com.swtp4.backend.repositories.entities.keyClasses.ModuleRelationKeyClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "module_relation")
public class ModuleRelationEntity {

    @EmbeddedId
    private ModuleRelationKeyClass moduleRelationKeyClass;

    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "module_block_id")
    private ModuleBlockEntity moduleBlockEntity;
}
