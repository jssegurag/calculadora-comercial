package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.*; // for static metamodels
import com.trycore.quotizo.domain.BudgetComment;
import com.trycore.quotizo.repository.BudgetCommentRepository;
import com.trycore.quotizo.service.criteria.BudgetCommentCriteria;
import com.trycore.quotizo.service.dto.BudgetCommentDTO;
import com.trycore.quotizo.service.mapper.BudgetCommentMapper;
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
 * Service for executing complex queries for {@link BudgetComment} entities in the database.
 * The main input is a {@link BudgetCommentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BudgetCommentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BudgetCommentQueryService extends QueryService<BudgetComment> {

    private final Logger log = LoggerFactory.getLogger(BudgetCommentQueryService.class);

    private final BudgetCommentRepository budgetCommentRepository;

    private final BudgetCommentMapper budgetCommentMapper;

    public BudgetCommentQueryService(BudgetCommentRepository budgetCommentRepository, BudgetCommentMapper budgetCommentMapper) {
        this.budgetCommentRepository = budgetCommentRepository;
        this.budgetCommentMapper = budgetCommentMapper;
    }

    /**
     * Return a {@link Page} of {@link BudgetCommentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BudgetCommentDTO> findByCriteria(BudgetCommentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BudgetComment> specification = createSpecification(criteria);
        return budgetCommentRepository.findAll(specification, page).map(budgetCommentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BudgetCommentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BudgetComment> specification = createSpecification(criteria);
        return budgetCommentRepository.count(specification);
    }

    /**
     * Function to convert {@link BudgetCommentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BudgetComment> createSpecification(BudgetCommentCriteria criteria) {
        Specification<BudgetComment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BudgetComment_.id));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), BudgetComment_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), BudgetComment_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), BudgetComment_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), BudgetComment_.lastModifiedDate));
            }
            if (criteria.getBudgetId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getBudgetId(), root -> root.join(BudgetComment_.budget, JoinType.LEFT).get(Budget_.id))
                );
            }
        }
        return specification;
    }
}
