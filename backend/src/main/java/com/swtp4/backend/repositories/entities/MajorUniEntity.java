package com.swtp4.backend.repositories.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "major_uni")
public class MajorUniEntity {

//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;
    @Id
    private String name;
    private Boolean visibleChoice;
    @OneToMany(mappedBy = "majorUniEntity", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<ModuleUniEntity> modules;
}
