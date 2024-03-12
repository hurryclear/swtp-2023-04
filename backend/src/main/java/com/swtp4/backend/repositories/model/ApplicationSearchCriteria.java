package com.swtp4.backend.repositories.model;

import lombok.Data;

@Data
public class ApplicationSearchCriteria {

    private String applicationID;
    private String status;
    private String dateOfSubmission;
    private String dateLastEdited;
    private String university;
}
