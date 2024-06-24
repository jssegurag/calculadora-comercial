package com.trycore.quotizo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UsersCriteriaTest {

    @Test
    void newUsersCriteriaHasAllFiltersNullTest() {
        var usersCriteria = new UsersCriteria();
        assertThat(usersCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void usersCriteriaFluentMethodsCreatesFiltersTest() {
        var usersCriteria = new UsersCriteria();

        setAllFilters(usersCriteria);

        assertThat(usersCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void usersCriteriaCopyCreatesNullFilterTest() {
        var usersCriteria = new UsersCriteria();
        var copy = usersCriteria.copy();

        assertThat(usersCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(usersCriteria)
        );
    }

    @Test
    void usersCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var usersCriteria = new UsersCriteria();
        setAllFilters(usersCriteria);

        var copy = usersCriteria.copy();

        assertThat(usersCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(usersCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var usersCriteria = new UsersCriteria();

        assertThat(usersCriteria).hasToString("UsersCriteria{}");
    }

    private static void setAllFilters(UsersCriteria usersCriteria) {
        usersCriteria.id();
        usersCriteria.name();
        usersCriteria.email();
        usersCriteria.password();
        usersCriteria.active();
        usersCriteria.createdBy();
        usersCriteria.createdDate();
        usersCriteria.lastModifiedBy();
        usersCriteria.lastModifiedDate();
        usersCriteria.budgetId();
        usersCriteria.financialParameterId();
        usersCriteria.userRoleId();
        usersCriteria.budgetAuthorizedId();
        usersCriteria.assignedToId();
        usersCriteria.approvedById();
        usersCriteria.distinct();
    }

    private static Condition<UsersCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPassword()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getBudgetId()) &&
                condition.apply(criteria.getFinancialParameterId()) &&
                condition.apply(criteria.getUserRoleId()) &&
                condition.apply(criteria.getBudgetAuthorizedId()) &&
                condition.apply(criteria.getAssignedToId()) &&
                condition.apply(criteria.getApprovedById()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UsersCriteria> copyFiltersAre(UsersCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPassword(), copy.getPassword()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getBudgetId(), copy.getBudgetId()) &&
                condition.apply(criteria.getFinancialParameterId(), copy.getFinancialParameterId()) &&
                condition.apply(criteria.getUserRoleId(), copy.getUserRoleId()) &&
                condition.apply(criteria.getBudgetAuthorizedId(), copy.getBudgetAuthorizedId()) &&
                condition.apply(criteria.getAssignedToId(), copy.getAssignedToId()) &&
                condition.apply(criteria.getApprovedById(), copy.getApprovedById()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
