package com.swtp4.backend.services;

import com.swtp4.backend.exception.InvalidApplicationStateException;
import com.swtp4.backend.exception.ResourceNotFoundException;
import com.swtp4.backend.repositories.*;
import com.swtp4.backend.repositories.applicationDtos.*;
import com.swtp4.backend.repositories.dto.ApplicationDto;
import com.swtp4.backend.repositories.dto.ApplicationIDWithFilePaths;
import com.swtp4.backend.repositories.dto.ModuleBlockDto;
import com.swtp4.backend.repositories.entities.*;
import com.swtp4.backend.repositories.entities.keyClasses.ApplicationKeyClass;
import com.swtp4.backend.repositories.entities.keyClasses.ModuleRelationKeyClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ApplicationService {

    private UniqueNumberService uniqueNumberService;
    private ApplicationRepository applicationRepository;
    private ModuleStudentRepository moduleStudentRepository;
    private ModuleBlockRepository moduleBlockRepository;
    private ModuleRelationRepository moduleRelationRepository;
    private ModuleUniRepository moduleUniRepository;

    @Autowired
    public ApplicationService(
            UniqueNumberService uniqueNumberService,
            ApplicationRepository applicationRepository,
            ModuleBlockRepository moduleBlockRepository,
            ModuleStudentRepository moduleStudentRepository,
            ModuleRelationRepository moduleRelationRepository,
            ModuleUniRepository moduleUniRepository) {
        this.uniqueNumberService = uniqueNumberService;
        this.applicationRepository = applicationRepository;
        this.moduleBlockRepository = moduleBlockRepository;
        this.moduleStudentRepository = moduleStudentRepository;
        this.moduleRelationRepository = moduleRelationRepository;
        this.moduleUniRepository = moduleUniRepository;
    }


    public ApplicationIDWithFilePaths saveSubmitted(SubmittedApplicationDto applicationDto) {
        String applicationID = uniqueNumberService.generateUniqueNumber();
        HashMap<String, String> file_paths = new HashMap<>();

        ApplicationEntity applicationStudent = saveApplicationEntity(applicationID, "Student", applicationDto);
        ApplicationEntity applicationEmployee = saveApplicationEntity(applicationID, "Employee", applicationDto);

        for(SubmittedBlock blockDto : applicationDto.blocks()) {
            ModuleBlockEntity blockStudent = saveModuleBlockEntity(applicationStudent, blockDto);
            ModuleBlockEntity blockEmployee = saveModuleBlockEntity(applicationEmployee, blockDto);

            for (SubmittedStudentModule moduleDto : blockDto.studentModules()) {
                ModuleStudentEntity moduleStudent = saveModuleStudentEntity("Student", moduleDto, "");

                // paths "/applicationID/S-moduleStudentID"
                String pdf_path = "/" + applicationID + "/S-" + moduleStudent.getId().toString();
                moduleStudent.setDescription_pdf(pdf_path);
                moduleStudentRepository.save(moduleStudent);
                // multipart file fields file-0:0, file-0:1...
                file_paths.put("file-" + blockDto.frontend_block_key() + ":" + moduleDto.frontend_module_key(), pdf_path);

                ModuleStudentEntity moduleEmployee = saveModuleStudentEntity("Employee", moduleDto, pdf_path);

                for(Long uniModuleID : blockDto.uniModulesIDs()) {
                    saveModuleRelationEntity(blockStudent, moduleStudent, uniModuleID);
                    saveModuleRelationEntity(blockEmployee, moduleEmployee, uniModuleID);
                }

            }
        }

        return new ApplicationIDWithFilePaths(applicationID, file_paths);
    }

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

    private ModuleBlockEntity saveModuleBlockEntity(ApplicationEntity applicationStudent, SubmittedBlock blockDto) {
        return moduleBlockRepository.save(ModuleBlockEntity.builder()
                .applicationEntity(applicationStudent)
                .frontendKey(blockDto.frontend_block_key())
                .build());
    }

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
        ModuleUniEntity uniModule = moduleUniRepository.findById(uniModuleID)
                .orElseThrow(() -> new ResourceNotFoundException("Major not found. The University Module with ID " + uniModuleID + " does not exist and therefore can't be credited."));
        if(!uniModule.getVisibleChoice()){
            throw new ResourceNotFoundException("Module outdated and can't be chosen.");
        }

        moduleRelationRepository.save(ModuleRelationEntity.builder()
                .moduleBlockEntity(block)
                .moduleRelationKeyClass(ModuleRelationKeyClass.builder()
                        .moduleStudentEntity(studentModule)
                        .moduleUniEntity(uniModule)
                        .build())
                .build());
    }

    public void updateApplication(EditedApplicationDto applicationDto) {
        ApplicationEntity applicationEmployee = updateApplicationEntity(applicationDto);

        for(EditedBlock blockDto : applicationDto.editedBlocks()) {
            ModuleBlockEntity blockEmployee = updateModuleBlockEntity(blockDto, applicationEmployee);

            for(EditedStudentModule moduleDto : blockDto.editedModules()) {
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

                for(Long newUniModuleID: blockDto.uniModuleIDs()){
                    updateModuleRelationEntity(blockEmployee, moduleEmployee, newUniModuleID);
                }
            }
        }
    }

    private void updateModuleRelationEntity(ModuleBlockEntity blockEmployee, ModuleStudentEntity moduleEmployee, Long newUniModuleID) {
        ModuleUniEntity moduleUni = moduleUniRepository.findById(newUniModuleID)
                .orElseThrow(() -> new ResourceNotFoundException("Major with ID " + newUniModuleID + "not found"));
        // TODO: vllt muss man erst suchen ob es das schon gibt und dann anpassen
        moduleRelationRepository.save(ModuleRelationEntity.builder()
                .moduleBlockEntity(blockEmployee)
                .moduleRelationKeyClass(ModuleRelationKeyClass.builder()
                        .moduleStudentEntity(moduleEmployee)
                        .moduleUniEntity(moduleUni)
                        .build())
                .build());
    }

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

    private ModuleBlockEntity updateModuleBlockEntity(EditedBlock blockDto, ApplicationEntity applicationEmployee) {
        ModuleBlockEntity blockEmployee;
        if (blockDto.blockID() == null) {
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
        String oldStatus = application.getStatus();
        if(oldStatus.equals("editing in progress") && newStatus.equals("edited")){
            application.setStatus(newStatus);
        }
        else if((oldStatus.equals("open") || oldStatus.equals("edited")) && newStatus.equals("editing in progress")){
            application.setStatus(newStatus);
        }
        else if(oldStatus.equals("editing in progress") && newStatus.equals("ready for approval")){
            application.setStatus(newStatus);
        }
        else if((oldStatus.equals("ready for approval") || oldStatus.equals("edited approval")) && newStatus.equals("approval in progress")){
            application.setStatus(newStatus);
        }
        else if(oldStatus.equals("approval in progress") && newStatus.equals("edited approval")){
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
        if(application.getStatus().equals("editing in progress")){
            if (applicationDto.formalRejection().isEmpty()){
                throw new InvalidApplicationStateException("Formal Rejection Reason is empty, but required to change Application Status to formally rejected.");
            }
            application.setStatus("formally rejected");
            applicationRepository.save(application);
        }
        else{
            throw new InvalidApplicationStateException("Setting status to formally rejected requires previous status editing in progress");
        }
    }

    //gerade nicht benutzt
    public void partialFormalRejection(EditedApplicationDto applicationDTO) {
        boolean allAreRejected = true;
        for (EditedBlock block : applicationDTO.editedBlocks()){
            for (EditedStudentModule module : block.editedModules()){
                ModuleStudentEntity moduleStudent = moduleStudentRepository.findById(module.moduleID())
                        .orElseThrow(() -> new ResourceNotFoundException("Student Module with ID " +module.moduleID() + "not found"));
                if (!module.reason().isEmpty() && module.approval().equals("formally rejected")) {
                    moduleStudent.setApproval("formally rejected");
                }
                else {
                    allAreRejected = false;
                }
                moduleStudent.setApprovalReason(module.reason());
                moduleStudentRepository.save(moduleStudent);
            }
        }
        if(allAreRejected) {
            ApplicationEntity application = applicationRepository.findById(new ApplicationKeyClass(applicationDTO.applicationID(), "Employee"))
                    .orElseThrow(() -> new ResourceNotFoundException("Application" + applicationDTO.applicationID()+ "not found"));
            application.setStatus("approval finished");
            applicationRepository.save(application);
        }
    }

    public void updateApproval(EditedApplicationDto applicationDto, List<String> validApprovals) {
        boolean allAreApproved = true;
        for (EditedBlock block : applicationDto.editedBlocks()){
            for(EditedStudentModule module : block.editedModules()){
                ModuleStudentEntity moduleStudent = moduleStudentRepository.findById(module.moduleID())
                        .orElseThrow(() -> new ResourceNotFoundException("Student Module with ID " +module.moduleID() + "not found"));
                if (!module.reason().isEmpty() && validApprovals.contains(module.approval())){
                    moduleStudent.setApproval(module.approval());
                }
                else{
                    // if there is still a module without reason or valid approval-state then approval has not finished yet
                    if(module.reason().isEmpty() || !module.approval().equals("formally rejected"))
                        allAreApproved = false;
                }
                moduleStudent.setApprovalReason(module.reason());
                moduleStudentRepository.save(moduleStudent);
            }
        }
        if(allAreApproved) {
            ApplicationEntity application = applicationRepository.findById(new ApplicationKeyClass(applicationDto.applicationID(), "Employee"))
                    .orElseThrow(() -> new ResourceNotFoundException("Application" + applicationDto.applicationID()+ "not found"));
            application.setStatus("approval finished");
            applicationRepository.save(application);
        }
    }



    public List<ApplicationEntity> getAllApplications() {
        return applicationRepository.findByApplicationKeyClass_Creator("Employee");
    }
    //get unique application by applicationkeyclass (id, creator)

    public ApplicationEntity getApplicationById(String id) {
        return applicationRepository.findByApplicationKeyClass_IdAndApplicationKeyClass_Creator(id, "Employee");
    }

    public List<ApplicationEntity> getApplicationsByStatus(String status) {
        return applicationRepository.findByStatusAndApplicationKeyClass_Creator(status, "Employee");
    }
    public List<ApplicationEntity> getApplicationsByMajor(String major) {
        return applicationRepository.findByUniMajorAndApplicationKeyClass_Creator(major, "Employee");
    }

    public List<ApplicationEntity> getApplicationsByUniversityName(String universityName) {
        return applicationRepository.findByUniversityNameAndApplicationKeyClass_Creator(universityName, "Employee");
    }

    public ApplicationEntity getApplicationsByDateOfSubmission(String dateOfSubmission) {
        return applicationRepository.findByDateOfSubmissionAndApplicationKeyClass_Creator(dateOfSubmission, "Employee");
    }
    // not complete

    public List<ApplicationEntity> getApplicationsByDateOfSubmissionBefore(String dateOfSubmission) {
        return applicationRepository.findByDateOfSubmissionBeforeAndApplicationKeyClass_Creator(dateOfSubmission, "Employee");
    }
    // not complete

    public List<ApplicationEntity> getApplicationsByDateOfSubmissionAfter(String dateOfSubmission) {
        return applicationRepository.findByDateOfSubmissionAfterAndApplicationKeyClass_Creator(dateOfSubmission, "Employee");
    }

    public ReviewApplicationDto getReviewApplication(String applicationID) {
        ApplicationKeyClass applicationIDAndCreator = ApplicationKeyClass.builder()
                .id(applicationID)
                .creator("Employee")
                .build();
        ApplicationEntity applicationEntity = applicationRepository.findById(applicationIDAndCreator)
                .orElseThrow(() -> new ResourceNotFoundException("Application Id not found" + applicationID));

        List<ModuleBlockEntity> moduleBlockEntityList = moduleBlockRepository.findAllByApplicationEntity(
                applicationEntity);

        List<ReviewBlock> reviewBlockList = new ArrayList<>();

        for (ModuleBlockEntity moduleBlockEntity: moduleBlockEntityList) {
            List<ModuleRelationEntity> moduleRelationEntityList = moduleRelationRepository.findByModuleBlockEntity(moduleBlockEntity);
            List<ModuleStudentEntity> moduleStudentEntityList = new ArrayList<>();
            List<ModuleUniEntity> moduleUniEntityList = new ArrayList<>();

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


        ReviewApplicationDetails reviewApplicationDetails = new ReviewApplicationDetails(
                applicationID,
                applicationEntity.getStatus(),
                applicationEntity.getFormalRejectionReason(),
                applicationEntity.getDateOfSubmission(),
                applicationEntity.getDateLastEdited(),
                applicationEntity.getUniversityName(),
                applicationEntity.getStudentMajor(),
                applicationEntity.getUniMajor()
        );

        return new ReviewApplicationDto(reviewApplicationDetails, reviewBlockList);

    }

    public EditedApplicationDto getEditedApplication(String applicationID) {
        return null;
    }
}