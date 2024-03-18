package com.swtp4.backend.repositories.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

// used for getting ID AND paths form service method
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApplicationIDWithFilePaths {
    private String applicationID;
    private HashMap<String, String> filesAndPaths;
}

