package com.swtp4.backend.ApplicationControllerTests;


import com.swtp4.backend.repositories.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApplicationServiceUnitTests {

    @InjectMocks
    private ApplicationService applicationService;
    @Mock // mock application repository so that application service will be independent of repository
    private ApplicationRepository applicationRepository;
    private ModuleBlockRepository moduleBlockRepository;
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

//    @Test
//    public void testThatSaveApplicationEntitySuccessfullyCreatesApplication() throws Exception {
//        ApplicationEntity testApplication = ApplicationTestData.createTestApplicationEntityA();
//        testApplication.setApplicationKeyClass(null);
//        UUID processNumber = UUID.randomUUID();
//        String creator = "Student";
//        applicationService.saveApplicationEntity(testApplication, processNumber, creator);
//        ApplicationKeyClass testApplicationKeyClass = ApplicationKeyClass.builder().id(processNumber).creator(creator).build();
//        Optional<ApplicationEntity> result = applicationRepository.findById(testApplicationKeyClass); // can't use applicationRepository because of independence?
//        assertThat(result).isPresent();
//        assertThat(result.get()).isEqualTo(testApplication);
//    }

    @Test
    public void testThatSaveApplicationEntitySuccessfullyCreatesApplication() throws Exception {
        //Given
        ApplicationEntity mockApplicationEntity = ApplicationTestData.createTestApplicationEntityA();
        UUID processNumber = UUID.randomUUID();
        String creator = "Student";
        ApplicationKeyClass testApplicationKeyClass = ApplicationKeyClass.builder().id(processNumber).creator(creator).build();
        mockApplicationEntity.setApplicationKeyClass(testApplicationKeyClass);
        //Mock the calls
        when(applicationRepository.save(mockApplicationEntity)).thenReturn(mockApplicationEntity);
        //when
        ApplicationEntity resultApplicationEntity = applicationService.saveApplicationEntity(mockApplicationEntity, processNumber, creator);
        //Then
        //Verify the result and mock
        assertEquals(mockApplicationEntity, resultApplicationEntity);
    }

    @Test
    public void testThatSaveModuleBlockEntitySuccessfullyCreatesModuleBlock() throws Exception {
        ModuleBlockEntity testModuleBlock = ApplicationTestData.createTestModuleBlockEntityA();
        applicationService.saveModuleBlockEntity(testModuleBlock, testModuleBlock.getApplicationEntity());
        Optional<ModuleBlockEntity> result = Optional.ofNullable(moduleBlockRepository.findByCommentStudentAndApplicationEntity_ApplicationKeyClass_Creator("War cool", "Student"));
        assertThat(result).isPresent();
        //ID is set by the method, so testEntity cant be exactly same to savedEntity but all atrributes must be
        assertThat(result.get().getApplicationEntity()).isEqualTo(testModuleBlock.getApplicationEntity());
        assertThat(result.get().getApproval()).isEqualTo(testModuleBlock.getApproval());
        assertThat(result.get().getCommentStudent()).isEqualTo(testModuleBlock.getCommentStudent());
        assertThat(result.get().getCommentEmployee()).isEqualTo(testModuleBlock.getCommentEmployee());
    }

    @Test
    public void testThatSaveModuleRelationEntitySuccessfullyCreatesModuleRelation() throws Exception {
        ModuleRelationEntity testModuleRelation = ApplicationTestData.createTestModuleRelationEntityA();
        majorUniRepository.save(testModuleRelation.getModuleRelationKeyClass().getModuleUniEntity().getMajorUniEntity());
        moduleUniRepository.save(testModuleRelation.getModuleRelationKeyClass().getModuleUniEntity());
        moduleStudentRepository.save(testModuleRelation.getModuleRelationKeyClass().getModuleStudentEntity());
        applicationService.saveModuleRelationEntity(testModuleRelation.getModuleBlockEntity(), testModuleRelation.getModuleRelationKeyClass().getModuleUniEntity(), testModuleRelation.getModuleRelationKeyClass().getModuleStudentEntity());
        Optional<ModuleRelationEntity> result = moduleRelationRepository.findById(testModuleRelation.getModuleRelationKeyClass());
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testModuleRelation);
    }

    @Test
    @Transactional
    public void testThatGetUniversityModulesByNameSuccessfullyRetrieveEntities() throws Exception {
        ModuleUniEntity testModuleUniA = ApplicationTestData.createTestModuleUniEntityA();
        ModuleUniEntity testModuleUniB = ApplicationTestData.createTestModuleUniEntityB();
        ModuleUniEntity testModuleUniC = ApplicationTestData.createTestModuleUniEntityC();
        List<ModuleUniEntity> testModuleUniEntities = Arrays.asList(testModuleUniA, testModuleUniB, testModuleUniC);
        majorUniRepository.saveAll(Arrays.asList(testModuleUniA.getMajorUniEntity(), testModuleUniB.getMajorUniEntity(), testModuleUniC.getMajorUniEntity()));
        moduleUniRepository.saveAll(testModuleUniEntities);
        List<String> testModuleUniStrings = Arrays.asList(testModuleUniA.getName(), testModuleUniB.getName(), testModuleUniC.getName());
        List<ModuleUniEntity> modulesUniEntityRetrieved = applicationService.getUniversityModulesByName(testModuleUniStrings);
        assertThat(modulesUniEntityRetrieved).isEqualTo(testModuleUniEntities);
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void shouldGetAllApplications() {
        //Given
        ApplicationEntity mockApplicationEntityA = ApplicationTestData.createTestApplicationEntityA();
        List<ApplicationEntity> mockApplicationEntityList = new ArrayList<>();
        mockApplicationEntityList.add(mockApplicationEntityA);
        //Mock the calls
        when(applicationRepository.findByApplicationKeyClass_Creator("Employee")).thenReturn(mockApplicationEntityList);
        //When
        List<ApplicationEntity> applicationEntityList1 = applicationService.getAllApplications();
        //Then
        assertEquals(mockApplicationEntityList.size(), applicationEntityList1.size());
        assertEquals(mockApplicationEntityList, applicationEntityList1);
    }

    @Test
    public void shouldGetApplicationsById() {
        //Given
        UUID id = UUID.randomUUID();
        String creator = "Employee";
        ApplicationKeyClass testApplicationKeyClass = ApplicationKeyClass.builder().id(id).creator(creator).build();
        ApplicationEntity mockApplicationEntity = ApplicationTestData.createTestApplicationEntityA();
        mockApplicationEntity.setApplicationKeyClass(testApplicationKeyClass);
        //Mock the calls
        when(applicationRepository.findByApplicationKeyClass_IdAndApplicationKeyClass_Creator(eq(id), eq(creator)))
                .thenReturn(mockApplicationEntity);
        //When
        ApplicationEntity resultApplicationEntity = applicationService.getApplicationById(id);
        //Then
        //Verify that the repository method was called with the correct parameter
        verify(applicationRepository).findByApplicationKeyClass_IdAndApplicationKeyClass_Creator(eq(id), eq(creator));
        //Verify that result is the same sa the mock
        assertEquals(mockApplicationEntity, resultApplicationEntity);
    }
    @Test
    public void shouldGetApplicationsByStatus() {
        //Given
        String status1 = "open";
        String status2 = "close";
        ApplicationEntity mockApplicationEntityA = ApplicationTestData.createTestApplicationEntityA();
        ApplicationEntity mockApplicationEntityB = ApplicationTestData.createTestApplicationEntityB();
        ApplicationEntity mockApplicationEntityC = ApplicationTestData.createTestApplicationEntityC();
        ApplicationEntity mockApplicationEntityD = ApplicationTestData.createTestApplicationEntityD();
        List<ApplicationEntity> mockApplicationEntityList1 = Arrays.asList(mockApplicationEntityA,mockApplicationEntityB);
        List<ApplicationEntity> mockApplicationEntityList2 = Arrays.asList(mockApplicationEntityC,mockApplicationEntityD);
        //Mock the calls
        when(applicationRepository.findByStatusAndApplicationKeyClass_Creator(status1,"Employee"))
                .thenReturn(mockApplicationEntityList1);
        when(applicationRepository.findByStatusAndApplicationKeyClass_Creator(status2, "Employee"))
                .thenReturn(mockApplicationEntityList2);
        //When
        List<ApplicationEntity> resultApplicationEntityList1 = applicationService.getApplicationsByStatus(status1);
        List<ApplicationEntity> resultApplicationEntityList2 = applicationService.getApplicationsByStatus(status2);
        //Then
        //Verify that the repository method was called with the correct parameter
        verify(applicationRepository).findByStatusAndApplicationKeyClass_Creator(status1, "Employee");
        verify(applicationRepository).findByStatusAndApplicationKeyClass_Creator(status2, "Employee");
        //Verify that result is the same sa the mock
        assertEquals(mockApplicationEntityList1, resultApplicationEntityList1);
        assertEquals(mockApplicationEntityList2, resultApplicationEntityList2);
    }

    @Test
    public void shouldGetApplicationsByMajor() {
        //Given
        String major = "B. Sc. Informatik";
        ApplicationEntity mockApplicationEntityA = ApplicationTestData.createTestApplicationEntityA();
        ApplicationEntity mockApplicationEntityB = ApplicationTestData.createTestApplicationEntityB();
        List<ApplicationEntity> mockApplicationEntityList = Arrays.asList(mockApplicationEntityA,mockApplicationEntityB);
        //Mock the calls
        when(applicationRepository.findByMajorAndApplicationKeyClass_Creator(major, "Employee"))
                .thenReturn(mockApplicationEntityList);
        //When
        List<ApplicationEntity> resultApplicationEntityList = applicationService.getApplicationsByMajor(major);
        //Then
        //Verify that the repository method was called with the correct parameter
        verify(applicationRepository).findByMajorAndApplicationKeyClass_Creator(major, "Employee");
        //Verify that result is the same sa the mock
        assertEquals(mockApplicationEntityList, resultApplicationEntityList);
    }

    @Test
    public void shouldGetApplicationsByUniversity() {
        //Given
        String universityName = "University of Halle";
        ApplicationEntity mockApplicationEntityB = ApplicationTestData.createTestApplicationEntityB();
        ApplicationEntity mockApplicationEntityC = ApplicationTestData.createTestApplicationEntityC();
        List<ApplicationEntity> mockApplicationEntityList = Arrays.asList(mockApplicationEntityB,mockApplicationEntityC);
        //Mock the calls
        when(applicationRepository.findByUniversityNameAndApplicationKeyClass_Creator(universityName, "Employee"))
                .thenReturn(mockApplicationEntityList);
        //When
        List<ApplicationEntity> resultApplicationEntityList = applicationService.getApplicationsByUniversityName(universityName);
        //Then
        //Verify that the repository method was called with the correct parameter
        verify(applicationRepository).findByUniversityNameAndApplicationKeyClass_Creator(universityName, "Employee");
        //Verify that result is the same sa the mock
        assertEquals(mockApplicationEntityList, resultApplicationEntityList);
    }

    @Test
    public void shouldGetApplicationsByDateOfSubmission() {
        //Given
        String dateOfSubmission = "2023-12-31T22:30:42.103Z";
        ApplicationEntity mockApplicationEntity = ApplicationTestData.createTestApplicationEntityA();
//        List<ApplicationEntity> mockApplicationEntityList = Arrays.asList(mockApplicationEntityA);
        //Mock the calls
        when(applicationRepository.findByDateOfSubmissionAndApplicationKeyClass_Creator(dateOfSubmission, "Employee"))
                .thenReturn(mockApplicationEntity);
        //When
        ApplicationEntity resultApplicationEntity = applicationService.getApplicationsByDateOfSubmission(dateOfSubmission);
        //Then
        //Verify that the repository method was called with the correct parameter
        verify(applicationRepository).findByDateOfSubmissionAndApplicationKeyClass_Creator(dateOfSubmission, "Employee");
        //Verify that result is the same sa the mock
        assertEquals(mockApplicationEntity, resultApplicationEntity);
    }
}
