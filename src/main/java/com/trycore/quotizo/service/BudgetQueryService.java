package com.trycore.quotizo.service;

import com.trycore.quotizo.domain.*; // for static metamodels
import com.trycore.quotizo.domain.Budget;
import com.trycore.quotizo.repository.BudgetRepository;
import com.trycore.quotizo.service.criteria.BudgetCriteria;
import com.trycore.quotizo.service.dto.BudgetDTO;
import com.trycore.quotizo.service.mapper.BudgetMapper;
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
 * Service for executing complex queries for {@link Budget} entities in the database.
 * The main input is a {@link BudgetCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link BudgetDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BudgetQueryService extends QueryService<Budget> {

    private final Logger log = LoggerFactory.getLogger(BudgetQueryService.class);

    private final BudgetRepository budgetRepository;

    private final BudgetMapper budgetMapper;

    public BudgetQueryService(BudgetRepository budgetRepository, BudgetMapper budgetMapper) {
        this.budgetRepository = budgetRepository;
        this.budgetMapper = budgetMapper;
    }

    /**
     * Return a {@link Page} of {@link BudgetDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BudgetDTO> findByCriteria(BudgetCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Budget> specification = createSpecification(criteria);
        return budgetRepository.findAll(specification, page).map(budgetMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BudgetCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Budget> specification = createSpecification(criteria);
        return budgetRepository.count(specification);
    }

    /**
     * Function to convert {@link BudgetCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Budget> createSpecification(BudgetCriteria criteria) {
        Specification<Budget> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Budget_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Budget_.name));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Budget_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Budget_.endDate));
            }
            if (criteria.getEstimatedDurationDays() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getEstimatedDurationDays(), Budget_.estimatedDurationDays)
                );
            }
            if (criteria.getDurationMonths() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDurationMonths(), Budget_.durationMonths));
            }
            if (criteria.getMonthlyHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonthlyHours(), Budget_.monthlyHours));
            }
            if (criteria.getPlannedHours() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPlannedHours(), Budget_.plannedHours));
            }
            if (criteria.getResourceCount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getResourceCount(), Budget_.resourceCount));
            }
            if (criteria.getIncome() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIncome(), Budget_.income));
            }
            if (criteria.getOtherTaxes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOtherTaxes(), Budget_.otherTaxes));
            }
            if (criteria.getDescriptionOtherTaxes() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getDescriptionOtherTaxes(), Budget_.descriptionOtherTaxes)
                );
            }
            if (criteria.getWithholdingTaxes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWithholdingTaxes(), Budget_.withholdingTaxes));
            }
            if (criteria.getModAndCifCosts() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModAndCifCosts(), Budget_.modAndCifCosts));
            }
            if (criteria.getGrossProfit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGrossProfit(), Budget_.grossProfit));
            }
            if (criteria.getGrossProfitPercentage() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getGrossProfitPercentage(), Budget_.grossProfitPercentage)
                );
            }
            if (criteria.getGrossProfitRule() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGrossProfitRule(), Budget_.grossProfitRule));
            }
            if (criteria.getAbsorbedFixedCosts() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAbsorbedFixedCosts(), Budget_.absorbedFixedCosts));
            }
            if (criteria.getOtherExpenses() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOtherExpenses(), Budget_.otherExpenses));
            }
            if (criteria.getProfitBeforeTax() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProfitBeforeTax(), Budget_.profitBeforeTax));
            }
            if (criteria.getEstimatedTaxes() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstimatedTaxes(), Budget_.estimatedTaxes));
            }
            if (criteria.getEstimatedNetProfit() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstimatedNetProfit(), Budget_.estimatedNetProfit));
            }
            if (criteria.getNetMarginPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNetMarginPercentage(), Budget_.netMarginPercentage));
            }
            if (criteria.getNetMarginRule() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNetMarginRule(), Budget_.netMarginRule));
            }
            if (criteria.getCommissionToReceive() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommissionToReceive(), Budget_.commissionToReceive));
            }
            if (criteria.getNeedsApproval() != null) {
                specification = specification.and(buildSpecification(criteria.getNeedsApproval(), Budget_.needsApproval));
            }
            if (criteria.getApprovalDecision() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApprovalDecision(), Budget_.approvalDecision));
            }
            if (criteria.getApprovalDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApprovalDate(), Budget_.approvalDate));
            }
            if (criteria.getApprovalTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApprovalTime(), Budget_.approvalTime));
            }
            if (criteria.getApprovalComments() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApprovalComments(), Budget_.approvalComments));
            }
            if (criteria.getApprovalStatus() != null) {
                specification = specification.and(buildStringSpecification(criteria.getApprovalStatus(), Budget_.approvalStatus));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), Budget_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), Budget_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), Budget_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), Budget_.lastModifiedDate));
            }
            if (criteria.getResourceAllocationId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getResourceAllocationId(),
                        root -> root.join(Budget_.resourceAllocations, JoinType.LEFT).get(ResourceAllocation_.id)
                    )
                );
            }
            if (criteria.getBudgetCommentId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getBudgetCommentId(),
                        root -> root.join(Budget_.budgetComments, JoinType.LEFT).get(BudgetComment_.id)
                    )
                );
            }
            if (criteria.getContryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getContryId(), root -> root.join(Budget_.contry, JoinType.LEFT).get(Country_.id))
                );
            }
            if (criteria.getUserAssignedToId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getUserAssignedToId(),
                        root -> root.join(Budget_.userAssignedTo, JoinType.LEFT).get(Users_.id)
                    )
                );
            }
            if (criteria.getUserApprovedById() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getUserApprovedById(),
                        root -> root.join(Budget_.userApprovedBy, JoinType.LEFT).get(Users_.id)
                    )
                );
            }
            if (criteria.getUserOwnerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserOwnerId(), root -> root.join(Budget_.userOwner, JoinType.LEFT).get(Users_.id))
                );
            }
            if (criteria.getAuthorizedId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAuthorizedId(), root -> root.join(Budget_.authorizeds, JoinType.LEFT).get(Users_.id))
                );
            }
            if (criteria.getRoleAuthorizedId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getRoleAuthorizedId(),
                        root -> root.join(Budget_.roleAuthorizeds, JoinType.LEFT).get(UserRole_.id)
                    )
                );
            }
        }
        return specification;
    }
}
