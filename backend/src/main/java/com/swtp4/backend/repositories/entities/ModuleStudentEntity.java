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
@Table(name = "module_student")
public class ModuleStudentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long frontendKey;
    private String approval;
    private String approvalReason;
    private String number;
    private String title;
    private Long credits;
    private String university;
    private String major;
    private String commentStudent;
    private String commentEmployee;
    private String description_pdf;
    private String creator;
}
