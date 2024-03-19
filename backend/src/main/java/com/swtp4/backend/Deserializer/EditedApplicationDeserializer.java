package com.swtp4.backend.Deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.swtp4.backend.repositories.applicationDtos.EditedApplicationDto;
import com.swtp4.backend.repositories.applicationDtos.EditedBlock;
import com.swtp4.backend.repositories.applicationDtos.EditedStudentModule;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonComponent
public class EditedApplicationDeserializer extends JsonDeserializer<EditedApplicationDto> {
    @Override
    public EditedApplicationDto deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
        JsonNode jsonNode = parser.getCodec().readTree(parser);
        JsonNode editedApplicationData = jsonNode.get("edited").get("applicationData");

        //first get all Blocks with Data
        JsonNode moduleFormsData = jsonNode.get("edited").get("moduleFormsData");
        List<EditedBlock> editedBlocks = new ArrayList<>();
        if (moduleFormsData.isArray()) {
            for(JsonNode block: moduleFormsData) {
                JsonNode modulesToBeCredited = block.get("modules2bCredited");
                List<Long> modulesCreditedIDs = new ArrayList<>();
                if(modulesToBeCredited.isArray()) {
                    for(JsonNode module : modulesToBeCredited) {
                        modulesCreditedIDs.add((module.asLong()));
                    }
                }
                // get all Modules in each Block
                JsonNode modulesStudent = block.get("modulesStudent");
                List<EditedStudentModule> editedModules = new ArrayList<>();
                if(modulesStudent.isArray()) {
                    for(JsonNode moduleNode : modulesStudent){
                        EditedStudentModule editedModule = new EditedStudentModule(
                                moduleNode.get("backend_module_id").asLong(),
                                moduleNode.get("frontend_key").asLong(),
                                moduleNode.get("approval").asText(),
                                moduleNode.get("reason").asText(),
                                moduleNode.get("number").asText(),
                                moduleNode.get("title").asText(),
                                moduleNode.get("credits").asLong(),
                                moduleNode.get("university").asText(),
                                moduleNode.get("major").asText(),
                                moduleNode.get("commentStudent").asText(),
                                moduleNode.get("commentEmployee").asText()
                        );
                        editedModules.add(editedModule);
                    }
                }
                // get Modules of Uni Leipzig
                EditedBlock editedBlock = new EditedBlock(
                        block.get("frontend_key").asLong(),
                        block.get("backend_block_id").asLong(),
                        editedModules,
                        modulesCreditedIDs
                );
                editedBlocks.add(editedBlock);
            }
        }

        // create ApplicationDto
        return new EditedApplicationDto(
                editedApplicationData.get("applicationID").asText(),
                parseDate(editedApplicationData.get("dateLastEdited").asText()),
                editedApplicationData.get("university").asText(),
                editedApplicationData.get("oldCourseOfStudy").asText(),
                editedApplicationData.get("formalReject").asText(),
                editedBlocks
        );
    }

    // parse String to timestamp, important for sorting
    private Date parseDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace(); // Handle parsing exception appropriately
            return null;
        }
    }
}