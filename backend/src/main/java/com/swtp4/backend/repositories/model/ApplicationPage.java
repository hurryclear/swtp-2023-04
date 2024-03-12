package com.swtp4.backend.repositories.model;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class ApplicationPage{
    private int pageNumber = 0;
    private int pageSize = 5;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "applicationID"; // application id
}
