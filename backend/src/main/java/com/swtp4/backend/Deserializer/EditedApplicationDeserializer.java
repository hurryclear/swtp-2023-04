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
import java.util.ArrayList;
import java.util.List;

@JsonComponent
public class EditedApplicationDeserializer extends JsonDeserializer<EditedApplicationDto> {
    @Override
    public EditedApplicationDto deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
        JsonNode jsonNode = parser.getCodec().readTree(parser);
        JsonNode editedApplicationData = jsonNode.get("edited").get("applicationData");

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
                EditedBlock editedBlock = new EditedBlock(
                        block.get("frontend_key").asLong(),
                        block.get("backend_block_id").asLong(),
                        editedModules,
                        modulesCreditedIDs
                );
                editedBlocks.add(editedBlock);
            }
        }


        return new EditedApplicationDto(
                editedApplicationData.get("applicationID").asText(),
                editedApplicationData.get("dateLastEdited").asText(),
                editedApplicationData.get("university").asText(),
                editedApplicationData.get("formalReject").asText(),
                editedBlocks
        );
    }
}