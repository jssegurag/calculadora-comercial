package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.*; // for static metamodels
import com.trycore.quotizo.domain.RuleAssignment;
import com.trycore.quotizo.repository.RuleAssignmentRepository;
import com.trycore.quotizo.service.criteria.RuleAssignmentCriteria;
import com.trycore.quotizo.service.dto.RuleAssignmentDTO;
import com.trycore.quotizo.service.mapper.RuleAssignmentMapper;
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
 * Service for executing complex queries for {@link RuleAssignment} entities in the database.
 * The main input is a {@link RuleAssignmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RuleAssignmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RuleAssignmentQueryService extends QueryService<RuleAssignment> {

    private final Logger log = LoggerFactory.getLogger(RuleAssignmentQueryService.class);

    private final RuleAssignmentRepository ruleAssignmentRepository;

    private final RuleAssignmentMapper ruleAssignmentMapper;

    public RuleAssignmentQueryService(RuleAssignmentRepository ruleAssignmentRepository, RuleAssignmentMapper ruleAssignmentMapper) {
        this.ruleAssignmentRepository = ruleAssignmentRepository;
        this.ruleAssignmentMapper = ruleAssignmentMapper;
    }

    /**
     * Return a {@link Page} of {@link RuleAssignmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RuleAssignmentDTO> findByCriteria(RuleAssignmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RuleAssignment> specification = createSpecification(criteria);
        return ruleAssignmentRepository.findAll(specification, page).map(ruleAssignmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RuleAssignmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RuleAssignment> specification = createSpecification(criteria);
        return ruleAssignmentRepository.count(specification);
    }

    /**
     * Function to convert {@link RuleAssignmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RuleAssignment> createSpecification(RuleAssignmentCriteria criteria) {
        Specification<RuleAssignment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RuleAssignment_.id));
            }
            if (criteria.getEntityName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEntityName(), RuleAssignment_.entityName));
            }
            if (criteria.getEntityId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEntityId(), RuleAssignment_.entityId));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), RuleAssignment_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), RuleAssignment_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), RuleAssignment_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), RuleAssignment_.lastModifiedDate)
                );
            }
            if (criteria.getDroolsRuleFileId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getDroolsRuleFileId(),
                        root -> root.join(RuleAssignment_.droolsRuleFile, JoinType.LEFT).get(DroolsRuleFile_.id)
                    )
                );
            }
        }
        return specification;
    }
}
