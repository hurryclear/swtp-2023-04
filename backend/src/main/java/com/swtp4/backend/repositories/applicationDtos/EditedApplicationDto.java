package com.swtp4.backend.repositories.applicationDtos;

import java.util.Date;
import java.util.List;

public record EditedApplicationDto (
       String applicationID,
       Date dateLastEdited,
       String university,
       String oldCourseOfStudy,
       String formalRejection,
       List<EditedBlock> editedBlocks
){
}
