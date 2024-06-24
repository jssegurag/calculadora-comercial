package com.trycore.quotizo.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class PermissionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPermissionAllPropertiesEquals(Permission expected, Permission actual) {
        assertPermissionAutoGeneratedPropertiesEquals(expected, actual);
        assertPermissionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPermissionAllUpdatablePropertiesEquals(Permission expected, Permission actual) {
        assertPermissionUpdatableFieldsEquals(expected, actual);
        assertPermissionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPermissionAutoGeneratedPropertiesEquals(Permission expected, Permission actual) {
        assertThat(expected)
            .as("Verify Permission auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPermissionUpdatableFieldsEquals(Permission expected, Permission actual) {
        assertThat(expected)
            .as("Verify Permission relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
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
    public static void assertPermissionUpdatableRelationshipsEquals(Permission expected, Permission actual) {
        assertThat(expected)
            .as("Verify Permission relationships")
            .satisfies(e -> assertThat(e.getPermissions()).as("check permissions").isEqualTo(actual.getPermissions()));
    }
}
