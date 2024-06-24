package com.trycore.quotizo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BudgetTemplateCriteriaTest {

    @Test
    void newBudgetTemplateCriteriaHasAllFiltersNullTest() {
        var budgetTemplateCriteria = new BudgetTemplateCriteria();
        assertThat(budgetTemplateCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void budgetTemplateCriteriaFluentMethodsCreatesFiltersTest() {
        var budgetTemplateCriteria = new BudgetTemplateCriteria();

        setAllFilters(budgetTemplateCriteria);

        assertThat(budgetTemplateCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void budgetTemplateCriteriaCopyCreatesNullFilterTest() {
        var budgetTemplateCriteria = new BudgetTemplateCriteria();
        var copy = budgetTemplateCriteria.copy();

        assertThat(budgetTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(budgetTemplateCriteria)
        );
    }

    @Test
    void budgetTemplateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var budgetTemplateCriteria = new BudgetTemplateCriteria();
        setAllFilters(budgetTemplateCriteria);

        var copy = budgetTemplateCriteria.copy();

        assertThat(budgetTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(budgetTemplateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var budgetTemplateCriteria = new BudgetTemplateCriteria();

        assertThat(budgetTemplateCriteria).hasToString("BudgetTemplateCriteria{}");
    }

    private static void setAllFilters(BudgetTemplateCriteria budgetTemplateCriteria) {
        budgetTemplateCriteria.id();
        budgetTemplateCriteria.name();
        budgetTemplateCriteria.startDate();
        budgetTemplateCriteria.endDate();
        budgetTemplateCriteria.estimatedDurationDays();
        budgetTemplateCriteria.durationMonths();
        budgetTemplateCriteria.monthlyHours();
        budgetTemplateCriteria.plannedHours();
        budgetTemplateCriteria.resourceCount();
        budgetTemplateCriteria.income();
        budgetTemplateCriteria.otherTaxes();
        budgetTemplateCriteria.descriptionOtherTaxes();
        budgetTemplateCriteria.withholdingTaxes();
        budgetTemplateCriteria.modAndCifCosts();
        budgetTemplateCriteria.grossProfit();
        budgetTemplateCriteria.grossProfitPercentage();
        budgetTemplateCriteria.grossProfitRule();
        budgetTemplateCriteria.absorbedFixedCosts();
        budgetTemplateCriteria.otherExpenses();
        budgetTemplateCriteria.profitBeforeTax();
        budgetTemplateCriteria.estimatedTaxes();
        budgetTemplateCriteria.estimatedNetProfit();
        budgetTemplateCriteria.netMarginPercentage();
        budgetTemplateCriteria.netMarginRule();
        budgetTemplateCriteria.commissionToReceive();
        budgetTemplateCriteria.active();
        budgetTemplateCriteria.createdBy();
        budgetTemplateCriteria.createdDate();
        budgetTemplateCriteria.lastModifiedBy();
        budgetTemplateCriteria.lastModifiedDate();
        budgetTemplateCriteria.resourceAllocationId();
        budgetTemplateCriteria.countryId();
        budgetTemplateCriteria.distinct();
    }

    private static Condition<BudgetTemplateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
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
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getResourceAllocationId()) &&
                condition.apply(criteria.getCountryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BudgetTemplateCriteria> copyFiltersAre(
        BudgetTemplateCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
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
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getResourceAllocationId(), copy.getResourceAllocationId()) &&
                condition.apply(criteria.getCountryId(), copy.getCountryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
