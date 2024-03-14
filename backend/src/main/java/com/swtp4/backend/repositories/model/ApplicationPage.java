package com.swtp4.backend.repositories.model;

import lombok.Data;
import org.springframework.data.domain.Sort;

// contains information about the current page with page size
@Data
public class ApplicationPage{
    private int pageNumber = 0; // default:0
    private int pageSize = 5; // default: 5
    private Sort.Direction sortDirection = Sort.Direction.ASC; // default: asc
    private String sortBy = "dateOfSubmission"; // default: dateOfSubmission
}
