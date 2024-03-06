package com.swtp4.backend.repositories.entities;

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
@Table(name = "module_block")
public class ModuleBlockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "application_id"),
            @JoinColumn(name = "application_creator")
    })
    private ApplicationEntity applicationEntity;
    private Integer frontendKey;

    private String approval;

    private String commentStudent;

    private String commentEmployee;

}
