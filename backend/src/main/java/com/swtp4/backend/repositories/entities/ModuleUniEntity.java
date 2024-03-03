package com.swtp4.backend.repositories.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "module_uni")
public class ModuleUniEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String number;
    private String name;
    private Boolean visibleChoice;
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name ="major_uni_name")
    private MajorUniEntity majorUniEntity;
}
