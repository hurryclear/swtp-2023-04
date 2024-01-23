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
@Table(name = "module_uni")
public class ModuleUniEntity {

    @Id
    private String number;
    private String name;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name ="major_uni_name")
    private MajorUniEntity majorUniEntity;
}
