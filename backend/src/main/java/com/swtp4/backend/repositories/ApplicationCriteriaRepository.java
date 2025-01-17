package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.applicationDtos.OverviewApplicationDto;
import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.model.ApplicationPage;
import com.swtp4.backend.repositories.model.ApplicationSearchCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Repository
@Slf4j
public class ApplicationCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public ApplicationCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<OverviewApplicationDto> findAllWithFilters(
            ApplicationPage applicationPage,
            ApplicationSearchCriteria applicationSearchCriteria) {

        // construct SELECT statement
        CriteriaQuery<ApplicationEntity> criteriaQuery = criteriaBuilder.createQuery(ApplicationEntity.class);
        Root<ApplicationEntity> applicationEntityRoot = criteriaQuery.from(ApplicationEntity.class);
        // predicate represents the conditions that will be applied to filter the results
        Predicate predicate = getPredicate(applicationSearchCriteria, applicationEntityRoot);
        criteriaQuery.distinct(true).where(predicate);
        //order the results
        setOrder(applicationPage, criteriaQuery, applicationEntityRoot);

        // A TypedQuery is a JPA query that is typed to return a specific result type. Here, we're creating a TypedQuery of type OverviewApplicationDto, which represents the result set of the query.
        TypedQuery<ApplicationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        // paging
        typedQuery.setFirstResult(applicationPage.getPageNumber() * applicationPage.getPageSize());
        typedQuery.setMaxResults(applicationPage.getPageSize());

        Pageable pageable = getPageable(applicationPage);

        long applicationsCount = getApplicationsCount(applicationSearchCriteria);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        List<ApplicationEntity> applicationEntityResultList = typedQuery.getResultList();
        List<OverviewApplicationDto> overviewApplicationDtoList = applicationEntityResultList
                .stream()
                .map(entity -> new OverviewApplicationDto(entity.getApplicationKeyClass().getId(),
                        entity.getUniversityName(),
                        dateFormat.format(entity.getDateOfSubmission()),
                        dateFormat.format(entity.getDateLastEdited()),
                        entity.getStatus()
                        )).toList();

        log.info("OverviewApplicationDtos: {}", overviewApplicationDtoList);
        return new PageImpl<>(overviewApplicationDtoList, pageable, applicationsCount);
    }

    // filtering, adds up all Conditions which must be matched (all Filters)
    private Predicate getPredicate(ApplicationSearchCriteria applicationSearchCriteria,
                                   Root<ApplicationEntity> applicationEntityRoot) {
        List<Predicate> predicates = new ArrayList<>();
        //only edited Applications are used
        predicates.add(criteriaBuilder.equal(applicationEntityRoot.get("applicationKeyClass").get("creator"), "Employee"));
        if(Objects.nonNull(applicationSearchCriteria.getApplicationID())) {
            predicates.add(
                    criteriaBuilder.like(
                            applicationEntityRoot.get("applicationKeyClass").get("id"),
                            "%" + applicationSearchCriteria.getApplicationID() + "%"));
        }
        // only specific states are allowed in each overview or comparison menu
        if(Objects.nonNull(applicationSearchCriteria.getStatusList())) {
            predicates.add(
                    applicationEntityRoot.get("status").in(applicationSearchCriteria.getStatusList()));
        }
        if(Objects.nonNull(applicationSearchCriteria.getUniversityName())) {
            predicates.add(
                    criteriaBuilder.like(
                            applicationEntityRoot.get("universityName"),
                            "%" + applicationSearchCriteria.getUniversityName() + "%"));
        }
        if(Objects.nonNull(applicationSearchCriteria.getUniMajor())) {
            predicates.add(
                    criteriaBuilder.like(
                            applicationEntityRoot.get("uniMajor"),
                            "%" + applicationSearchCriteria.getUniMajor() + "%"));
        }
        if(Objects.nonNull(applicationSearchCriteria.getDateOfSubmission())) {
            predicates.add(
                    criteriaBuilder.greaterThanOrEqualTo(
                            applicationEntityRoot.<Date>get("dateOfSubmission"),
                            applicationSearchCriteria.getDateOfSubmission()));
        }
        // filter by module names of an application
        if(Objects.nonNull(applicationSearchCriteria.getModule())) {
            predicates.add(
                    criteriaBuilder.like(
                            applicationEntityRoot
                                    .join("moduleBlocks")
                                    .join("blockRelations")
                                    .join("moduleRelationKeyClass")
                                    .join("moduleStudentEntity")
                                    .get("title"),
                            "%" + applicationSearchCriteria.getModule() + "%"));
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    // sorting but only after one property at a time
    private void setOrder(ApplicationPage applicationPage,
                          CriteriaQuery<ApplicationEntity> criteriaQuery,
                          Root<ApplicationEntity> applicationEntityRoot) {
        if(applicationPage.getSortDirection().equals(Sort.Direction.ASC)) {
            // sorting criteria applicationID is transformed to match entity
            if(applicationPage.getSortBy().equals("applicationID")) {
                criteriaQuery.orderBy(criteriaBuilder.asc(applicationEntityRoot.get("applicationKeyClass").get("id")));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.asc(applicationEntityRoot.get(applicationPage.getSortBy())));
            }
        } else {
            // sorting criteria applicationID is transformed to match entity
            if(applicationPage.getSortBy().equals("applicationID")) {
                criteriaQuery.orderBy(criteriaBuilder.desc(applicationEntityRoot.get("applicationKeyClass").get("id")));
            } else {
                criteriaQuery.orderBy(criteriaBuilder.desc(applicationEntityRoot.get(applicationPage.getSortBy())));
            }
        }
    }
    // paging, only one page with a specific number of entities is provided
    private Pageable getPageable(ApplicationPage applicationPage) {
        Sort sort = Sort.by(applicationPage.getSortDirection(), applicationPage.getSortBy());
        return PageRequest.of(applicationPage.getPageNumber(), applicationPage.getPageSize(), sort);
    }

    // count the total number of entities found with the querry
    private long getApplicationsCount(ApplicationSearchCriteria applicationSearchCriteria) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<ApplicationEntity> countRoot = countQuery.from(ApplicationEntity.class);
        Predicate predicate = getPredicate(applicationSearchCriteria, countRoot);
        countQuery.select(criteriaBuilder.countDistinct(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
