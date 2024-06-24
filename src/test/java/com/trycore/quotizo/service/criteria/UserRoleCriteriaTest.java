package com.trycore.quotizo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserRoleCriteriaTest {

    @Test
    void newUserRoleCriteriaHasAllFiltersNullTest() {
        var userRoleCriteria = new UserRoleCriteria();
        assertThat(userRoleCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void userRoleCriteriaFluentMethodsCreatesFiltersTest() {
        var userRoleCriteria = new UserRoleCriteria();

        setAllFilters(userRoleCriteria);

        assertThat(userRoleCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void userRoleCriteriaCopyCreatesNullFilterTest() {
        var userRoleCriteria = new UserRoleCriteria();
        var copy = userRoleCriteria.copy();

        assertThat(userRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(userRoleCriteria)
        );
    }

    @Test
    void userRoleCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userRoleCriteria = new UserRoleCriteria();
        setAllFilters(userRoleCriteria);

        var copy = userRoleCriteria.copy();

        assertThat(userRoleCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(userRoleCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userRoleCriteria = new UserRoleCriteria();

        assertThat(userRoleCriteria).hasToString("UserRoleCriteria{}");
    }

    private static void setAllFilters(UserRoleCriteria userRoleCriteria) {
        userRoleCriteria.id();
        userRoleCriteria.name();
        userRoleCriteria.createdBy();
        userRoleCriteria.createdDate();
        userRoleCriteria.lastModifiedBy();
        userRoleCriteria.lastModifiedDate();
        userRoleCriteria.permissionId();
        userRoleCriteria.budgetId();
        userRoleCriteria.financialParameterId();
        userRoleCriteria.usersId();
        userRoleCriteria.distinct();
    }

    private static Condition<UserRoleCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getPermissionId()) &&
                condition.apply(criteria.getBudgetId()) &&
                condition.apply(criteria.getFinancialParameterId()) &&
                condition.apply(criteria.getUsersId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserRoleCriteria> copyFiltersAre(UserRoleCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getPermissionId(), copy.getPermissionId()) &&
                condition.apply(criteria.getBudgetId(), copy.getBudgetId()) &&
                condition.apply(criteria.getFinancialParameterId(), copy.getFinancialParameterId()) &&
                condition.apply(criteria.getUsersId(), copy.getUsersId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
