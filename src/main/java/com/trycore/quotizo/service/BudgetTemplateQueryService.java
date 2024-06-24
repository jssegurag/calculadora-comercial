package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.*; // for static metamodels
import com.trycore.quotizo.domain.BudgetTemplate;
import com.trycore.quotizo.repository.BudgetTemplateRepository;
import com.trycore.quotizo.service.criteria.BudgetTemplateCriteria;
import com.trycore.quotizo.service.dto.BudgetTemplateDTO;
import com.trycore.quotizo.service.mapper.BudgetTemplateMapper;
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
 * Service for executing complex queries for {@link BudgetTemplate} entities in the database.
 * The main input is a {@link BudgetTemplateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BudgetTemplateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BudgetTemplateQueryService extends QueryService<BudgetTemplate> {

    private final Logger log = LoggerFactory.getLogger(BudgetTemplateQueryService.class);

    private final BudgetTemplateRepository budgetTemplateRepository;

    private final BudgetTemplateMapper budgetTemplateMapper;

    public BudgetTemplateQueryService(BudgetTemplateRepository budgetTemplateRepository, BudgetTemplateMapper budgetTemplateMapper) {
        this.budgetTemplateRepository = budgetTemplateRepository;
        this.budgetTemplateMapper = budgetTemplateMapper;
    }

    /**
     * Return a {@link Page} of {@link BudgetTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BudgetTemplateDTO> findByCriteria(BudgetTemplateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BudgetTemplate> specification = createSpecification(criteria);
        return budgetTemplateRepository.findAll(specification, page).map(budgetTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BudgetTemplateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BudgetTemplate> specification = createSpecification(criteria);
        return budgetTemplateRepository.count(specification);
    }

    /**
     * Function to convert {@link BudgetTemplateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BudgetTemplate> createSpecification(BudgetTemplateCriteria criteria) {
        Specification<BudgetTemplate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BudgetTemplate_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), BudgetTemplate_.name));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), BudgetTemplate_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), BudgetTemplate_.endDate));
            }
            if (criteria.getEstimatedDurationDays() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getEstimatedDurationDays(), BudgetTemplate_.estimatedDurationDays)
                );
            }
            if (criteria.getDurationMonths() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDurationMonths(), BudgetTemplate_.durationMonths));
            }
            if (criteria.getMonthlyHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonthlyHours(), BudgetTemplate_.monthlyHours));
            }
            if (criteria.getPlannedHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlannedHours(), BudgetTemplate_.plannedHours));
            }
            if (criteria.getResourceCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getResourceCount(), BudgetTemplate_.resourceCount));
            }
            if (criteria.getIncome() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIncome(), BudgetTemplate_.income));
            }
            if (criteria.getOtherTaxes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOtherTaxes(), BudgetTemplate_.otherTaxes));
            }
            if (criteria.getDescriptionOtherTaxes() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getDescriptionOtherTaxes(), BudgetTemplate_.descriptionOtherTaxes)
                );
            }
            if (criteria.getWithholdingTaxes() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getWithholdingTaxes(), BudgetTemplate_.withholdingTaxes)
                );
            }
            if (criteria.getModAndCifCosts() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModAndCifCosts(), BudgetTemplate_.modAndCifCosts));
            }
            if (criteria.getGrossProfit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGrossProfit(), BudgetTemplate_.grossProfit));
            }
            if (criteria.getGrossProfitPercentage() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getGrossProfitPercentage(), BudgetTemplate_.grossProfitPercentage)
                );
            }
            if (criteria.getGrossProfitRule() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGrossProfitRule(), BudgetTemplate_.grossProfitRule));
            }
            if (criteria.getAbsorbedFixedCosts() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getAbsorbedFixedCosts(), BudgetTemplate_.absorbedFixedCosts)
                );
            }
            if (criteria.getOtherExpenses() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOtherExpenses(), BudgetTemplate_.otherExpenses));
            }
            if (criteria.getProfitBeforeTax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProfitBeforeTax(), BudgetTemplate_.profitBeforeTax));
            }
            if (criteria.getEstimatedTaxes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstimatedTaxes(), BudgetTemplate_.estimatedTaxes));
            }
            if (criteria.getEstimatedNetProfit() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getEstimatedNetProfit(), BudgetTemplate_.estimatedNetProfit)
                );
            }
            if (criteria.getNetMarginPercentage() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getNetMarginPercentage(), BudgetTemplate_.netMarginPercentage)
                );
            }
            if (criteria.getNetMarginRule() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNetMarginRule(), BudgetTemplate_.netMarginRule));
            }
            if (criteria.getCommissionToReceive() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getCommissionToReceive(), BudgetTemplate_.commissionToReceive)
                );
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), BudgetTemplate_.active));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), BudgetTemplate_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), BudgetTemplate_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), BudgetTemplate_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), BudgetTemplate_.lastModifiedDate)
                );
            }
            if (criteria.getResourceAllocationId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getResourceAllocationId(),
                        root -> root.join(BudgetTemplate_.resourceAllocations, JoinType.LEFT).get(ResourceAllocation_.id)
                    )
                );
            }
            if (criteria.getCountryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCountryId(), root -> root.join(BudgetTemplate_.country, JoinType.LEFT).get(Country_.id))
                );
            }
        }
        return specification;
    }
}
