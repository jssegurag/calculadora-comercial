package com.trycore.quotizo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FinancialParameterTypeCriteriaTest {

    @Test
    void newFinancialParameterTypeCriteriaHasAllFiltersNullTest() {
        var financialParameterTypeCriteria = new FinancialParameterTypeCriteria();
        assertThat(financialParameterTypeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void financialParameterTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var financialParameterTypeCriteria = new FinancialParameterTypeCriteria();

        setAllFilters(financialParameterTypeCriteria);

        assertThat(financialParameterTypeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void financialParameterTypeCriteriaCopyCreatesNullFilterTest() {
        var financialParameterTypeCriteria = new FinancialParameterTypeCriteria();
        var copy = financialParameterTypeCriteria.copy();

        assertThat(financialParameterTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(financialParameterTypeCriteria)
        );
    }

    @Test
    void financialParameterTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var financialParameterTypeCriteria = new FinancialParameterTypeCriteria();
        setAllFilters(financialParameterTypeCriteria);

        var copy = financialParameterTypeCriteria.copy();

        assertThat(financialParameterTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(financialParameterTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var financialParameterTypeCriteria = new FinancialParameterTypeCriteria();

        assertThat(financialParameterTypeCriteria).hasToString("FinancialParameterTypeCriteria{}");
    }

    private static void setAllFilters(FinancialParameterTypeCriteria financialParameterTypeCriteria) {
        financialParameterTypeCriteria.id();
        financialParameterTypeCriteria.name();
        financialParameterTypeCriteria.active();
        financialParameterTypeCriteria.createdBy();
        financialParameterTypeCriteria.createdDate();
        financialParameterTypeCriteria.lastModifiedBy();
        financialParameterTypeCriteria.lastModifiedDate();
        financialParameterTypeCriteria.financialParameterId();
        financialParameterTypeCriteria.distinct();
    }

    private static Condition<FinancialParameterTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getFinancialParameterId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FinancialParameterTypeCriteria> copyFiltersAre(
        FinancialParameterTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getFinancialParameterId(), copy.getFinancialParameterId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
