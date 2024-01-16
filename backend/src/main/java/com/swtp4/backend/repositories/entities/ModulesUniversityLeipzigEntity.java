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
@Table(name = "modules_university_leipzig")
public class ModulesUniversityLeipzigEntity {

    @Id
    private String number;

    private String name;

    @ManyToOne
    @JoinColumn(name ="majors_university_leipzig_name")
    private MajorsUniversityLeipzigEntity majorsUniversityLeipzigEntity;
}
