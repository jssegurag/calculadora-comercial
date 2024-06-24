package com.trycore.quotizo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FinancialParameterCriteriaTest {

    @Test
    void newFinancialParameterCriteriaHasAllFiltersNullTest() {
        var financialParameterCriteria = new FinancialParameterCriteria();
        assertThat(financialParameterCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void financialParameterCriteriaFluentMethodsCreatesFiltersTest() {
        var financialParameterCriteria = new FinancialParameterCriteria();

        setAllFilters(financialParameterCriteria);

        assertThat(financialParameterCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void financialParameterCriteriaCopyCreatesNullFilterTest() {
        var financialParameterCriteria = new FinancialParameterCriteria();
        var copy = financialParameterCriteria.copy();

        assertThat(financialParameterCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(financialParameterCriteria)
        );
    }

    @Test
    void financialParameterCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var financialParameterCriteria = new FinancialParameterCriteria();
        setAllFilters(financialParameterCriteria);

        var copy = financialParameterCriteria.copy();

        assertThat(financialParameterCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(financialParameterCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var financialParameterCriteria = new FinancialParameterCriteria();

        assertThat(financialParameterCriteria).hasToString("FinancialParameterCriteria{}");
    }

    private static void setAllFilters(FinancialParameterCriteria financialParameterCriteria) {
        financialParameterCriteria.id();
        financialParameterCriteria.name();
        financialParameterCriteria.value();
        financialParameterCriteria.active();
        financialParameterCriteria.mandatory();
        financialParameterCriteria.createdBy();
        financialParameterCriteria.createdDate();
        financialParameterCriteria.lastModifiedBy();
        financialParameterCriteria.lastModifiedDate();
        financialParameterCriteria.financialParameterTypeId();
        financialParameterCriteria.countryId();
        financialParameterCriteria.administratorId();
        financialParameterCriteria.roleAuthorizedId();
        financialParameterCriteria.distinct();
    }

    private static Condition<FinancialParameterCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getValue()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getMandatory()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getFinancialParameterTypeId()) &&
                condition.apply(criteria.getCountryId()) &&
                condition.apply(criteria.getAdministratorId()) &&
                condition.apply(criteria.getRoleAuthorizedId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FinancialParameterCriteria> copyFiltersAre(
        FinancialParameterCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getValue(), copy.getValue()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getMandatory(), copy.getMandatory()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getFinancialParameterTypeId(), copy.getFinancialParameterTypeId()) &&
                condition.apply(criteria.getCountryId(), copy.getCountryId()) &&
                condition.apply(criteria.getAdministratorId(), copy.getAdministratorId()) &&
                condition.apply(criteria.getRoleAuthorizedId(), copy.getRoleAuthorizedId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
