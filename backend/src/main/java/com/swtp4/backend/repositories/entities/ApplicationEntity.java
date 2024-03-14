package com.swtp4.backend.repositories.entities;

import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
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
@Entity(name="application")
@Table(name="application")

public class ApplicationEntity {

    @EmbeddedId
    private ApplicationKeyClass applicationKeyClass;

    private String status;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfSubmission;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateLastEdited;

    private String universityName;

    private String studentMajor;
    private String uniMajor;

    private String formalRejectionReason;

}
