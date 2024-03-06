package com.swtp4.backend.repositories.applicationDtos;

public record SubmittedStudentModule(
        String moduleNumber,
        String moduleName,
        Integer credits,
        String university,
        String moduleMajor,
        String commentStudent,
        Integer frontend_module_key
) {
}
