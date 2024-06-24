package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.*; // for static metamodels
import com.trycore.quotizo.domain.FinancialParameter;
import com.trycore.quotizo.repository.FinancialParameterRepository;
import com.trycore.quotizo.service.criteria.FinancialParameterCriteria;
import com.trycore.quotizo.service.dto.FinancialParameterDTO;
import com.trycore.quotizo.service.mapper.FinancialParameterMapper;
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
 * Service for executing complex queries for {@link FinancialParameter} entities in the database.
 * The main input is a {@link FinancialParameterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link FinancialParameterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FinancialParameterQueryService extends QueryService<FinancialParameter> {

    private final Logger log = LoggerFactory.getLogger(FinancialParameterQueryService.class);

    private final FinancialParameterRepository financialParameterRepository;

    private final FinancialParameterMapper financialParameterMapper;

    public FinancialParameterQueryService(
        FinancialParameterRepository financialParameterRepository,
        FinancialParameterMapper financialParameterMapper
    ) {
        this.financialParameterRepository = financialParameterRepository;
        this.financialParameterMapper = financialParameterMapper;
    }

    /**
     * Return a {@link Page} of {@link FinancialParameterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FinancialParameterDTO> findByCriteria(FinancialParameterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FinancialParameter> specification = createSpecification(criteria);
        return financialParameterRepository.findAll(specification, page).map(financialParameterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FinancialParameterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FinancialParameter> specification = createSpecification(criteria);
        return financialParameterRepository.count(specification);
    }

    /**
     * Function to convert {@link FinancialParameterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FinancialParameter> createSpecification(FinancialParameterCriteria criteria) {
        Specification<FinancialParameter> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FinancialParameter_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), FinancialParameter_.name));
            }
            if (criteria.getValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValue(), FinancialParameter_.value));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), FinancialParameter_.active));
            }
            if (criteria.getMandatory() != null) {
                specification = specification.and(buildSpecification(criteria.getMandatory(), FinancialParameter_.mandatory));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), FinancialParameter_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), FinancialParameter_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getLastModifiedBy(), FinancialParameter_.lastModifiedBy)
                );
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), FinancialParameter_.lastModifiedDate)
                );
            }
            if (criteria.getFinancialParameterTypeId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getFinancialParameterTypeId(),
                        root -> root.join(FinancialParameter_.financialParameterType, JoinType.LEFT).get(FinancialParameterType_.id)
                    )
                );
            }
            if (criteria.getCountryId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getCountryId(),
                        root -> root.join(FinancialParameter_.country, JoinType.LEFT).get(Country_.id)
                    )
                );
            }
            if (criteria.getAdministratorId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getAdministratorId(),
                        root -> root.join(FinancialParameter_.administrator, JoinType.LEFT).get(Users_.id)
                    )
                );
            }
            if (criteria.getRoleAuthorizedId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getRoleAuthorizedId(),
                        root -> root.join(FinancialParameter_.roleAuthorizeds, JoinType.LEFT).get(UserRole_.id)
                    )
                );
            }
        }
        return specification;
    }
}
