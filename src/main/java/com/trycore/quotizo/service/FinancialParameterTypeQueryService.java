package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.*; // for static metamodels
import com.trycore.quotizo.domain.FinancialParameterType;
import com.trycore.quotizo.repository.FinancialParameterTypeRepository;
import com.trycore.quotizo.service.criteria.FinancialParameterTypeCriteria;
import com.trycore.quotizo.service.dto.FinancialParameterTypeDTO;
import com.trycore.quotizo.service.mapper.FinancialParameterTypeMapper;
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
 * Service for executing complex queries for {@link FinancialParameterType} entities in the database.
 * The main input is a {@link FinancialParameterTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FinancialParameterTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FinancialParameterTypeQueryService extends QueryService<FinancialParameterType> {

    private final Logger log = LoggerFactory.getLogger(FinancialParameterTypeQueryService.class);

    private final FinancialParameterTypeRepository financialParameterTypeRepository;

    private final FinancialParameterTypeMapper financialParameterTypeMapper;

    public FinancialParameterTypeQueryService(
        FinancialParameterTypeRepository financialParameterTypeRepository,
        FinancialParameterTypeMapper financialParameterTypeMapper
    ) {
        this.financialParameterTypeRepository = financialParameterTypeRepository;
        this.financialParameterTypeMapper = financialParameterTypeMapper;
    }

    /**
     * Return a {@link Page} of {@link FinancialParameterTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FinancialParameterTypeDTO> findByCriteria(FinancialParameterTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FinancialParameterType> specification = createSpecification(criteria);
        return financialParameterTypeRepository.findAll(specification, page).map(financialParameterTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FinancialParameterTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FinancialParameterType> specification = createSpecification(criteria);
        return financialParameterTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FinancialParameterTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FinancialParameterType> createSpecification(FinancialParameterTypeCriteria criteria) {
        Specification<FinancialParameterType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FinancialParameterType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), FinancialParameterType_.name));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), FinancialParameterType_.active));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), FinancialParameterType_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), FinancialParameterType_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getLastModifiedBy(), FinancialParameterType_.lastModifiedBy)
                );
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), FinancialParameterType_.lastModifiedDate)
                );
            }
            if (criteria.getFinancialParameterId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getFinancialParameterId(),
                        root -> root.join(FinancialParameterType_.financialParameters, JoinType.LEFT).get(FinancialParameter_.id)
                    )
                );
            }
        }
        return specification;
    }
}
