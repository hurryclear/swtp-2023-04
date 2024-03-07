package com.swtp4.backend.repositories.applicationDtos;

import java.util.List;

public record SubmittedBlock(
        Long frontend_block_key,
        List<SubmittedStudentModule> studentModules,
        List<Long> uniModulesIDs
) {
}
