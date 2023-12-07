package com.swtp4.backend.repositories.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.sql.Blob;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "university_modules")
public class UniversityModulesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String number;

    @ManyToOne
    @JoinColumn(name = "applications_id")
    private ApplicationEntity applicationEntity;

    private String title;

    @Lob
    private Blob description_pdf;

    private int credits;

    private String comment_student;

    private String comment_employee;
}
