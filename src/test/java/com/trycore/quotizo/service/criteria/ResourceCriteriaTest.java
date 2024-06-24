package com.trycore.quotizo.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ResourceCriteriaTest {

    @Test
    void newResourceCriteriaHasAllFiltersNullTest() {
        var resourceCriteria = new ResourceCriteria();
        assertThat(resourceCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void resourceCriteriaFluentMethodsCreatesFiltersTest() {
        var resourceCriteria = new ResourceCriteria();

        setAllFilters(resourceCriteria);

        assertThat(resourceCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void resourceCriteriaCopyCreatesNullFilterTest() {
        var resourceCriteria = new ResourceCriteria();
        var copy = resourceCriteria.copy();

        assertThat(resourceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(resourceCriteria)
        );
    }

    @Test
    void resourceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var resourceCriteria = new ResourceCriteria();
        setAllFilters(resourceCriteria);

        var copy = resourceCriteria.copy();

        assertThat(resourceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(resourceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var resourceCriteria = new ResourceCriteria();

        assertThat(resourceCriteria).hasToString("ResourceCriteria{}");
    }

    private static void setAllFilters(ResourceCriteria resourceCriteria) {
        resourceCriteria.id();
        resourceCriteria.salary();
        resourceCriteria.hourlyRate();
        resourceCriteria.active();
        resourceCriteria.createdBy();
        resourceCriteria.createdDate();
        resourceCriteria.lastModifiedBy();
        resourceCriteria.lastModifiedDate();
        resourceCriteria.positionId();
        resourceCriteria.resourceAllocationId();
        resourceCriteria.distinct();
    }

    private static Condition<ResourceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSalary()) &&
                condition.apply(criteria.getHourlyRate()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getResourceAllocationId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ResourceCriteria> copyFiltersAre(ResourceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSalary(), copy.getSalary()) &&
                condition.apply(criteria.getHourlyRate(), copy.getHourlyRate()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getResourceAllocationId(), copy.getResourceAllocationId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
