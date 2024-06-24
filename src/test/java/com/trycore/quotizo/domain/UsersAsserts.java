package com.trycore.quotizo.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsersAllPropertiesEquals(Users expected, Users actual) {
        assertUsersAutoGeneratedPropertiesEquals(expected, actual);
        assertUsersAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsersAllUpdatablePropertiesEquals(Users expected, Users actual) {
        assertUsersUpdatableFieldsEquals(expected, actual);
        assertUsersUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsersAutoGeneratedPropertiesEquals(Users expected, Users actual) {
        assertThat(expected)
            .as("Verify Users auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUsersUpdatableFieldsEquals(Users expected, Users actual) {
        assertThat(expected)
            .as("Verify Users relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getEmail()).as("check email").isEqualTo(actual.getEmail()))
            .satisfies(e -> assertThat(e.getPassword()).as("check password").isEqualTo(actual.getPassword()))
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
    public static void assertUsersUpdatableRelationshipsEquals(Users expected, Users actual) {
        assertThat(expected)
            .as("Verify Users relationships")
            .satisfies(e -> assertThat(e.getUserRoles()).as("check userRoles").isEqualTo(actual.getUserRoles()))
            .satisfies(e -> assertThat(e.getBudgetAuthorizeds()).as("check budgetAuthorizeds").isEqualTo(actual.getBudgetAuthorizeds()));
    }
}