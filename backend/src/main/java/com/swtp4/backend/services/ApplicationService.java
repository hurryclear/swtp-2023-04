package com.swtp4.backend.services;

import com.swtp4.backend.exception.InvalidApplicationStateException;
import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.*;
import com.swtp4.backend.repositories.applicationDtos.*;
import com.swtp4.backend.repositories.dto.ApplicationIDWithFilePaths;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.repositories.entities.keyClasses.ModuleRelationKeyClass;
import com.swtp4.backend.repositories.model.ApplicationPage;
import com.swtp4.backend.repositories.model.ApplicationSearchCriteria;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
public class ApplicationService {

    private UniqueNumberService uniqueNumberService;
    private ApplicationRepository applicationRepository;
    private ModuleStudentRepository moduleStudentRepository;
    private ModuleBlockRepository moduleBlockRepository;
    private ModuleRelationRepository moduleRelationRepository;
    private ModuleUniRepository moduleUniRepository;
    private ApplicationCriteriaRepository applicationCriteriaRepository;

    @Autowired
    public ApplicationService(
            UniqueNumberService uniqueNumberService,
            ApplicationRepository applicationRepository,
            ModuleBlockRepository moduleBlockRepository,
            ModuleStudentRepository moduleStudentRepository,
            ModuleRelationRepository moduleRelationRepository,
            ModuleUniRepository moduleUniRepository,
            ApplicationCriteriaRepository applicationCriteriaRepository) {
        this.uniqueNumberService = uniqueNumberService;
        this.applicationRepository = applicationRepository;
        this.moduleBlockRepository = moduleBlockRepository;
        this.moduleStudentRepository = moduleStudentRepository;
        this.moduleRelationRepository = moduleRelationRepository;
        this.moduleUniRepository = moduleUniRepository;
        this.applicationCriteriaRepository = applicationCriteriaRepository;
    }


    public ApplicationIDWithFilePaths saveSubmitted(SubmittedApplicationDto applicationDto) {
        String applicationID = uniqueNumberService.generateUniqueNumber();
        HashMap<String, String> file_paths = new HashMap<>();

        ApplicationEntity applicationStudent = saveApplicationEntity(applicationID, "Student", applicationDto);
        ApplicationEntity applicationEmployee = saveApplicationEntity(applicationID, "Employee", applicationDto);

        // save all blocks for original (Student) and edited (Employee) application
        for(SubmittedBlock blockDto : applicationDto.blocks()) {
            ModuleBlockEntity blockStudent = saveModuleBlockEntity(applicationStudent, blockDto);
            ModuleBlockEntity blockEmployee = saveModuleBlockEntity(applicationEmployee, blockDto);

            // save all module of that block
            for (SubmittedStudentModule moduleDto : blockDto.studentModules()) {
                ModuleStudentEntity moduleStudent = saveModuleStudentEntity("Student", moduleDto, "");

                // create paths "/applicationID/S-moduleStudentID"
                String pdf_path = "/" + applicationID + "/S-" + moduleStudent.getId().toString();
                moduleStudent.setDescription_pdf(pdf_path);
                moduleStudentRepository.save(moduleStudent);
                // multipart file fields file-0:0, file-0:1...
                file_paths.put("file-" + blockDto.frontend_block_key() + ":" + moduleDto.frontend_module_key(), pdf_path);

                ModuleStudentEntity moduleEmployee = saveModuleStudentEntity("Employee", moduleDto, pdf_path);

                // save all Uni modules that should be credited for that module
                for(Long uniModuleID : blockDto.uniModulesIDs()) {
                    saveModuleRelationEntity(blockStudent, moduleStudent, uniModuleID);
                    saveModuleRelationEntity(blockEmployee, moduleEmployee, uniModuleID);
                }

            }
        }

        return new ApplicationIDWithFilePaths(applicationID, file_paths);
    }

    // save application details
    private ApplicationEntity saveApplicationEntity(String applicationID, String creator, SubmittedApplicationDto applicationDto) {
        return applicationRepository.save(ApplicationEntity.builder()
                .applicationKeyClass(ApplicationKeyClass.builder()
                        .id(applicationID)
                        .creator(creator).build())
                .status("open")
                .formalRejectionReason("")
                .dateOfSubmission(applicationDto.dateOfSubmission())
                .dateLastEdited(applicationDto.dateLastEdited())
                .universityName(applicationDto.university())
                .studentMajor(applicationDto.oldCourseOfStudy())
                .uniMajor(applicationDto.newCourseOfStudy())
                .build());
    }

    // save module blocks of the application
    private ModuleBlockEntity saveModuleBlockEntity(ApplicationEntity applicationStudent, SubmittedBlock blockDto) {
        return moduleBlockRepository.save(ModuleBlockEntity.builder()
                .applicationEntity(applicationStudent)
                .frontendKey(blockDto.frontend_block_key())
                .build());
    }

    // save module of student of one block with its path if provided
    private ModuleStudentEntity saveModuleStudentEntity(String creator, SubmittedStudentModule moduleDto, String path) {
        return moduleStudentRepository.save(ModuleStudentEntity.builder()
                .frontendKey(moduleDto.frontend_module_key())
                .approval("")
                .approvalReason("")
                .number(moduleDto.moduleNumber())
                .title(moduleDto.moduleName())
                .credits(moduleDto.credits())
                .university(moduleDto.university())
                .major(moduleDto.moduleMajor())
                .commentStudent(moduleDto.commentStudent())
                .commentEmployee("")
                .description_pdf(path)
                .creator(creator)
                .build());
    }

    private void saveModuleRelationEntity(ModuleBlockEntity block, ModuleStudentEntity studentModule, Long uniModuleID) {
        // first validate uni module
        ModuleUniEntity uniModule = moduleUniRepository.findById(uniModuleID)
                .orElseThrow(() -> new ResourceNotFoundException("Module not found. The University Module with ID " + uniModuleID + " does not exist and therefore can't be credited."));
        if(!uniModule.getVisibleChoice()){
            throw new ResourceNotFoundException("Module outdated and can't be chosen.");
        }

        // if valid uni module then save the student - uni relation
        moduleRelationRepository.save(ModuleRelationEntity.builder()
                .moduleBlockEntity(block)
                .moduleRelationKeyClass(ModuleRelationKeyClass.builder()
                        .moduleStudentEntity(studentModule)
                        .moduleUniEntity(uniModule)
                        .build())
                .build());
    }

    // update only edited appliction (with creator Employee
    public void updateApplication(EditedApplicationDto applicationDto) {
        ApplicationEntity applicationEmployee = updateApplicationEntity(applicationDto);

        for(EditedBlock blockDto : applicationDto.editedBlocks()) {
            ModuleBlockEntity blockEmployee = updateModuleBlockEntity(blockDto, applicationEmployee);
            for (EditedStudentModule moduleDto : blockDto.editedModules()) {
                ModuleStudentEntity moduleEmployee = updateModuleStudentEntity(moduleDto);

                List<ModuleRelationEntity> moduleRelationEntities = moduleRelationRepository
                        .findAllByModuleRelationKeyClass_ModuleStudentEntity(moduleEmployee);

                // delete ONLY relations which contain studentModules with changed blocks
                for (ModuleRelationEntity moduleRelation : moduleRelationEntities) {
                    Long oldBlockID = moduleRelation.getModuleBlockEntity().getId();
                    Long oldUniModuleID = moduleRelation.getModuleRelationKeyClass().getModuleUniEntity().getId();
                    if (!Objects.equals(oldBlockID, blockEmployee.getId()) ||
                            !blockDto.uniModuleIDs().contains(oldUniModuleID)) {
                        moduleRelationRepository.delete(moduleRelation);
                    }
                }

                for (Long newUniModuleID : blockDto.uniModuleIDs()) {
                    updateModuleRelationEntity(blockEmployee, moduleEmployee, newUniModuleID);
                }
            }
        }

        List<ModuleBlockEntity> moduleBlockEntityList = moduleBlockRepository
                .findAllByApplicationEntity(applicationEmployee);
        for(ModuleBlockEntity oldBlock: moduleBlockEntityList){
            List<ModuleRelationEntity> moduleRelationEntityList = moduleRelationRepository
                    .findByModuleBlockEntity(oldBlock);
            if(moduleRelationEntityList.isEmpty()){
                moduleBlockRepository.delete(oldBlock);
            }
        }
    }

    // add new relation if uni modules changed or blocks changed
    private void updateModuleRelationEntity(ModuleBlockEntity blockEmployee, ModuleStudentEntity moduleEmployee, Long newUniModuleID) {
        ModuleUniEntity moduleUni = moduleUniRepository.findById(newUniModuleID)
                .orElseThrow(() -> new ResourceNotFoundException("UniModule with ID " + newUniModuleID + "not found, cannot updateModuleRelationEntity"));

        moduleRelationRepository.save(ModuleRelationEntity.builder()
                .moduleBlockEntity(blockEmployee)
                .moduleRelationKeyClass(ModuleRelationKeyClass.builder()
                        .moduleStudentEntity(moduleEmployee)
                        .moduleUniEntity(moduleUni)
                        .build())
                .build());
    }

    // update modules of student with creator Employee
    private ModuleStudentEntity updateModuleStudentEntity(EditedStudentModule moduleDto) {
        ModuleStudentEntity moduleEmployee = moduleStudentRepository.findByIdAndCreator(moduleDto.moduleID(), "Employee")
                .orElseThrow(() -> new ResourceNotFoundException("StundentModule with Id "
                        + moduleDto.moduleID() + " not found or creator isn't Employee"));

        moduleEmployee.setFrontendKey(moduleDto.frontendKey());
        moduleEmployee.setNumber(moduleDto.number());
        moduleEmployee.setTitle(moduleDto.title());
        moduleEmployee.setCredits(moduleDto.credits());
        moduleEmployee.setUniversity(moduleDto.university());
        moduleEmployee.setMajor(moduleDto.moduleMajor());
        moduleEmployee.setCommentStudent(moduleDto.commentStudent());
        moduleEmployee.setCommentEmployee(moduleDto.commentEmployee());
        return moduleStudentRepository.save(moduleEmployee);
    }

    // create new or update exiting Block
    private ModuleBlockEntity updateModuleBlockEntity(EditedBlock blockDto, ApplicationEntity applicationEmployee) {
        ModuleBlockEntity blockEmployee;
        if (blockDto.blockID()==null || Objects.equals(blockDto.blockID(), 0L)) {
            blockEmployee = ModuleBlockEntity.builder().applicationEntity(applicationEmployee).build();
        }
        else {
            blockEmployee = moduleBlockRepository.findById(blockDto.blockID())
                    .orElseThrow(() -> new ResourceNotFoundException("ModuleBlock with blockID "
                            + blockDto.blockID() + " not found, and therefore can't be updated."));
        }

        blockEmployee.setFrontendKey(blockDto.frontendKey());
        return moduleBlockRepository.save(blockEmployee);
    }

    private ApplicationEntity updateApplicationEntity(EditedApplicationDto applicationDto) {
        ApplicationEntity applicationEmployee = applicationRepository.findById(ApplicationKeyClass.builder()
                        .id(applicationDto.applicationID())
                        .creator("Employee")
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("Application with applicationID "
                        + applicationDto.applicationID() + " not found, and therefore can't be updated."));

        // only can be set Formal rejection Reason when application is edited but not proven yet
        if(applicationEmployee.getStatus().equals("edited")||applicationEmployee.getStatus().equals("formally rejected"))
            applicationEmployee.setFormalRejectionReason(applicationDto.formalRejection());
        else
            applicationEmployee.setFormalRejectionReason("");
        applicationEmployee.setDateLastEdited(applicationDto.dateLastEdited());
        applicationEmployee.setUniversityName(applicationDto.university());
        applicationEmployee.setStudentMajor(applicationDto.oldCourseOfStudy());
        return applicationRepository.save(applicationEmployee);
    }

    public void updateStatus(String applicationID, String newStatus){
        ApplicationEntity application = applicationRepository.findById(ApplicationKeyClass.builder()
                        .id(applicationID)
                        .creator("Employee")
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("Application with ID "+applicationID+" not found. Status can't be changed."));
        // ensure that application state form open to edited /ready for approval to approval edited, else throw exception
        String oldStatus = application.getStatus();
        if((oldStatus.equals("open") || oldStatus.equals("edited")) && newStatus.equals("edited")){
            application.setStatus(newStatus);
        }
        else if((oldStatus.equals("open") || oldStatus.equals("edited")) && newStatus.equals("ready for approval")){
            application.setStatus(newStatus);
        }
        else if((oldStatus.equals("ready for approval") || oldStatus.equals("edited approval")) && newStatus.equals("edited approval")){
            application.setStatus(newStatus);
        }
        else{
            throw new InvalidApplicationStateException("Status can't change from "+oldStatus+" to "+newStatus);
        }
        applicationRepository.save(application);
    }

    public void statusFormalRejection(EditedApplicationDto applicationDto) {
        ApplicationEntity application = applicationRepository.findById(ApplicationKeyClass.builder()
                        .id(applicationDto.applicationID())
                        .creator("Employee")
                        .build())
                .orElseThrow(() -> new ResourceNotFoundException("Application with ID "+applicationDto.applicationID()+" not found. Status can't be changed."));
        // formally rejected is only when editing but not when proving application
        if(application.getStatus().equals("edited") || application.getStatus().equals("open")){
            if (applicationDto.formalRejection().isEmpty()){
                throw new InvalidApplicationStateException("Formal Rejection Reason is empty, but required to change Application Status to formally rejected.");
            }
            application.setStatus("formally rejected");
            applicationRepository.save(application);

            //All modules got formally rejected if whole application is formally rejected
            for (EditedBlock block: applicationDto.editedBlocks()) {
                for(EditedStudentModule editedModule : block.editedModules()){
                    ModuleStudentEntity module = moduleStudentRepository.findById(editedModule.moduleID())
                            .orElseThrow(() -> new ResourceNotFoundException("Module with ID "+ editedModule.moduleID()+ " not found, cant set Application to formally rejected"));
                    module.setApproval("formally rejected");
                    module.setApprovalReason("Der gesamte Antrag wurde mit oben genannter Begründung formal abgelehnt.");
                    moduleStudentRepository.save(module);
                }
            }
        }
        else{
            throw new InvalidApplicationStateException("Setting status to formally rejected requires previous status open or edited");
        }
    }

    //updates Approval and if all Modules are accepted, rejected or formally rejected, the application status changes to approval finished
    public void updateApproval(EditedApplicationDto applicationDto, List<String> validApprovals) {
        boolean allAreApproved = true;
        for (EditedBlock block : applicationDto.editedBlocks()){
            for(EditedStudentModule editedModule : block.editedModules()){
                ModuleStudentEntity moduleStudent = moduleStudentRepository.findById(editedModule.moduleID())
                        .orElseThrow(() -> new ResourceNotFoundException("Student Module with ID " +editedModule.moduleID() + "not found, cant updateApproval"));
                // if it was not proven yet then..
                if (moduleStudent.getApproval().isEmpty()) {
                    //if there is a reason and a valid Approval then set the Approval
                    if (!editedModule.reason().isEmpty() && validApprovals.contains(editedModule.approval())) {
                        moduleStudent.setApproval(editedModule.approval());
                    } else {
                        // if there is still a module without reason or valid approval-state then approval has not finished yet
                        allAreApproved = false;
                    }
                    moduleStudent.setApprovalReason(editedModule.reason());
                }
                moduleStudentRepository.save(moduleStudent);
            }
        }
        if(allAreApproved) {
            ApplicationEntity application = applicationRepository.findById(new ApplicationKeyClass(applicationDto.applicationID(), "Employee"))
                    .orElseThrow(() -> new ResourceNotFoundException("Application" + applicationDto.applicationID()+ "not found"));
            application.setStatus("approval finished");
            // if all are formally rejected then the whole application is formally rejected
            if(validApprovals.contains("formally rejected")) {
                application.setStatus("formally rejected");
                application.setFormalRejectionReason("Info: Alle Module wurden einzeln formal abgelehnt, die Begründungen finden Sie jeweils bei den Modulen.");
            }
            applicationRepository.save(application);
        }
    }

    // get review of application by ID(for student)
    public ReviewApplicationDto getReviewApplication(String applicationID) {
        ApplicationKeyClass applicationIDAndCreator = ApplicationKeyClass.builder()
                .id(applicationID)
                .creator("Employee")
                .build();
        ApplicationEntity applicationEntity = applicationRepository.findById(applicationIDAndCreator)
                .orElseThrow(() -> new ResourceNotFoundException("Application Id not found" + applicationID));
        // what to return
        ReviewApplicationDto reviewApplicationDto;


        //format Date to correct String: "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        String dateOfSubmission = dateFormat.format(applicationEntity.getDateOfSubmission());
        String dateLastEdited = dateFormat.format(applicationEntity.getDateLastEdited());
        // 1. applicationData (ReviewApplicationDetails)
        ReviewApplicationDetails reviewApplicationDetails = new ReviewApplicationDetails(
                applicationID,
                applicationEntity.getStatus(),
                applicationEntity.getFormalRejectionReason(),
                dateOfSubmission,
                dateLastEdited,
                applicationEntity.getUniversityName(),
                applicationEntity.getStudentMajor(),
                applicationEntity.getUniMajor()
        );
        // 2. moduleFormsData (List<ReviewBlock>)
        List<ModuleBlockEntity> moduleBlockEntityList = moduleBlockRepository.findAllByApplicationEntity(
                applicationEntity);
        List<ReviewBlock> reviewBlockList = new ArrayList<>();
        for (ModuleBlockEntity moduleBlockEntity: moduleBlockEntityList) {
            List<ModuleRelationEntity> moduleRelationEntityList = moduleRelationRepository.findByModuleBlockEntity(moduleBlockEntity);
            List<ModuleStudentEntity> moduleStudentEntityList = new ArrayList<>();
            List<ModuleUniEntity> moduleUniEntityList = new ArrayList<>();

            // adds each module only once to its list
            for (ModuleRelationEntity moduleRelationEntity : moduleRelationEntityList) {
                ModuleStudentEntity moduleStudentEntity = moduleRelationEntity.getModuleRelationKeyClass().getModuleStudentEntity();
                boolean isNewStudentModule = !moduleStudentEntityList.stream()
                        .map(ModuleStudentEntity::getId)
                        .toList()
                        .contains(moduleStudentEntity.getId());
                if (isNewStudentModule) {
                    moduleStudentEntityList.add(moduleStudentEntity);
                }
                ModuleUniEntity moduleUniEntity = moduleRelationEntity.getModuleRelationKeyClass().getModuleUniEntity();
                boolean isNewUniModule = !moduleUniEntityList.stream()
                        .map(ModuleUniEntity::getId)
                        .toList()
                        .contains(moduleUniEntity.getId());
                if (isNewUniModule) {
                    moduleUniEntityList.add(moduleUniEntity);
                }
            }

            List<ReviewStudentModule> reviewStudentModuleList = new ArrayList<>();
            for (ModuleStudentEntity moduleStudentEntity : moduleStudentEntityList) {
                reviewStudentModuleList.add(new ReviewStudentModule(
                        moduleStudentEntity.getFrontendKey(),
                        moduleStudentEntity.getApproval(),
                        moduleStudentEntity.getApprovalReason(),
                        moduleStudentEntity.getNumber(),
                        moduleStudentEntity.getTitle(),
                        moduleStudentEntity.getCredits(),
                        moduleStudentEntity.getUniversity(),
                        moduleStudentEntity.getMajor(),
                        moduleStudentEntity.getCommentStudent()
                ));
            }

            List<ReviewUniModule> reviewUniModuleList = new ArrayList<>();
            for(ModuleUniEntity moduleUniEntity : moduleUniEntityList) {
                reviewUniModuleList.add(new ReviewUniModule(
                        moduleUniEntity.getName(),
                        moduleUniEntity.getNumber()
                ));
            }

            reviewBlockList.add(new ReviewBlock(
                    moduleBlockEntity.getFrontendKey(),
                    reviewStudentModuleList,
                    reviewUniModuleList
            ));
        }
        // instanilize reviewApplicationDto
        reviewApplicationDto = new ReviewApplicationDto(reviewApplicationDetails, reviewBlockList);
        return reviewApplicationDto;

    }


    // get edited application by ID (for employee)
    public EntireOriginalAndEditedApplicationDto getApplicationByID(String applicationID) {

        HashMap<String, EntireApplication> entireOriginalAndEditedApplication = new HashMap<>();

        for (String creator : List.of("Student", "Employee")) {
            ApplicationKeyClass applicationIDAndCreator = ApplicationKeyClass.builder()
                    .id(applicationID)
                    .creator(creator)
                    .build();
            ApplicationEntity applicationEntity = applicationRepository.findById(applicationIDAndCreator)
                    .orElseThrow(() -> new ResourceNotFoundException("Application Id not found" + applicationID));


            //format Date to correct String: "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            String dateOfSubmission = dateFormat.format(applicationEntity.getDateOfSubmission());
            String dateLastEdited = dateFormat.format(applicationEntity.getDateLastEdited());
            // 1. applicationData (ReviewApplicationDetails)
            EntireApplicationDetails applicationData = new EntireApplicationDetails(
                    applicationID,
                    applicationEntity.getStatus(),
                    applicationEntity.getFormalRejectionReason(),
                    dateOfSubmission,
                    dateLastEdited,
                    applicationEntity.getUniversityName(),
                    applicationEntity.getStudentMajor(),
                    applicationEntity.getUniMajor()
            );
            // 2. moduleFormsData (List<ReviewBlock>)
            List<ModuleBlockEntity> moduleBlockEntityList = moduleBlockRepository.findAllByApplicationEntity(
                    applicationEntity);
            List<EntireBlock> entireBlockList = new ArrayList<>();
            for (ModuleBlockEntity moduleBlockEntity : moduleBlockEntityList) {
                List<ModuleRelationEntity> moduleRelationEntityList = moduleRelationRepository.findByModuleBlockEntity(moduleBlockEntity);
                List<ModuleStudentEntity> moduleStudentEntityList = new ArrayList<>();
                List<ModuleUniEntity> moduleUniEntityList = new ArrayList<>();

                // adds each module only once to its list
                for (ModuleRelationEntity moduleRelationEntity : moduleRelationEntityList) {
                    ModuleStudentEntity moduleStudentEntity = moduleRelationEntity.getModuleRelationKeyClass().getModuleStudentEntity();
                    boolean isNewStudentModule = !moduleStudentEntityList.stream()
                            .map(ModuleStudentEntity::getId)
                            .toList()
                            .contains(moduleStudentEntity.getId());
                    if (isNewStudentModule) {
                        moduleStudentEntityList.add(moduleStudentEntity);
                    }
                    ModuleUniEntity moduleUniEntity = moduleRelationEntity.getModuleRelationKeyClass().getModuleUniEntity();
                    boolean isNewUniModule = !moduleUniEntityList.stream()
                            .map(ModuleUniEntity::getId)
                            .toList()
                            .contains(moduleUniEntity.getId());
                    if (isNewUniModule) {
                        moduleUniEntityList.add(moduleUniEntity);
                    }
                }

                List<EntireStudentModule> entireStudentModuleList = new ArrayList<>();
                for (ModuleStudentEntity moduleStudentEntity : moduleStudentEntityList) {
                    entireStudentModuleList.add(new EntireStudentModule(
                            moduleStudentEntity.getFrontendKey(),
                            moduleStudentEntity.getId(),
                            moduleStudentEntity.getApproval(),
                            moduleStudentEntity.getApprovalReason(),
                            moduleStudentEntity.getNumber(),
                            moduleStudentEntity.getTitle(),
                            moduleStudentEntity.getDescription_pdf(),
                            moduleStudentEntity.getCredits(),
                            moduleStudentEntity.getUniversity(),
                            moduleStudentEntity.getMajor(),
                            moduleStudentEntity.getCommentStudent(),
                            moduleStudentEntity.getCommentEmployee()
                    ));
                }

                List<Long> uniModuleList = new ArrayList<>();
                for (ModuleUniEntity moduleUniEntity : moduleUniEntityList) {
                    uniModuleList.add(moduleUniEntity.getId());
                }

                entireBlockList.add(new EntireBlock(
                        moduleBlockEntity.getFrontendKey(),
                        moduleBlockEntity.getId(),
                        entireStudentModuleList,
                        uniModuleList
                ));
            }

            EntireApplication entireApplication = new EntireApplication(applicationData,entireBlockList);

            entireOriginalAndEditedApplication.put(creator,entireApplication);
        }


        //instanilize entireOriginalAndEditedApplicationDto
        return new EntireOriginalAndEditedApplicationDto(
                entireOriginalAndEditedApplication.get("Student"),
                entireOriginalAndEditedApplication.get("Employee"));
    }

    // get overview of applications for employees with certain searching criteria
    public Page<OverviewApplicationDto> getOverviewOffice(ApplicationPage applicationPage,
                                                                ApplicationSearchCriteria applicationSearchCriteria) {
        applicationSearchCriteria.setStatusList(List.of("open", "edited"));
        return applicationCriteriaRepository.findAllWithFilters(applicationPage, applicationSearchCriteria);
    }

    // get overview of applications for employees with certain searching criteria
    public Page<OverviewApplicationDto> getOverviewCommittee(ApplicationPage applicationPage,
                                                             ApplicationSearchCriteria applicationSearchCriteria) {
        applicationSearchCriteria.setStatusList(List.of(
                "open", "edited", "ready for approval", "edited approval"));
        return applicationCriteriaRepository.findAllWithFilters(applicationPage, applicationSearchCriteria);
    }

    // search for old applications to compare them
    public Page<OverviewApplicationDto> searchApplications(ApplicationPage applicationPage,
                                                           ApplicationSearchCriteria applicationSearchCriteria) {

        applicationSearchCriteria.setStatusList(List.of("approval finished"));
        return applicationCriteriaRepository.findAllWithFilters(applicationPage, applicationSearchCriteria);
    }

}