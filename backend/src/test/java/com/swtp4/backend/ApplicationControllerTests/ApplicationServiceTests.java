package com.swtp4.backend.ApplicationControllerTests;


import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.*;
import com.swtp4.backend.repositories.applicationDtos.*;
import com.swtp4.backend.repositories.dto.ApplicationIDWithFilePaths;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.services.ApplicationService;
import com.swtp4.backend.services.UniqueNumberService;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTests {
    @InjectMocks
    private ApplicationService applicationService;
    @Mock
    private UniqueNumberService uniqueNumberService;
    @Mock
    private ApplicationRepository applicationRepository;
    @Mock
    private ModuleStudentRepository moduleStudentRepository;
    @Mock
    private ModuleBlockRepository moduleBlockRepository;
    @Mock
    private ModuleRelationRepository moduleRelationRepository;
    @Mock
    private ModuleUniRepository moduleUniRepository;



    @Test
    public void whenSaveSubmittedHasInvalidUniModules_ItThrowsResourceNotFound() throws Exception {
        when(uniqueNumberService.generateUniqueNumber()).thenReturn("123ID");
        when(applicationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(moduleStudentRepository.save(any())).thenAnswer(invocation -> {
            ModuleStudentEntity module = invocation.getArgument(0);
            module.setId(0L);
            return module;
        });
        when(moduleBlockRepository.save(any())).thenAnswer(invocation -> {
            ModuleBlockEntity block = invocation.getArgument(0);
            block.setId(0L);
            return block;
        });
        when(moduleUniRepository.findById(any())).thenReturn(Optional.empty());



        SubmittedApplicationDto applicationDto = new SubmittedApplicationDto("",
                "",
                ""  ,
                "",
                "",
                List.of(new SubmittedBlock(
                        0L,
                        List.of(new SubmittedStudentModule("","",3L,"","","",0L)),
                        List.of(1L)
                        )));

        assertThrows(ResourceNotFoundException.class, () -> {
            applicationService.saveSubmitted(applicationDto);
        });
    }

    @Test
    public void whenSaveSubmittedHasInvisibleUniModule_ItThrowsResourceNotFound() throws Exception {
        when(uniqueNumberService.generateUniqueNumber()).thenReturn("123ID");
        when(applicationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(moduleStudentRepository.save(any())).thenAnswer(invocation -> {
            ModuleStudentEntity module = invocation.getArgument(0);
            module.setId(0L);
            return module;
        });
        when(moduleBlockRepository.save(any())).thenAnswer(invocation -> {
            ModuleBlockEntity block = invocation.getArgument(0);
            block.setId(0L);
            return block;
        });
        when(moduleUniRepository.findById(any())).thenReturn(Optional.of(ModuleUniEntity.builder()
                .id(1L)
                .name("")
                .number("")
                .visibleChoice(false)
                .build()));



        SubmittedApplicationDto applicationDto = new SubmittedApplicationDto("",
                "",
                ""  ,
                "",
                "",
                List.of(new SubmittedBlock(
                        0L,
                        List.of(new SubmittedStudentModule("","",3L,"","","",0L)),
                        List.of(1L)
                )));

        assertThrows(ResourceNotFoundException.class, () -> {
            applicationService.saveSubmitted(applicationDto);
        });
    }

    @Test
    public void whenSaveSubmittedSuccessfully_IdAndHashMapIsReturned() throws Exception {
        when(uniqueNumberService.generateUniqueNumber()).thenReturn("123ID");
        when(applicationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(moduleStudentRepository.save(any())).thenAnswer(invocation -> {
            ModuleStudentEntity module = invocation.getArgument(0);
            module.setId(0L);
            return module;
        });
        when(moduleBlockRepository.save(any())).thenAnswer(invocation -> {
            ModuleBlockEntity block = invocation.getArgument(0);
            block.setId(0L);
            return block;
        });
        when(moduleUniRepository.findById(any())).thenReturn(Optional.of(ModuleUniEntity.builder()
                .id(1L)
                .name("")
                .number("")
                .visibleChoice(true)
                .build()));



        SubmittedApplicationDto applicationDto = new SubmittedApplicationDto("",
                "",
                ""  ,
                "",
                "",
                List.of(new SubmittedBlock(
                        0L,
                        List.of(new SubmittedStudentModule("","",3L,"","","",0L)),
                        List.of(1L)
                )));

        HashMap<String, String> expected_file_path = new HashMap<String, String>();
        String expected_applicationID = "123ID";
        expected_file_path.put("file-0:0", "/123ID/S-0");

        ApplicationIDWithFilePaths applicationIDWithFilePaths = applicationService.saveSubmitted(applicationDto);

        HashMap<String, String> actual_file_path = applicationIDWithFilePaths.getFilesAndPaths();
        String actual_applicationID = applicationIDWithFilePaths.getApplicationID();

        System.out.println(actual_file_path);

        assertEquals(expected_file_path, actual_file_path);
        assertNotNull(actual_file_path);
        assertEquals(expected_applicationID, actual_applicationID);
    }

    @Test
    public void whenEditedApplicationIDNotExists_ItThrowsResourceNotFound() throws Exception {
        when(applicationRepository.findById(any())).thenReturn(Optional.empty());
        EditedApplicationDto applicationDto = new EditedApplicationDto("asdfklfjf","",
                "",
                ""  ,
                "",
                List.of(new EditedBlock(
                        1L,
                        0L,
                        List.of(new EditedStudentModule(0L, 0L,"","","3L","",3L,"","", "", "")),
                        List.of(1L)
                )));

        assertThrows(ResourceNotFoundException.class, () -> {
            applicationService.updateApplication(applicationDto);
        });
    }

    @Test
    public void whenEditedBlockIDisNull_DoesNotThrowResourceNotFound() throws Exception {
        when(applicationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(applicationRepository.findById(any())).thenReturn(Optional.of(ApplicationEntity.builder()
                .applicationKeyClass(ApplicationKeyClass.builder()
                        .id("123ID")
                        .creator("Employee")
                        .build())
                .build()));
        when(moduleStudentRepository.save(any())).thenAnswer(invocation -> {
            ModuleStudentEntity module = invocation.getArgument(0);
            module.setId(0L);
            return module;
        });
        when(moduleStudentRepository.findByIdAndCreator(any(), any())).thenReturn(Optional.of(ModuleStudentEntity.builder()
                .id(0L)
                .creator("Employee")
                .build()));

        when(moduleBlockRepository.save(any())).thenAnswer(invocation -> {
            ModuleBlockEntity block = invocation.getArgument(0);
            block.setId(0L);
            return block;
        });
        when(moduleUniRepository.findById(any())).thenReturn(Optional.of(ModuleUniEntity.builder()
                .id(1L)
                .name("")
                .number("")
                .visibleChoice(true)
                .build()));
        when(moduleRelationRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));



        EditedBlock blockDto = new EditedBlock(
                1L,
                null,
                List.of(new EditedStudentModule(0L, 0L,"","","3L","",3L,"","", "", "")),
                List.of(1L));
        EditedApplicationDto applicationDto = new EditedApplicationDto("asdfklfjf","",
                "",
                ""  ,
                "",
                List.of(blockDto));

        assertDoesNotThrow(() -> applicationService.updateApplication(applicationDto));
    }
}
