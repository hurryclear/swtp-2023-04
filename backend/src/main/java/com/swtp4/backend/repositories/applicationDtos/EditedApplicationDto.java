package com.swtp4.backend.repositories.applicationDtos;

import java.util.List;

public record EditedApplicationDto (
       String applicationID,
       String dateLastEdited,
       String university,
       String formalRejection,
       List<EditedBlock> editedBlocks
){
}
