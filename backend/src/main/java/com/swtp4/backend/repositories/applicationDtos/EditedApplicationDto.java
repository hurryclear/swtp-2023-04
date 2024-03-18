package com.swtp4.backend.repositories.applicationDtos;

import java.util.Date;
import java.util.List;

// this Dto represents the edited part of the Application which gets saved
public record EditedApplicationDto (
       String applicationID,
       Date dateLastEdited,
       String university,
       String oldCourseOfStudy,
       String formalRejection,
       List<EditedBlock> editedBlocks
){
}
