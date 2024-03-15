package com.swtp4.backend.repositories.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "module_block")
public class ModuleBlockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "application_id", referencedColumnName = "id"),
            @JoinColumn(name = "application_creator", referencedColumnName = "creator")
    })
    private ApplicationEntity applicationEntity;
    private Long frontendKey;

    @OneToMany(mappedBy = "moduleBlockEntity", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<ModuleRelationEntity> blockRelations;
}
