package com.swtp4.backend.repositories.model;

import lombok.Data;

// contains criteria for filtering criteria
@Data
public class ApplicationSearchCriteria {

    private String applicationID;
//    private String status;
    private String dateOfSubmission;
    private String universityName;
    private String module;
    private String uniMajor;
}
