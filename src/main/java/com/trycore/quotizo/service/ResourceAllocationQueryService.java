package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.*; // for static metamodels
import com.trycore.quotizo.domain.ResourceAllocation;
import com.trycore.quotizo.repository.ResourceAllocationRepository;
import com.trycore.quotizo.service.criteria.ResourceAllocationCriteria;
import com.trycore.quotizo.service.dto.ResourceAllocationDTO;
import com.trycore.quotizo.service.mapper.ResourceAllocationMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ResourceAllocation} entities in the database.
 * The main input is a {@link ResourceAllocationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ResourceAllocationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ResourceAllocationQueryService extends QueryService<ResourceAllocation> {

    private final Logger log = LoggerFactory.getLogger(ResourceAllocationQueryService.class);

    private final ResourceAllocationRepository resourceAllocationRepository;

    private final ResourceAllocationMapper resourceAllocationMapper;

    public ResourceAllocationQueryService(
        ResourceAllocationRepository resourceAllocationRepository,
        ResourceAllocationMapper resourceAllocationMapper
    ) {
        this.resourceAllocationRepository = resourceAllocationRepository;
        this.resourceAllocationMapper = resourceAllocationMapper;
    }

    /**
     * Return a {@link Page} of {@link ResourceAllocationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ResourceAllocationDTO> findByCriteria(ResourceAllocationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ResourceAllocation> specification = createSpecification(criteria);
        return resourceAllocationRepository.findAll(specification, page).map(resourceAllocationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ResourceAllocationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ResourceAllocation> specification = createSpecification(criteria);
        return resourceAllocationRepository.count(specification);
    }

    /**
     * Function to convert {@link ResourceAllocationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ResourceAllocation> createSpecification(ResourceAllocationCriteria criteria) {
        Specification<ResourceAllocation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ResourceAllocation_.id));
            }
            if (criteria.getAssignedHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssignedHours(), ResourceAllocation_.assignedHours));
            }
            if (criteria.getTotalCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalCost(), ResourceAllocation_.totalCost));
            }
            if (criteria.getUnits() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnits(), ResourceAllocation_.units));
            }
            if (criteria.getCapacity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCapacity(), ResourceAllocation_.capacity));
            }
            if (criteria.getPlannedHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlannedHours(), ResourceAllocation_.plannedHours));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), ResourceAllocation_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), ResourceAllocation_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getLastModifiedBy(), ResourceAllocation_.lastModifiedBy)
                );
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), ResourceAllocation_.lastModifiedDate)
                );
            }
            if (criteria.getBudgetId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getBudgetId(), root -> root.join(ResourceAllocation_.budget, JoinType.LEFT).get(Budget_.id))
                );
            }
            if (criteria.getResourceId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getResourceId(),
                        root -> root.join(ResourceAllocation_.resource, JoinType.LEFT).get(Resource_.id)
                    )
                );
            }
            if (criteria.getBudgetTemplateId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getBudgetTemplateId(),
                        root -> root.join(ResourceAllocation_.budgetTemplate, JoinType.LEFT).get(BudgetTemplate_.id)
                    )
                );
            }
        }
        return specification;
    }
}
