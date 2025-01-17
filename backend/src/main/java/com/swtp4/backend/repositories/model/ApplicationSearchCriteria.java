package com.swtp4.backend.repositories.model;

import lombok.Data;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

// contains criteria for filtering criteria
@Data
public class ApplicationSearchCriteria {

    private String applicationID;
    private List<String> statusList; // this is set by the service method and not by the frontend request
    private String dateOfSubmission;
    private String universityName;
    private String module;
    private String uniMajor;

    // format date to european date pattern
    public Date getDateOfSubmission() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        try {
            if (dateOfSubmission == null)
                return null;
            return dateFormat.parse(dateOfSubmission);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle parsing exception appropriately
            return null;
        }
    }


}
