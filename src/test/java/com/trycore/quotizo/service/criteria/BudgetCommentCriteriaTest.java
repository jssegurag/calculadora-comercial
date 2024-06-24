package com.trycore.quotizo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BudgetCommentCriteriaTest {

    @Test
    void newBudgetCommentCriteriaHasAllFiltersNullTest() {
        var budgetCommentCriteria = new BudgetCommentCriteria();
        assertThat(budgetCommentCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void budgetCommentCriteriaFluentMethodsCreatesFiltersTest() {
        var budgetCommentCriteria = new BudgetCommentCriteria();

        setAllFilters(budgetCommentCriteria);

        assertThat(budgetCommentCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void budgetCommentCriteriaCopyCreatesNullFilterTest() {
        var budgetCommentCriteria = new BudgetCommentCriteria();
        var copy = budgetCommentCriteria.copy();

        assertThat(budgetCommentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(budgetCommentCriteria)
        );
    }

    @Test
    void budgetCommentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var budgetCommentCriteria = new BudgetCommentCriteria();
        setAllFilters(budgetCommentCriteria);

        var copy = budgetCommentCriteria.copy();

        assertThat(budgetCommentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(budgetCommentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var budgetCommentCriteria = new BudgetCommentCriteria();

        assertThat(budgetCommentCriteria).hasToString("BudgetCommentCriteria{}");
    }

    private static void setAllFilters(BudgetCommentCriteria budgetCommentCriteria) {
        budgetCommentCriteria.id();
        budgetCommentCriteria.createdBy();
        budgetCommentCriteria.createdDate();
        budgetCommentCriteria.lastModifiedBy();
        budgetCommentCriteria.lastModifiedDate();
        budgetCommentCriteria.budgetId();
        budgetCommentCriteria.distinct();
    }

    private static Condition<BudgetCommentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getBudgetId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BudgetCommentCriteria> copyFiltersAre(
        BudgetCommentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getBudgetId(), copy.getBudgetId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
