package com.swtp4.backend.repositories;

import com.swtp4.backend.repositories.applicationDtos.OverviewApplicationDto;
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

    public Page<OverviewApplicationDto> findAllWithFilters(
            ApplicationPage applicationPage,
            ApplicationSearchCriteria applicationSearchCriteria) {
        CriteriaQuery<OverviewApplicationDto> criteriaQuery = criteriaBuilder.createQuery(OverviewApplicationDto.class);
        Root<OverviewApplicationDto> overviewApplicationDtoRoot = criteriaQuery.from(OverviewApplicationDto.class);
        Predicate predicate = getPredicate(applicationSearchCriteria, overviewApplicationDtoRoot);
        criteriaQuery.where(predicate);
        setOrder(applicationPage, criteriaQuery, overviewApplicationDtoRoot);

        TypedQuery<OverviewApplicationDto> typedQuery = entityManager.createQuery(criteriaQuery);
        typedQuery.setFirstResult(applicationPage.getPageNumber() * applicationPage.getPageSize());
        typedQuery.setMaxResults(applicationPage.getPageSize());

        Pageable pageable = getPageable(applicationPage);

        long applicationsCount = getApplicationsCount(predicate);

        return new PageImpl<>(typedQuery.getResultList(), pageable, applicationsCount);
    }

    private Predicate getPredicate(ApplicationSearchCriteria applicationSearchCriteria,
                                   Root<OverviewApplicationDto> overviewApplicationDtoRoot) {
        List<Predicate> predicates = new ArrayList<>();
        if(Objects.nonNull(applicationSearchCriteria.getApplicationID())) {
            predicates.add(
                    criteriaBuilder.like(
                            overviewApplicationDtoRoot.get("applicationID"),
                            "%" + applicationSearchCriteria.getApplicationID() + "%"));
        }

        if(Objects.nonNull(applicationSearchCriteria.getStatus())) {
            predicates.add(
                    criteriaBuilder.like(
                            overviewApplicationDtoRoot.get("status"),
                            "%" + applicationSearchCriteria.getStatus() + "%"));
        }
        if(Objects.nonNull(applicationSearchCriteria.getUniversity())) {
            predicates.add(
                    criteriaBuilder.like(
                            overviewApplicationDtoRoot.get("university"),
                            "%" + applicationSearchCriteria.getUniversity() + "%"));
        }
        if(Objects.nonNull(applicationSearchCriteria.getDateOfSubmission())) {
            predicates.add(
                    criteriaBuilder.like(
                            overviewApplicationDtoRoot.get("dateOfSubmission"),
                            "%" + applicationSearchCriteria.getDateOfSubmission() + "%"));
        }
        if(Objects.nonNull(applicationSearchCriteria.getDateLastEdited())) {
            predicates.add(
                    criteriaBuilder.like(
                            overviewApplicationDtoRoot.get("dateLastEdited"),
                            "%" + applicationSearchCriteria.getDateLastEdited() + "%"));
        }

        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }

    private void setOrder(ApplicationPage applicationPage,
                          CriteriaQuery<OverviewApplicationDto> criteriaQuery,
                          Root<OverviewApplicationDto> overviewApplicationDtoRoot) {
        if(applicationPage.getSortDirection().equals(Sort.Direction.ASC)) {
            criteriaQuery.orderBy(criteriaBuilder.asc(overviewApplicationDtoRoot.get(applicationPage.getSortBy())));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.desc(overviewApplicationDtoRoot.get(applicationPage.getSortBy())));
        }
    }

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
