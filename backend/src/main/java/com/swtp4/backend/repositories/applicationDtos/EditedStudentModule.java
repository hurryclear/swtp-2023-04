package com.swtp4.backend.repositories.applicationDtos;

public record EditedStudentModule(
        Long moduleID,
        Long frontendKey,
        String approval,
        String reason,
        String number,
        String title,
        Long credits,
        String university,
        String moduleMajor,
        String commentStudent,
        String commentEmployee
){
}