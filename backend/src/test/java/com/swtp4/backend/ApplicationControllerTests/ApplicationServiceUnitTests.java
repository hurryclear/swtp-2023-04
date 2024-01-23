package com.swtp4.backend.ApplicationControllerTests;


import com.swtp4.backend.repositories.*;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.services.ApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ApplicationServiceUnitTests {

    private ApplicationService applicationService;
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

    @Test
    public void testThatSaveApplicationEntitySuccessfullyCreatesApplication() throws Exception {
        ApplicationEntity testApplication = ApplicationTestData.createTestApplicationEntityA();
        testApplication.setApplicationKeyClass(null);
        UUID processNumber = UUID.randomUUID();
        String creator = "Student";
        applicationService.saveApplicationEntity(testApplication, processNumber, creator);
        ApplicationKeyClass testApplicationKeyClass = ApplicationKeyClass.builder().id(processNumber).creator(creator).build();
        Optional<ApplicationEntity> result = applicationRepository.findById(testApplicationKeyClass);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testApplication);
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
}
