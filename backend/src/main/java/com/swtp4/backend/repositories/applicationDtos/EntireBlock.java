package com.swtp4.backend.repositories.applicationDtos;

import java.util.List;

public record EntireBlock(
        Long frontend_key,
        Long backend_block_id,
        List<EntireStudentModule> modulesStudent,
        List<Long> modules2bCredited
) {
}
