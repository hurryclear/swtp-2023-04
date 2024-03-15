package com.swtp4.backend.repositories.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

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

    @OneToMany(mappedBy = "applicationEntity", fetch = FetchType.LAZY)
    @JsonIgnore
    @ToString.Exclude
    private List<ModuleBlockEntity> moduleBlocks;
}
