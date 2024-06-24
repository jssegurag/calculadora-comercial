package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.*; // for static metamodels
import com.trycore.quotizo.domain.DroolsRuleFile;
import com.trycore.quotizo.repository.DroolsRuleFileRepository;
import com.trycore.quotizo.service.criteria.DroolsRuleFileCriteria;
import com.trycore.quotizo.service.dto.DroolsRuleFileDTO;
import com.trycore.quotizo.service.mapper.DroolsRuleFileMapper;
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
 * Service for executing complex queries for {@link DroolsRuleFile} entities in the database.
 * The main input is a {@link DroolsRuleFileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DroolsRuleFileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DroolsRuleFileQueryService extends QueryService<DroolsRuleFile> {

    private final Logger log = LoggerFactory.getLogger(DroolsRuleFileQueryService.class);

    private final DroolsRuleFileRepository droolsRuleFileRepository;

    private final DroolsRuleFileMapper droolsRuleFileMapper;

    public DroolsRuleFileQueryService(DroolsRuleFileRepository droolsRuleFileRepository, DroolsRuleFileMapper droolsRuleFileMapper) {
        this.droolsRuleFileRepository = droolsRuleFileRepository;
        this.droolsRuleFileMapper = droolsRuleFileMapper;
    }

    /**
     * Return a {@link Page} of {@link DroolsRuleFileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DroolsRuleFileDTO> findByCriteria(DroolsRuleFileCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DroolsRuleFile> specification = createSpecification(criteria);
        return droolsRuleFileRepository.findAll(specification, page).map(droolsRuleFileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DroolsRuleFileCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DroolsRuleFile> specification = createSpecification(criteria);
        return droolsRuleFileRepository.count(specification);
    }

    /**
     * Function to convert {@link DroolsRuleFileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DroolsRuleFile> createSpecification(DroolsRuleFileCriteria criteria) {
        Specification<DroolsRuleFile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DroolsRuleFile_.id));
            }
            if (criteria.getFileName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileName(), DroolsRuleFile_.fileName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), DroolsRuleFile_.description));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), DroolsRuleFile_.active));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), DroolsRuleFile_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), DroolsRuleFile_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), DroolsRuleFile_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), DroolsRuleFile_.lastModifiedDate)
                );
            }
            if (criteria.getRuleAssignmentId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getRuleAssignmentId(),
                        root -> root.join(DroolsRuleFile_.ruleAssignments, JoinType.LEFT).get(RuleAssignment_.id)
                    )
                );
            }
        }
        return specification;
    }
}
