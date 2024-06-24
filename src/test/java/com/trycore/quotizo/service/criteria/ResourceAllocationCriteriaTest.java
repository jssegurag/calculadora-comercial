package com.trycore.quotizo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ResourceAllocationCriteriaTest {

    @Test
    void newResourceAllocationCriteriaHasAllFiltersNullTest() {
        var resourceAllocationCriteria = new ResourceAllocationCriteria();
        assertThat(resourceAllocationCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void resourceAllocationCriteriaFluentMethodsCreatesFiltersTest() {
        var resourceAllocationCriteria = new ResourceAllocationCriteria();

        setAllFilters(resourceAllocationCriteria);

        assertThat(resourceAllocationCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void resourceAllocationCriteriaCopyCreatesNullFilterTest() {
        var resourceAllocationCriteria = new ResourceAllocationCriteria();
        var copy = resourceAllocationCriteria.copy();

        assertThat(resourceAllocationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(resourceAllocationCriteria)
        );
    }

    @Test
    void resourceAllocationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var resourceAllocationCriteria = new ResourceAllocationCriteria();
        setAllFilters(resourceAllocationCriteria);

        var copy = resourceAllocationCriteria.copy();

        assertThat(resourceAllocationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(resourceAllocationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var resourceAllocationCriteria = new ResourceAllocationCriteria();

        assertThat(resourceAllocationCriteria).hasToString("ResourceAllocationCriteria{}");
    }

    private static void setAllFilters(ResourceAllocationCriteria resourceAllocationCriteria) {
        resourceAllocationCriteria.id();
        resourceAllocationCriteria.assignedHours();
        resourceAllocationCriteria.totalCost();
        resourceAllocationCriteria.units();
        resourceAllocationCriteria.capacity();
        resourceAllocationCriteria.plannedHours();
        resourceAllocationCriteria.createdBy();
        resourceAllocationCriteria.createdDate();
        resourceAllocationCriteria.lastModifiedBy();
        resourceAllocationCriteria.lastModifiedDate();
        resourceAllocationCriteria.budgetId();
        resourceAllocationCriteria.resourceId();
        resourceAllocationCriteria.budgetTemplateId();
        resourceAllocationCriteria.distinct();
    }

    private static Condition<ResourceAllocationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getAssignedHours()) &&
                condition.apply(criteria.getTotalCost()) &&
                condition.apply(criteria.getUnits()) &&
                condition.apply(criteria.getCapacity()) &&
                condition.apply(criteria.getPlannedHours()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getBudgetId()) &&
                condition.apply(criteria.getResourceId()) &&
                condition.apply(criteria.getBudgetTemplateId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ResourceAllocationCriteria> copyFiltersAre(
        ResourceAllocationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getAssignedHours(), copy.getAssignedHours()) &&
                condition.apply(criteria.getTotalCost(), copy.getTotalCost()) &&
                condition.apply(criteria.getUnits(), copy.getUnits()) &&
                condition.apply(criteria.getCapacity(), copy.getCapacity()) &&
                condition.apply(criteria.getPlannedHours(), copy.getPlannedHours()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getBudgetId(), copy.getBudgetId()) &&
                condition.apply(criteria.getResourceId(), copy.getResourceId()) &&
                condition.apply(criteria.getBudgetTemplateId(), copy.getBudgetTemplateId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
