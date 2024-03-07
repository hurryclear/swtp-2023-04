package com.swtp4.backend.repositories.applicationDtos;

import java.util.List;

public record EditedBlock (
        Long frontendKey,
        Long blockID,
        List<EditedStudentModule> editedModules,
        List<Long> uniModuleIDs
){
}
