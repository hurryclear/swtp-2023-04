package com.swtp4.backend.repositories.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniModuleListResponse {
    private String name;
    private List<UniModuleDto> modules;
}

