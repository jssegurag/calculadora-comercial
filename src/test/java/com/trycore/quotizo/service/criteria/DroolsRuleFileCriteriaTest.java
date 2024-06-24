package com.trycore.quotizo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class DroolsRuleFileCriteriaTest {

    @Test
    void newDroolsRuleFileCriteriaHasAllFiltersNullTest() {
        var droolsRuleFileCriteria = new DroolsRuleFileCriteria();
        assertThat(droolsRuleFileCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void droolsRuleFileCriteriaFluentMethodsCreatesFiltersTest() {
        var droolsRuleFileCriteria = new DroolsRuleFileCriteria();

        setAllFilters(droolsRuleFileCriteria);

        assertThat(droolsRuleFileCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void droolsRuleFileCriteriaCopyCreatesNullFilterTest() {
        var droolsRuleFileCriteria = new DroolsRuleFileCriteria();
        var copy = droolsRuleFileCriteria.copy();

        assertThat(droolsRuleFileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(droolsRuleFileCriteria)
        );
    }

    @Test
    void droolsRuleFileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var droolsRuleFileCriteria = new DroolsRuleFileCriteria();
        setAllFilters(droolsRuleFileCriteria);

        var copy = droolsRuleFileCriteria.copy();

        assertThat(droolsRuleFileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(droolsRuleFileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var droolsRuleFileCriteria = new DroolsRuleFileCriteria();

        assertThat(droolsRuleFileCriteria).hasToString("DroolsRuleFileCriteria{}");
    }

    private static void setAllFilters(DroolsRuleFileCriteria droolsRuleFileCriteria) {
        droolsRuleFileCriteria.id();
        droolsRuleFileCriteria.fileName();
        droolsRuleFileCriteria.description();
        droolsRuleFileCriteria.active();
        droolsRuleFileCriteria.createdBy();
        droolsRuleFileCriteria.createdDate();
        droolsRuleFileCriteria.lastModifiedBy();
        droolsRuleFileCriteria.lastModifiedDate();
        droolsRuleFileCriteria.ruleAssignmentId();
        droolsRuleFileCriteria.distinct();
    }

    private static Condition<DroolsRuleFileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFileName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getRuleAssignmentId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<DroolsRuleFileCriteria> copyFiltersAre(
        DroolsRuleFileCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFileName(), copy.getFileName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getRuleAssignmentId(), copy.getRuleAssignmentId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
