package com.swtp4.backend.ApplicationControllerTests;


import com.swtp4.backend.repositories.*;
import com.swtp4.backend.repositories.applicationDtos.*;
import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.entities.ModuleBlockEntity;
import com.swtp4.backend.repositories.entities.ModuleRelationEntity;
import com.swtp4.backend.repositories.entities.ModuleUniEntity;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.services.ApplicationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApplicationServiceUnitTests {

    @InjectMocks
    private ApplicationService applicationService;
    @Mock // mock application repository so that application service will be independent of repository
    private ApplicationRepository applicationRepository;
    @Mock
    private ModuleBlockRepository moduleBlockRepository;
    @Mock
    private ModuleRelationRepository moduleRelationRepository;
    private ModuleUniRepository moduleUniRepository;
    private ModuleStudentRepository moduleStudentRepository;
    private MajorUniRepository majorUniRepository;

    @Autowired
    public ApplicationServiceUnitTests(ApplicationService applicationService,
                                       ApplicationRepository applicationRepository,
                                       ModuleBlockRepository moduleBlockRepository,
                                       ModuleRelationRepository moduleRelationRepository,
                                       ModuleUniRepository moduleUniRepository,
                                       ModuleStudentRepository moduleStudentRepository,
                                       MajorUniRepository majorUniRepository) {
        this.applicationService = applicationService;
        this.applicationRepository = applicationRepository;
        this.moduleBlockRepository = moduleBlockRepository;
        this.moduleRelationRepository = moduleRelationRepository;
        this.moduleUniRepository = moduleUniRepository;
        this.moduleStudentRepository = moduleStudentRepository;
        this.majorUniRepository = majorUniRepository;
    }


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldGetEntireApplicationByID () throws ParseException {

        // mock data
        String applicationID = "93e9a8f5-6ecc-4ac8-8854-13a8b7f3e6a2";
        //original application
        //applicationData
        EntireApplicationDetails originalApplicationsData = new EntireApplicationDetails(
                "93e9a8f5-6ecc-4ac8-8854-13a8b7f3e6a2",
                "edited",
                "Jo du",
                "2024-03-05T13:56:51.560Z",
                "2024-03-05T13:56:51.560Z",
                "University of Halle",
                "B.Sc Informatik",
                "B.Sc Informatik"
        );
        //moduleFormsData
        List<EntireStudentModule> studentModuleList = new ArrayList<>();
        EntireStudentModule entireStudentModule = new EntireStudentModule(
                0L,
                1L,
                "angenommen",
                "Jo du",
                "420",
                "AlgoDat 1.5",
                "/23-23-23-23-23-23-23-23-23-23-23/S-3",
                5L,
                "University of Halle",
                "B.Sc. Informatik",
                "War cool",
                "Das nicht so cool"
        );
        studentModuleList.add(entireStudentModule);
        List<Long> modules2bCredited = new ArrayList<>();
        modules2bCredited.add(1L);
        modules2bCredited.add(2L);
        List<EntireBlock> originalModuleFormsDataList = new ArrayList<>();
        EntireBlock entireBlock = new EntireBlock(
                0L,
                1L,
                studentModuleList,
                modules2bCredited
        );
        originalModuleFormsDataList.add(entireBlock);

        //entire original and edited application
        EntireApplication originalApplication =  new EntireApplication(originalApplicationsData, originalModuleFormsDataList);
        EntireApplication editedApplication =  new EntireApplication(originalApplicationsData, originalModuleFormsDataList);

        //applicationEntity
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        ApplicationKeyClass applicationKeyClass = ApplicationKeyClass.builder()
                .id("93e9a8f5-6ecc-4ac8-8854-13a8b7f3e6a2")
                .creator("Employee").build();
        ApplicationEntity applicationEntity = ApplicationEntity.builder()
                .applicationKeyClass(applicationKeyClass)
                .status("edited")
                .dateOfSubmission(dateFormat.parse("2024-03-05T13:56:51.560Z"))
                .dateLastEdited(dateFormat.parse("2024-03-05T13:56:51.560Z"))
                .universityName("University of Halle")
                .uniMajor("B.Sc. Informatik")
                .studentMajor("B.Sc. Informatik")
                .build();

        //moduleBlockEntity
        ModuleBlockEntity moduleBlockEntity = new ModuleBlockEntity();

        // Mock repository responses
        when(applicationRepository.findById(any())).thenReturn(Optional.of(applicationEntity));
        when(moduleBlockRepository.findAllByApplicationEntity(any())).thenReturn(Collections.emptyList());
//        when(moduleRelationRepository.findByModuleBlockEntity(any())).thenReturn(Collections.emptyList());

        // When
        EntireOriginalAndEditedApplicationDto result = applicationService.getApplicationByID(applicationID);

        // Then: Assertions
        assertEquals("93e9a8f5-6ecc-4ac8-8854-13a8b7f3e6a2", result.original().applicationData().applicationID());
        assertEquals("edited", result.original().applicationData().status());
        assertEquals("University of Halle", result.original().applicationData().university());
        assertEquals("2024-03-05T13:56:51.560Z", result.original().applicationData().dateOfSubmission());
        assertEquals("2024-03-05T13:56:51.560Z", result.original().applicationData().dateLastEdited());
        assertEquals("B.Sc. Informatik", result.original().applicationData().newCourseOfStudy());
        assertEquals("B.Sc. Informatik", result.original().applicationData().oldCourseOfStudy());


        // Verify the repository interactions
//        verify(applicationRepository, times(2)).findById(any());
//        verify(moduleBlockRepository, times(1)).findAllByApplicationEntity(any());
//        verify(moduleRelationRepository, times(1)).findByModuleBlockEntity(any());

    }

//    @Test
//    public void shouldGetAllApplications() {
//        //Given
//        ApplicationEntity mockApplicationEntityA = ApplicationTestData.createTestApplicationEntityA();
//        List<ApplicationEntity> mockApplicationEntityList = new ArrayList<>();
//        mockApplicationEntityList.add(mockApplicationEntityA);
//        //Mock the calls
//        when(applicationRepository.findByApplicationKeyClass_Creator("Employee")).thenReturn(mockApplicationEntityList);
//        //When
//        List<ApplicationEntity> applicationEntityList1 = applicationService.getAllApplications();
//        //Then
//        assertEquals(mockApplicationEntityList.size(), applicationEntityList1.size());
//        assertEquals(mockApplicationEntityList, applicationEntityList1);
//    }

//    @Test
//    public void shouldGetApplicationsById() {
//        //Given
//        UUID id = UUID.randomUUID();
//        String creator = "Employee";
//        ApplicationKeyClass testApplicationKeyClass = ApplicationKeyClass.builder().id(id).creator(creator).build();
//        ApplicationEntity mockApplicationEntity = ApplicationTestData.createTestApplicationEntityA();
//        mockApplicationEntity.setApplicationKeyClass(testApplicationKeyClass);
//        //Mock the calls
//        when(applicationRepository.findByApplicationKeyClass_IdAndApplicationKeyClass_Creator(eq(id), eq(creator)))
//                .thenReturn(mockApplicationEntity);
//        //When
//        ApplicationEntity resultApplicationEntity = applicationService.getApplicationById(id);
//        //Then
//        //Verify that the repository method was called with the correct parameter
//        verify(applicationRepository).findByApplicationKeyClass_IdAndApplicationKeyClass_Creator(eq(id), eq(creator));
//        //Verify that result is the same sa the mock
//        assertEquals(mockApplicationEntity, resultApplicationEntity);
//    }
//    @Test
//    public void shouldGetApplicationsByStatus() {
//        //Given
//        String status1 = "open";
//        String status2 = "close";
//        ApplicationEntity mockApplicationEntityA = ApplicationTestData.createTestApplicationEntityA();
//        ApplicationEntity mockApplicationEntityB = ApplicationTestData.createTestApplicationEntityB();
//        ApplicationEntity mockApplicationEntityC = ApplicationTestData.createTestApplicationEntityC();
//        ApplicationEntity mockApplicationEntityD = ApplicationTestData.createTestApplicationEntityD();
//        List<ApplicationEntity> mockApplicationEntityList1 = Arrays.asList(mockApplicationEntityA,mockApplicationEntityB);
//        List<ApplicationEntity> mockApplicationEntityList2 = Arrays.asList(mockApplicationEntityC,mockApplicationEntityD);
//        //Mock the calls
//        when(applicationRepository.findByStatusAndApplicationKeyClass_Creator(status1,"Employee"))
//                .thenReturn(mockApplicationEntityList1);
//        when(applicationRepository.findByStatusAndApplicationKeyClass_Creator(status2, "Employee"))
//                .thenReturn(mockApplicationEntityList2);
//        //When
//        List<ApplicationEntity> resultApplicationEntityList1 = applicationService.getApplicationsByStatus(status1);
//        List<ApplicationEntity> resultApplicationEntityList2 = applicationService.getApplicationsByStatus(status2);
//        //Then
//        //Verify that the repository method was called with the correct parameter
//        verify(applicationRepository).findByStatusAndApplicationKeyClass_Creator(status1, "Employee");
//        verify(applicationRepository).findByStatusAndApplicationKeyClass_Creator(status2, "Employee");
//        //Verify that result is the same sa the mock
//        assertEquals(mockApplicationEntityList1, resultApplicationEntityList1);
//        assertEquals(mockApplicationEntityList2, resultApplicationEntityList2);
//    }

//    @Test
//    public void shouldGetApplicationsByMajor() {
//        //Given
//        String major = "B. Sc. Informatik";
//        ApplicationEntity mockApplicationEntityA = ApplicationTestData.createTestApplicationEntityA();
//        ApplicationEntity mockApplicationEntityB = ApplicationTestData.createTestApplicationEntityB();
//        List<ApplicationEntity> mockApplicationEntityList = Arrays.asList(mockApplicationEntityA,mockApplicationEntityB);
//        //Mock the calls
//        when(applicationRepository.findByUniMajorAndApplicationKeyClass_Creator(major, "Employee"))
//                .thenReturn(mockApplicationEntityList);
//        //When
//        List<ApplicationEntity> resultApplicationEntityList = applicationService.getApplicationsByMajor(major);
//        //Then
//        //Verify that the repository method was called with the correct parameter
//        verify(applicationRepository).findByUniMajorAndApplicationKeyClass_Creator(major, "Employee");
//        //Verify that result is the same sa the mock
//        assertEquals(mockApplicationEntityList, resultApplicationEntityList);
//    }

//    @Test
//    public void shouldGetApplicationsByUniversity() {
//        //Given
//        String universityName = "University of Halle";
//        ApplicationEntity mockApplicationEntityB = ApplicationTestData.createTestApplicationEntityB();
//        ApplicationEntity mockApplicationEntityC = ApplicationTestData.createTestApplicationEntityC();
//        List<ApplicationEntity> mockApplicationEntityList = Arrays.asList(mockApplicationEntityB,mockApplicationEntityC);
//        //Mock the calls
//        when(applicationRepository.findByUniversityNameAndApplicationKeyClass_Creator(universityName, "Employee"))
//                .thenReturn(mockApplicationEntityList);
//        //When
//        List<ApplicationEntity> resultApplicationEntityList = applicationService.getApplicationsByUniversityName(universityName);
//        //Then
//        //Verify that the repository method was called with the correct parameter
//        verify(applicationRepository).findByUniversityNameAndApplicationKeyClass_Creator(universityName, "Employee");
//        //Verify that result is the same sa the mock
//        assertEquals(mockApplicationEntityList, resultApplicationEntityList);
//    }

//    @Test
//    public void shouldGetApplicationsByDateOfSubmission() {
//        //Given
//        String dateOfSubmission = "2023-12-31T22:30:42.103Z";
//        ApplicationEntity mockApplicationEntity = ApplicationTestData.createTestApplicationEntityA();
////        List<ApplicationEntity> mockApplicationEntityList = Arrays.asList(mockApplicationEntityA);
//        //Mock the calls
//        when(applicationRepository.findByDateOfSubmissionAndApplicationKeyClass_Creator(dateOfSubmission, "Employee"))
//                .thenReturn(mockApplicationEntity);
//        //When
//        ApplicationEntity resultApplicationEntity = applicationService.getApplicationsByDateOfSubmission(dateOfSubmission);
//        //Then
//        //Verify that the repository method was called with the correct parameter
//        verify(applicationRepository).findByDateOfSubmissionAndApplicationKeyClass_Creator(dateOfSubmission, "Employee");
//        //Verify that result is the same sa the mock
//        assertEquals(mockApplicationEntity, resultApplicationEntity);
//    }
}
