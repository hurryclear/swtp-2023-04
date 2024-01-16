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
@Table(name = "module_blocks")
public class ModulesBlockEntity {

    @Id
    private Long id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "applications_id"),
            @JoinColumn(name = "applications_creator")
    })
    private ApplicationsEntity applicationsEntity;

    private String approval;
}
