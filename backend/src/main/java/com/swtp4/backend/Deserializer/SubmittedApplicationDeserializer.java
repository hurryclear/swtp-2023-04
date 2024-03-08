package com.swtp4.backend.Deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.swtp4.backend.repositories.applicationDtos.SubmittedApplicationDto;
import com.swtp4.backend.repositories.applicationDtos.SubmittedBlock;
import com.swtp4.backend.repositories.applicationDtos.SubmittedStudentModule;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@JsonComponent
public class SubmittedApplicationDeserializer extends JsonDeserializer<SubmittedApplicationDto> {
    @Override
    public SubmittedApplicationDto deserialize(JsonParser parser, DeserializationContext context) throws IOException, JacksonException {
        JsonNode jsonNode = parser.getCodec().readTree(parser);
        JsonNode moduleMappings = jsonNode.get("moduleMappings");
        List<SubmittedBlock> submittedBlocks = new ArrayList<>();
        if(moduleMappings.isArray()) {
            for(JsonNode block : moduleMappings){
                JsonNode modulesToBeCredited = block.get("modulesToBeCredited");
                List<Long> modulesCreditedIDs = new ArrayList<>();
                if(modulesToBeCredited.isArray()) {
                    for(JsonNode module : modulesToBeCredited) {
                        modulesCreditedIDs.add(module.get("id").asLong());
                    }
                }
                JsonNode previousModules = block.get("previousModules");
                List<SubmittedStudentModule> submittedStudentModules = new ArrayList<>();
                if (previousModules.isArray()){
                    for(JsonNode moduleNode : previousModules) {
                        SubmittedStudentModule submittedModule = new SubmittedStudentModule(
                                moduleNode.get("number").asText(),
                                moduleNode.get("name").asText(),
                                moduleNode.get("credits").asLong(),
                                moduleNode.get("university").get("name").asText(),
                                moduleNode.get("major").asText(),
                                moduleNode.get("meta").get("comments").get("student").asText(),
                                moduleNode.get("key").asLong()
                        );
                        submittedStudentModules.add(submittedModule);
                    }
                }
                SubmittedBlock submittedBlock = new SubmittedBlock(
                        block.get("meta").get("key").asLong(),
                        submittedStudentModules,
                        modulesCreditedIDs
                );
                submittedBlocks.add(submittedBlock);
            }
        }

        return new SubmittedApplicationDto(
                jsonNode.get("meta").get("dateOfSubmission").asText(), // TODO: change to extractDate to create date and not string to sort by date in database
                jsonNode.get("meta").get("dateLastEdited").asText(),
                jsonNode.get("university").get("name").asText(),
                jsonNode.get("university").get("courseOfStudy").asText(),
                jsonNode.get("university").get("newCourseOfStudy").asText(),
                submittedBlocks
        );
    }
}
