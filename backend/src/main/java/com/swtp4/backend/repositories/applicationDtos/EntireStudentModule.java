package com.swtp4.backend.repositories.applicationDtos;

public record EntireStudentModule(
        Long frontend_key,
        Long backend_module_id,
        String approval,
        String reason,
        String number,
        String title,
        String path,
        Long credits, // TODO: ist in der json noch ein String müsste aber eig long sein zum addieren
        String university,
        String major,
        String commentStudent,
        String commentEmployee
) {
}
