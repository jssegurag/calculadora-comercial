package com.trycore.quotizo.domain;

import static com.trycore.quotizo.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class FinancialParameterAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFinancialParameterAllPropertiesEquals(FinancialParameter expected, FinancialParameter actual) {
        assertFinancialParameterAutoGeneratedPropertiesEquals(expected, actual);
        assertFinancialParameterAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFinancialParameterAllUpdatablePropertiesEquals(FinancialParameter expected, FinancialParameter actual) {
        assertFinancialParameterUpdatableFieldsEquals(expected, actual);
        assertFinancialParameterUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFinancialParameterAutoGeneratedPropertiesEquals(FinancialParameter expected, FinancialParameter actual) {
        assertThat(expected)
            .as("Verify FinancialParameter auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFinancialParameterUpdatableFieldsEquals(FinancialParameter expected, FinancialParameter actual) {
        assertThat(expected)
            .as("Verify FinancialParameter relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getValue()).as("check value").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getValue()))
            .satisfies(e -> assertThat(e.getActive()).as("check active").isEqualTo(actual.getActive()))
            .satisfies(e -> assertThat(e.getMandatory()).as("check mandatory").isEqualTo(actual.getMandatory()))
            .satisfies(e -> assertThat(e.getCreatedBy()).as("check createdBy").isEqualTo(actual.getCreatedBy()))
            .satisfies(e -> assertThat(e.getCreatedDate()).as("check createdDate").isEqualTo(actual.getCreatedDate()))
            .satisfies(e -> assertThat(e.getLastModifiedBy()).as("check lastModifiedBy").isEqualTo(actual.getLastModifiedBy()))
            .satisfies(e -> assertThat(e.getLastModifiedDate()).as("check lastModifiedDate").isEqualTo(actual.getLastModifiedDate()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFinancialParameterUpdatableRelationshipsEquals(FinancialParameter expected, FinancialParameter actual) {
        assertThat(expected)
            .as("Verify FinancialParameter relationships")
            .satisfies(
                e ->
                    assertThat(e.getFinancialParameterType())
                        .as("check financialParameterType")
                        .isEqualTo(actual.getFinancialParameterType())
            )
            .satisfies(e -> assertThat(e.getCountry()).as("check country").isEqualTo(actual.getCountry()))
            .satisfies(e -> assertThat(e.getAdministrator()).as("check administrator").isEqualTo(actual.getAdministrator()))
            .satisfies(e -> assertThat(e.getRoleAuthorizeds()).as("check roleAuthorizeds").isEqualTo(actual.getRoleAuthorizeds()));
    }
}
