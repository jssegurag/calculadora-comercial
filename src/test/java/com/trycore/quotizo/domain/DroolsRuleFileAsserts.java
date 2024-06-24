package com.trycore.quotizo.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class DroolsRuleFileAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDroolsRuleFileAllPropertiesEquals(DroolsRuleFile expected, DroolsRuleFile actual) {
        assertDroolsRuleFileAutoGeneratedPropertiesEquals(expected, actual);
        assertDroolsRuleFileAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDroolsRuleFileAllUpdatablePropertiesEquals(DroolsRuleFile expected, DroolsRuleFile actual) {
        assertDroolsRuleFileUpdatableFieldsEquals(expected, actual);
        assertDroolsRuleFileUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDroolsRuleFileAutoGeneratedPropertiesEquals(DroolsRuleFile expected, DroolsRuleFile actual) {
        assertThat(expected)
            .as("Verify DroolsRuleFile auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertDroolsRuleFileUpdatableFieldsEquals(DroolsRuleFile expected, DroolsRuleFile actual) {
        assertThat(expected)
            .as("Verify DroolsRuleFile relevant properties")
            .satisfies(e -> assertThat(e.getFileName()).as("check fileName").isEqualTo(actual.getFileName()))
            .satisfies(e -> assertThat(e.getFileContent()).as("check fileContent").isEqualTo(actual.getFileContent()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getActive()).as("check active").isEqualTo(actual.getActive()))
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
    public static void assertDroolsRuleFileUpdatableRelationshipsEquals(DroolsRuleFile expected, DroolsRuleFile actual) {}
}
