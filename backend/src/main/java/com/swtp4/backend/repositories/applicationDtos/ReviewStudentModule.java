package com.swtp4.backend.repositories.applicationDtos;

public record ReviewStudentModule(
        Long frontend_key,
        String approval,
        String reason,
        String number,
        String title,
        Long credits,
        String university,
        String major,
        String commentStudent
) {
}
