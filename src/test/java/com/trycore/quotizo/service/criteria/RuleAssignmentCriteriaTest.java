package com.trycore.quotizo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class RuleAssignmentCriteriaTest {

    @Test
    void newRuleAssignmentCriteriaHasAllFiltersNullTest() {
        var ruleAssignmentCriteria = new RuleAssignmentCriteria();
        assertThat(ruleAssignmentCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void ruleAssignmentCriteriaFluentMethodsCreatesFiltersTest() {
        var ruleAssignmentCriteria = new RuleAssignmentCriteria();

        setAllFilters(ruleAssignmentCriteria);

        assertThat(ruleAssignmentCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void ruleAssignmentCriteriaCopyCreatesNullFilterTest() {
        var ruleAssignmentCriteria = new RuleAssignmentCriteria();
        var copy = ruleAssignmentCriteria.copy();

        assertThat(ruleAssignmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(ruleAssignmentCriteria)
        );
    }

    @Test
    void ruleAssignmentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ruleAssignmentCriteria = new RuleAssignmentCriteria();
        setAllFilters(ruleAssignmentCriteria);

        var copy = ruleAssignmentCriteria.copy();

        assertThat(ruleAssignmentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(ruleAssignmentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ruleAssignmentCriteria = new RuleAssignmentCriteria();

        assertThat(ruleAssignmentCriteria).hasToString("RuleAssignmentCriteria{}");
    }

    private static void setAllFilters(RuleAssignmentCriteria ruleAssignmentCriteria) {
        ruleAssignmentCriteria.id();
        ruleAssignmentCriteria.entityName();
        ruleAssignmentCriteria.entityId();
        ruleAssignmentCriteria.createdBy();
        ruleAssignmentCriteria.createdDate();
        ruleAssignmentCriteria.lastModifiedBy();
        ruleAssignmentCriteria.lastModifiedDate();
        ruleAssignmentCriteria.droolsRuleFileId();
        ruleAssignmentCriteria.distinct();
    }

    private static Condition<RuleAssignmentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEntityName()) &&
                condition.apply(criteria.getEntityId()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDroolsRuleFileId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<RuleAssignmentCriteria> copyFiltersAre(
        RuleAssignmentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEntityName(), copy.getEntityName()) &&
                condition.apply(criteria.getEntityId(), copy.getEntityId()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDroolsRuleFileId(), copy.getDroolsRuleFileId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
