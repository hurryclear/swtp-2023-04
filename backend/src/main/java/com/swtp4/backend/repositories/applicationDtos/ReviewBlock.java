package com.swtp4.backend.repositories.applicationDtos;

import java.util.List;

public record ReviewBlock(
        Long frontend_key,
        List<ReviewStudentModule> modulesStudent,
        List<ReviewUniModule> modules2bCredited
) {
}
