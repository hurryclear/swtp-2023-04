package com.swtp4.backend.repositories.entities;

import com.swtp4.backend.repositories.entities.keyClasses.ApplicationsKeyClass;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name="applications")
@Table(name="applications")

public class ApplicationsEntity {

    @EmbeddedId
    private ApplicationsKeyClass applicationsKeyClass;

    private String status;

    private Date dateOfSubmission;

    private Date dateLastEdited;

    private String universityName;

    private String major;

    private String commentStudent;

    private String commentEmployee;
}
