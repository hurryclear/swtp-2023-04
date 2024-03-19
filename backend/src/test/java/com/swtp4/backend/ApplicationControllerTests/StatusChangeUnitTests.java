package com.swtp4.backend.ApplicationControllerTests;

import com.swtp4.backend.exception.InvalidApplicationStateException;
import com.swtp4.backend.repositories.ApplicationRepository;
import com.swtp4.backend.repositories.ModuleStudentRepository;
import com.swtp4.backend.repositories.applicationDtos.EditedApplicationDto;
import com.swtp4.backend.repositories.applicationDtos.EditedBlock;
import com.swtp4.backend.repositories.applicationDtos.EditedStudentModule;
import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.entities.ModuleStudentEntity;
import com.swtp4.backend.services.ApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusChangeUnitTests {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private ModuleStudentRepository moduleStudentRepository;

    @InjectMocks
    private ApplicationService applicationService;

    @Test
    void testUpdateStatus_ValidStatusChange() {
        String applicationId = "1";
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setStatus("open");

        when(applicationRepository.findById(any())).thenReturn(Optional.of(applicationEntity));

        assertDoesNotThrow(() -> applicationService.updateStatus(applicationId, "edited"));
        assertEquals("edited", applicationEntity.getStatus());
        verify(applicationRepository, times(1)).save(applicationEntity);
    }

    @Test
    void testUpdateStatus_InvalidStatusChange() {
        String applicationId = "1";
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setStatus("ready for approval");

        when(applicationRepository.findById(any())).thenReturn(Optional.of(applicationEntity));

        assertThrows(InvalidApplicationStateException.class, () -> applicationService.updateStatus(applicationId, "open"));
        assertEquals("ready for approval", applicationEntity.getStatus());
        verify(applicationRepository, never()).save(applicationEntity);
    }

    @Test
    void testStatusFormalRejection_ValidStatusChange() {
        String applicationId = "1";

        EditedStudentModule editedModule = new EditedStudentModule(1L, 1L,""
                ,"Reason", "", "", 1L,"", "","", "");

        EditedBlock block = new EditedBlock(1L, 1L, List.of(editedModule), null);
        EditedApplicationDto applicationDto = new EditedApplicationDto(
                applicationId,
                null,
                "",
                "",
                "Reason",
                List.of(block));

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setStatus("open");
        ModuleStudentEntity moduleStudentEntity = new ModuleStudentEntity();
        moduleStudentEntity.setApproval("");

        when(moduleStudentRepository.findById(any())).thenReturn(Optional.of(moduleStudentEntity));
        when(applicationRepository.findById(any())).thenReturn(Optional.of(applicationEntity));

        assertDoesNotThrow(() -> applicationService.statusFormalRejection(applicationDto));
        assertEquals("formally rejected", applicationEntity.getStatus());
        verify(applicationRepository, times(1)).save(applicationEntity);
    }

    @Test
    void testUpdateApproval_AllModulesApproved() {
        Long moduleId = 1L;
        List<String> validApprovals = new ArrayList<>();
        validApprovals.add("Approved");

        EditedStudentModule editedModule = new EditedStudentModule(moduleId, 1L,"Approved"
                ,"Reason", "", "", 1L,"", "","", "");

        EditedBlock block = new EditedBlock(1L, 1L, List.of(editedModule), null);
        EditedApplicationDto applicationDto = new EditedApplicationDto("",null,"","","",List.of(block));

        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setStatus("");
        ModuleStudentEntity moduleStudentEntity = new ModuleStudentEntity();
        moduleStudentEntity.setApproval("");

        when(moduleStudentRepository.findById(any())).thenReturn(Optional.of(moduleStudentEntity));
        when(applicationRepository.findById(any())).thenReturn(Optional.of(applicationEntity));

        assertDoesNotThrow(() -> applicationService.updateApproval(applicationDto, validApprovals));
        assertEquals("Approved", moduleStudentEntity.getApproval());
        assertEquals("Reason", moduleStudentEntity.getApprovalReason());
        verify(moduleStudentRepository, times(1)).save(moduleStudentEntity);
    }

    @Test
    void testUpdateApproval_NotAllModulesApproved() {
        Long moduleId = 1L;

        EditedStudentModule editedModule = new EditedStudentModule(moduleId, 1L,""
                ,"Reason", "", "", 1L,"", "","", "");

        List<String> validApprovals = new ArrayList<>();
        validApprovals.add("Approved");

        EditedBlock block = new EditedBlock(1L, 1L, List.of(editedModule), null);
        EditedApplicationDto applicationDto = new EditedApplicationDto("",null,"","","",List.of(block));


        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setStatus("");
        ModuleStudentEntity moduleStudentEntity = new ModuleStudentEntity();
        moduleStudentEntity.setApproval("");

        when(moduleStudentRepository.findById(any())).thenReturn(Optional.of(moduleStudentEntity));

        assertDoesNotThrow(() -> applicationService.updateApproval(applicationDto, validApprovals));
        assertEquals("", moduleStudentEntity.getApproval());
        assertEquals("Reason", moduleStudentEntity.getApprovalReason());
        verify(moduleStudentRepository, times(1)).save(moduleStudentEntity);
    }
}
