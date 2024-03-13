package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.applicationDtos.OverviewApplicationDto;
import com.swtp4.backend.repositories.dto.ApplicationDto;
import com.swtp4.backend.repositories.entities.ApplicationEntity;
import com.swtp4.backend.repositories.model.ApplicationPage;
import com.swtp4.backend.repositories.model.ApplicationSearchCriteria;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@Repository
public class ApplicationCriteriaRepository {

    private final EntityManager entityManager;
    private final CriteriaBuilder criteriaBuilder;

    public ApplicationCriteriaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.criteriaBuilder = entityManager.getCriteriaBuilder();
    }

    public Page<ApplicationEntity> findAllWithFilters(
            ApplicationPage applicationPage,
            ApplicationSearchCriteria applicationSearchCriteria) {

        // construct SELECT statement
        CriteriaQuery<ApplicationEntity> criteriaQuery = criteriaBuilder.createQuery(ApplicationEntity.class);
        Root<ApplicationEntity> applicationEntityRoot = criteriaQuery.from(ApplicationEntity.class);
        // predicate represents the conditions that will be applied to filter the results
        Predicate predicate = getPredicate(applicationSearchCriteria, applicationEntityRoot);
        criteriaQuery.where(predicate);
        //order the results
        setOrder(applicationPage, criteriaQuery, applicationEntityRoot);

        // A TypedQuery is a JPA query that is typed to return a specific result type. Here, we're creating a TypedQuery of type OverviewApplicationDto, which represents the result set of the query.
        TypedQuery<ApplicationEntity> typedQuery = entityManager.createQuery(criteriaQuery);
        // paging
        typedQuery.setFirstResult(applicationPage.getPageNumber() * applicationPage.getPageSize());
        typedQuery.setMaxResults(applicationPage.getPageSize());

        Pageable pageable = getPageable(applicationPage);

        long applicationsCount = getApplicationsCount(predicate);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

        List<ApplicationEntity> applicationEntityResultList = typedQuery.getResultList();
        List<OverviewApplicationDto> overviewApplicationDtoList = applicationEntityResultList
                .stream()
                .map(entity -> new OverviewApplicationDto(entity.getApplicationKeyClass().getId(),
                        entity.getUniversityName(),
                        dateFormat.format(entity.getDateOfSubmission()),
                        entity.getStatus()
                        )).toList();

        log.info("OverviewApplicationDtos: {}", overviewApplicationDtoList);
        return new PageImpl<>(overviewApplicationDtoList, pageable, 1);
    }

    // filtering
    private Predicate getPredicate(ApplicationSearchCriteria applicationSearchCriteria,
                                   Root<ApplicationEntity> applicationEntityRoot) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(criteriaBuilder.equal(applicationEntityRoot.get("applicationKeyClass").get("creator"), "Employee"));
        predicates.add(criteriaBuilder.equal(applicationEntityRoot.get("status"),"approval finished"));
        if(Objects.nonNull(applicationSearchCriteria.getApplicationID())) {
            predicates.add(
                    criteriaBuilder.like(
                            applicationEntityRoot.get("applicationKeyClass").get("id"),
                            "%" + applicationSearchCriteria.getApplicationID() + "%"));
        }

//        if(Objects.nonNull(applicationSearchCriteria.getStatus())) {
//            predicates.add(
//                    criteriaBuilder.like(
//                            overviewApplicationDtoRoot.get("status"),
//                            "%" + applicationSearchCriteria.getStatus() + "%"));
//        }

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
        //todo
        if(Objects.nonNull(applicationSearchCriteria.getDateOfSubmission())) {
            predicates.add(
                    criteriaBuilder.like(
                            applicationEntityRoot.get("dateOfSubmission"),
                            "%" + applicationSearchCriteria.getDateOfSubmission() + "%"));
        }
        if(Objects.nonNull(applicationSearchCriteria.getDateOfSubmission())) {
            predicates.add(
                    criteriaBuilder.like(
                            applicationEntityRoot.get("dateOfSubmission"),
                            "%" + applicationSearchCriteria.getDateOfSubmission() + "%"));
        }
        criteriaBuilder.or();
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    // sorting
    private void setOrder(ApplicationPage applicationPage,
                          CriteriaQuery<ApplicationEntity> criteriaQuery,
                          Root<ApplicationEntity> applicationEntityRoot) {
        if(applicationPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(applicationEntityRoot.get(applicationPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(applicationEntityRoot.get(applicationPage.getSortBy())));
        }
    }

    // paging
    private Pageable getPageable(ApplicationPage applicationPage) {
        Sort sort = Sort.by(applicationPage.getSortDirection(), applicationPage.getSortBy());
        return PageRequest.of(applicationPage.getPageNumber(), applicationPage.getPageSize(), sort);
    }

    private long getApplicationsCount(Predicate predicate) {
        CriteriaQuery<Long> countQuery = criteriaBuilder.createQuery(Long.class);
        Root<OverviewApplicationDto> countRoot = countQuery.from(OverviewApplicationDto.class);
        countQuery.select(criteriaBuilder.count(countRoot)).where(predicate);
        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
