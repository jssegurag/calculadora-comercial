package com.trycore.quotizo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BudgetCriteriaTest {

    @Test
    void newBudgetCriteriaHasAllFiltersNullTest() {
        var budgetCriteria = new BudgetCriteria();
        assertThat(budgetCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void budgetCriteriaFluentMethodsCreatesFiltersTest() {
        var budgetCriteria = new BudgetCriteria();

        setAllFilters(budgetCriteria);

        assertThat(budgetCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void budgetCriteriaCopyCreatesNullFilterTest() {
        var budgetCriteria = new BudgetCriteria();
        var copy = budgetCriteria.copy();

        assertThat(budgetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(budgetCriteria)
        );
    }

    @Test
    void budgetCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var budgetCriteria = new BudgetCriteria();
        setAllFilters(budgetCriteria);

        var copy = budgetCriteria.copy();

        assertThat(budgetCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(budgetCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var budgetCriteria = new BudgetCriteria();

        assertThat(budgetCriteria).hasToString("BudgetCriteria{}");
    }

    private static void setAllFilters(BudgetCriteria budgetCriteria) {
        budgetCriteria.id();
        budgetCriteria.name();
        budgetCriteria.startDate();
        budgetCriteria.endDate();
        budgetCriteria.estimatedDurationDays();
        budgetCriteria.durationMonths();
        budgetCriteria.monthlyHours();
        budgetCriteria.plannedHours();
        budgetCriteria.resourceCount();
        budgetCriteria.income();
        budgetCriteria.otherTaxes();
        budgetCriteria.descriptionOtherTaxes();
        budgetCriteria.withholdingTaxes();
        budgetCriteria.modAndCifCosts();
        budgetCriteria.grossProfit();
        budgetCriteria.grossProfitPercentage();
        budgetCriteria.grossProfitRule();
        budgetCriteria.absorbedFixedCosts();
        budgetCriteria.otherExpenses();
        budgetCriteria.profitBeforeTax();
        budgetCriteria.estimatedTaxes();
        budgetCriteria.estimatedNetProfit();
        budgetCriteria.netMarginPercentage();
        budgetCriteria.netMarginRule();
        budgetCriteria.commissionToReceive();
        budgetCriteria.needsApproval();
        budgetCriteria.approvalDecision();
        budgetCriteria.approvalDate();
        budgetCriteria.approvalTime();
        budgetCriteria.approvalComments();
        budgetCriteria.approvalStatus();
        budgetCriteria.createdBy();
        budgetCriteria.createdDate();
        budgetCriteria.lastModifiedBy();
        budgetCriteria.lastModifiedDate();
        budgetCriteria.resourceAllocationId();
        budgetCriteria.budgetCommentId();
        budgetCriteria.contryId();
        budgetCriteria.userAssignedToId();
        budgetCriteria.userApprovedById();
        budgetCriteria.userOwnerId();
        budgetCriteria.authorizedId();
        budgetCriteria.roleAuthorizedId();
        budgetCriteria.distinct();
    }

    private static Condition<BudgetCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getEstimatedDurationDays()) &&
                condition.apply(criteria.getDurationMonths()) &&
                condition.apply(criteria.getMonthlyHours()) &&
                condition.apply(criteria.getPlannedHours()) &&
                condition.apply(criteria.getResourceCount()) &&
                condition.apply(criteria.getIncome()) &&
                condition.apply(criteria.getOtherTaxes()) &&
                condition.apply(criteria.getDescriptionOtherTaxes()) &&
                condition.apply(criteria.getWithholdingTaxes()) &&
                condition.apply(criteria.getModAndCifCosts()) &&
                condition.apply(criteria.getGrossProfit()) &&
                condition.apply(criteria.getGrossProfitPercentage()) &&
                condition.apply(criteria.getGrossProfitRule()) &&
                condition.apply(criteria.getAbsorbedFixedCosts()) &&
                condition.apply(criteria.getOtherExpenses()) &&
                condition.apply(criteria.getProfitBeforeTax()) &&
                condition.apply(criteria.getEstimatedTaxes()) &&
                condition.apply(criteria.getEstimatedNetProfit()) &&
                condition.apply(criteria.getNetMarginPercentage()) &&
                condition.apply(criteria.getNetMarginRule()) &&
                condition.apply(criteria.getCommissionToReceive()) &&
                condition.apply(criteria.getNeedsApproval()) &&
                condition.apply(criteria.getApprovalDecision()) &&
                condition.apply(criteria.getApprovalDate()) &&
                condition.apply(criteria.getApprovalTime()) &&
                condition.apply(criteria.getApprovalComments()) &&
                condition.apply(criteria.getApprovalStatus()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getResourceAllocationId()) &&
                condition.apply(criteria.getBudgetCommentId()) &&
                condition.apply(criteria.getContryId()) &&
                condition.apply(criteria.getUserAssignedToId()) &&
                condition.apply(criteria.getUserApprovedById()) &&
                condition.apply(criteria.getUserOwnerId()) &&
                condition.apply(criteria.getAuthorizedId()) &&
                condition.apply(criteria.getRoleAuthorizedId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BudgetCriteria> copyFiltersAre(BudgetCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getEstimatedDurationDays(), copy.getEstimatedDurationDays()) &&
                condition.apply(criteria.getDurationMonths(), copy.getDurationMonths()) &&
                condition.apply(criteria.getMonthlyHours(), copy.getMonthlyHours()) &&
                condition.apply(criteria.getPlannedHours(), copy.getPlannedHours()) &&
                condition.apply(criteria.getResourceCount(), copy.getResourceCount()) &&
                condition.apply(criteria.getIncome(), copy.getIncome()) &&
                condition.apply(criteria.getOtherTaxes(), copy.getOtherTaxes()) &&
                condition.apply(criteria.getDescriptionOtherTaxes(), copy.getDescriptionOtherTaxes()) &&
                condition.apply(criteria.getWithholdingTaxes(), copy.getWithholdingTaxes()) &&
                condition.apply(criteria.getModAndCifCosts(), copy.getModAndCifCosts()) &&
                condition.apply(criteria.getGrossProfit(), copy.getGrossProfit()) &&
                condition.apply(criteria.getGrossProfitPercentage(), copy.getGrossProfitPercentage()) &&
                condition.apply(criteria.getGrossProfitRule(), copy.getGrossProfitRule()) &&
                condition.apply(criteria.getAbsorbedFixedCosts(), copy.getAbsorbedFixedCosts()) &&
                condition.apply(criteria.getOtherExpenses(), copy.getOtherExpenses()) &&
                condition.apply(criteria.getProfitBeforeTax(), copy.getProfitBeforeTax()) &&
                condition.apply(criteria.getEstimatedTaxes(), copy.getEstimatedTaxes()) &&
                condition.apply(criteria.getEstimatedNetProfit(), copy.getEstimatedNetProfit()) &&
                condition.apply(criteria.getNetMarginPercentage(), copy.getNetMarginPercentage()) &&
                condition.apply(criteria.getNetMarginRule(), copy.getNetMarginRule()) &&
                condition.apply(criteria.getCommissionToReceive(), copy.getCommissionToReceive()) &&
                condition.apply(criteria.getNeedsApproval(), copy.getNeedsApproval()) &&
                condition.apply(criteria.getApprovalDecision(), copy.getApprovalDecision()) &&
                condition.apply(criteria.getApprovalDate(), copy.getApprovalDate()) &&
                condition.apply(criteria.getApprovalTime(), copy.getApprovalTime()) &&
                condition.apply(criteria.getApprovalComments(), copy.getApprovalComments()) &&
                condition.apply(criteria.getApprovalStatus(), copy.getApprovalStatus()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getResourceAllocationId(), copy.getResourceAllocationId()) &&
                condition.apply(criteria.getBudgetCommentId(), copy.getBudgetCommentId()) &&
                condition.apply(criteria.getContryId(), copy.getContryId()) &&
                condition.apply(criteria.getUserAssignedToId(), copy.getUserAssignedToId()) &&
                condition.apply(criteria.getUserApprovedById(), copy.getUserApprovedById()) &&
                condition.apply(criteria.getUserOwnerId(), copy.getUserOwnerId()) &&
                condition.apply(criteria.getAuthorizedId(), copy.getAuthorizedId()) &&
                condition.apply(criteria.getRoleAuthorizedId(), copy.getRoleAuthorizedId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
