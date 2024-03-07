package com.swtp4.backend.repositories.applicationDtos;

public record SubmittedStudentModule(
        String moduleNumber,
        String moduleName,
        Long credits,
        String university,
        String moduleMajor,
        String commentStudent,
        Long frontend_module_key
) {
}
